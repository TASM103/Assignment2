public class ReutersQuoteEvent extends TradeEvent {
    private String quoteId;

    public ReutersQuoteEvent(String senderId, long timestamp, double basePrice, String quoteId) {
        super(senderId, timestamp, basePrice);
        this.quoteId = quoteId;
    }

    @Override
    public void dispatch() {
        System.out.println("Reuters Quote Event -> Sender ID: " + senderId + ", Timestamp: " + timestamp + ", Base Price: " + basePrice + ", Quote ID: " + quoteId);
    }
}