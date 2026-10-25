public class TradeEvent {
    protected String senderId;
    protected long timestamp;
    protected basePrice; // double type

    // Parameterized constructor
    public TradeEvent(String senderId, long timestamp, double basePrice) {
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.basePrice = basePrice;
    }

    // Baseline dispatch method
    public void dispatch() {
        System.out.println("Generic Trade Event -> Sender ID: " + senderId + ", Timestamp: " + timestamp + ", Base Price: " + basePrice);
    }
}