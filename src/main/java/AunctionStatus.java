/**
 * AunctionStatus (and classes that extend it) is only used by the Jackson marshaller to display the status when making a bid
 */
public abstract class AunctionStatus {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
