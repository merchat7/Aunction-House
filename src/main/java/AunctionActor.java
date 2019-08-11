import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The actor process all aunction requests sequentially, then forward the request to the workerActor associated with the aunction if it exist
 * Note that after it finishes forwarding, it does not wait (or shouldn't wait) for the result and simply process the next requests
 */
public class AunctionActor extends AbstractActor {

    static Props props() {
        return Props.create(AunctionActor.class);
    }

    private final Map<Integer, ActorRef> aunctions =new HashMap<>();
    private Integer currentAvailID = 1;

    /**
     * Create a worker actor to store the aunction information and handle all requests to it sequentially
     * The aunctions are stored in a hashmap in memory.
     * The uniqueness of name are guaranteed by incrementing a counter
     */
    private Aunction createAunction () {
        aunctions.put(currentAvailID, getContext().actorOf(WorkerActor.props()));
        currentAvailID += 1;
        return new Aunction(currentAvailID-1);
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                // Aunction creation requests are handle by this actor
                .match(AunctionMessages.CreateAunction.class, createAunction -> getSender().tell(createAunction(), getSelf()))
                // Aunction get/put/delete requests are handled by the worker if the aunction exist
                .match(Work.class, work -> {
                    ActorRef worker = aunctions.get(work.getAunctionID());
                    if (worker != null) {
                        worker.forward(work.getWork(), getContext());
                        // A delete operation is the same as a GET [aunction], so the work should be forward first before deleting the aunction
                        if (work.getWork().getClass() == AunctionMessages.DeleteAunction.class) {
                            worker.tell(PoisonPill.getInstance(), getSelf()); // Worker will finish the work then kill itself (may be unnecessary)
                            aunctions.remove(work.getAunctionID());
                        }
                    }
                    // AunctionRoutes expects an optional, and optional is used to indicate whether the aunction exist or not and return quickly
                    else getSender().tell(Optional.empty(), getSelf());
                })
                .build();
    }
}