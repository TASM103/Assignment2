public class FIXOrderEvent extends TradeEvent {
    private String orderId;

    public FIXOrderEvent(String senderId, long timestamp, double basePrice, String orderId) {
        super(senderId, timestamp, basePrice);
        this.orderId = orderId;
    }

    @Override
    public void dispatch() {
        System.out.println("FIX Order Event -> Sender ID: " + senderId + ", Timestamp: " + timestamp + ", Base Price: " + basePrice + ", Order ID: " + orderId);
    }
}