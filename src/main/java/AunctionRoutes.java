import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Maps the request to the right work, and send it to the AunctionActor, then returns the correct response
 */
public class AunctionRoutes extends AllDirectives {
    final private ActorRef aunctionActor;

    public AunctionRoutes(ActorRef aunctionActor) {
        this.aunctionActor = aunctionActor;
    }

    Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS)); // usually we'd obtain the timeout from the system's configuration


    public Route routes() {
        return route(pathPrefix("aunction", () ->
                route(
                        // aunction/[aunctionID]
                        path(PathMatchers.segment(), aunctionID -> route(
                                getAunction(Integer.parseInt(aunctionID)),
                                postAunction(Integer.parseInt(aunctionID)),
                                deleteAunction(Integer.parseInt(aunctionID))
                                )
                        ),
                        // aunction?create
                        parameter("create", create -> route(createAunction()))
                )
        ));
    }

    private Route createAunction() {
        return post(() -> {
            CompletionStage<Aunction> aunction = PatternsCS
                    .ask(aunctionActor, new AunctionMessages.CreateAunction(), timeout)
                    .thenApply(obj -> (Aunction) obj);
            return onSuccess(() -> aunction,
                    performed -> complete(StatusCodes.OK, performed, Jackson.marshaller()));
        });
    }

    private Route getAunction(Integer aunctionID) {
        return get(() -> {
            CompletionStage<Optional<AunctionInfoWinner>> aunctionInfo = PatternsCS
                    .ask(aunctionActor, new Work(new AunctionMessages.GetAunction(), aunctionID), timeout)
                    .thenApply(obj -> (Optional<AunctionInfoWinner>) obj);
            return onSuccess(() -> aunctionInfo, performed -> {
                if (performed.isPresent()) return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                else return complete(StatusCodes.NOT_FOUND);
            });
        });
    }

    private Route postAunction(Integer aunctionID) {
        return post(() -> entity(Jackson.unmarshaller(AunctionInfo.class), bid -> {
            CompletionStage<Optional<AunctionStatus>> status = PatternsCS
                    .ask(aunctionActor, new Work(new AunctionMessages.BidAunction(bid), aunctionID), timeout)
                    .thenApply(obj -> (Optional<AunctionStatus>) obj);
            return onSuccess(() -> status, performed -> {
                if (performed.isPresent()) return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                else return complete(StatusCodes.NOT_FOUND);
            });
        }));
    }

    private Route deleteAunction(Integer aunctionID) {
        return delete(() -> {
            CompletionStage<Optional<AunctionInfoWinner>> status = PatternsCS
                    .ask(aunctionActor, new Work(new AunctionMessages.DeleteAunction(), aunctionID), timeout)
                    .thenApply(obj -> (Optional<AunctionInfoWinner>) obj);
            return onSuccess(() -> status, performed -> {
                if (performed.isPresent()) return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                else return complete(StatusCodes.NOT_FOUND);
            });
        });
    }
}
