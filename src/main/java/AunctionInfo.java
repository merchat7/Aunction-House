/**
 * AunctionInfo is only used by the Jackson unmarshaller to parse the bid (json) from the client to an aunction
 */
public class AunctionInfo {
    private String bidder_id = null;
    private Integer bid_price = null;

    // This constructor is necesary for Jackson unmarshaller to work
    public AunctionInfo() {
    }

    public AunctionInfo(String bidderID, Integer bidPrice) {
        this.bidder_id = bidderID;
        this.bid_price = bidPrice;
    }

    public String getBidder_id() {
        return bidder_id;
    }

    public void setBidder_id(String bidderID) {
        this.bidder_id = bidderID;
    }

    public Integer getBid_price() {
        return bid_price;
    }

    public void setBid_price(Integer bidPrice) {
        this.bid_price = bidPrice;
    }
}

