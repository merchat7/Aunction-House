/**
 * Aunction is only used for the Jackson marshaller to tell the client the AunctionID via json
 */
public class Aunction {
    private final Integer aunction_id;

    public Aunction (Integer aunctionID) {
        this.aunction_id = aunctionID;
    }

    public Integer getAunction_id() {
        return aunction_id;
    }
}

