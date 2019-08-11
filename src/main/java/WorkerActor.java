import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.Optional;

/**
 * The worker actor stores an aunction information, and is responsible for handling all requests to it
 */
public class WorkerActor extends AbstractActor {
    static Props props() {
        return Props.create(WorkerActor.class);
    }

    private String bidderID = null;
    private Integer bidPrice = null;

    private AunctionInfoWinner getAunctionInfo() {
        return new AunctionInfoWinner(bidderID, bidPrice);
    }

    private AunctionStatus updateBidStatus (AunctionInfo bid) {
        if (bidPrice == null || bid.getBid_price() > bidPrice) {
            bidderID = bid.getBidder_id();
            bidPrice = bid.getBid_price();
            return new AunctionStatusWinning("won");
        }
        return new AunctionStatusLosing("lost", bidPrice);
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                .match(AunctionMessages.GetAunction.class, getAunction -> getSender().tell(Optional.of(getAunctionInfo()), getContext().getParent()))
                .match(AunctionMessages.BidAunction.class, bidAunction -> getSender().tell(Optional.of(updateBidStatus(bidAunction.getAunctionInfo())), getContext().getParent()))
                .match(AunctionMessages.DeleteAunction.class, deleteAunction ->  getSender().tell(Optional.of(getAunctionInfo()), getContext().getParent()))
                .build();
    }
}
