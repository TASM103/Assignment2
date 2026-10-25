public class TradingEngineApp {
    public static void main(String[] args) {
        TradeEvent genericEvent = new TradeEvent("S101", System.currentTimeMillis(), 150.50);
        TradeEvent fixEvent = new FIXOrderEvent("S102", System.currentTimeMillis(), 200.00, "ORD99823");
        TradeEvent reutersEvent = new ReutersQuoteEvent("S103", System.currentTimeMillis(), 305.75, "QT44102");

        genericEvent.dispatch();
        fixEvent.dispatch();
        reutersEvent.dispatch();
    }
}