public class AunctionStatusLosing extends AunctionStatus {
    private final Integer current_price;

    public AunctionStatusLosing(String status, Integer currentPrice) {
        this.setStatus(status);
        this.current_price = currentPrice;
    }

    public Integer getCurrent_price() {
        return current_price;
    }
}
