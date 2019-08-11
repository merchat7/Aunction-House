/**
 * AunctionInfoWinner is used by the Jackson marshaller to display the winning bid information
 * Note that it is the same as AunctionInfo, but with slightly different variable names for displaying purposes
 */
public class AunctionInfoWinner {
    private String bid_winner_id = null;
    private Integer bid_price = null;

    public AunctionInfoWinner(String bidderID, Integer bidPrice) {
        this.bid_winner_id = bidderID;
        this.bid_price = bidPrice;
    }

    public String getBid_winner_id() {
        return bid_winner_id;
    }

    public void setBid_winner_id(String bidderID) {
        this.bid_winner_id = bidderID;
    }

    public Integer getBid_price() {
        return bid_price;
    }

    public void setBid_price(Integer bidPrice) {
        this.bid_price = bidPrice;
    }
}
