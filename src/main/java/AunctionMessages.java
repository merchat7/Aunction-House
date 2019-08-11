import java.io.Serializable;

/**
 * This is used for communication between AunctionRoutes and the AunctionActors's WorkerActor
 */
public interface AunctionMessages {
    class CreateAunction implements Serializable {}

    class DeleteAunction implements Serializable {}

    class GetAunction implements Serializable {}

    class BidAunction implements Serializable {
        private AunctionInfo aunctionInfo;

        BidAunction (AunctionInfo aunctionInfo) {
            this.aunctionInfo = aunctionInfo;
        }

        public AunctionInfo getAunctionInfo() {
            return aunctionInfo;
        }
    }
}

