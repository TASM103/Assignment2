public class TelemetryThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("[TelemetryThread] CPU & Memory Health Update " + i + " - Thread Name: " + Thread.currentThread().getName());
            try {
                Thread.sleep(800); // 800 milliseconds pause
            } catch (InterruptedException e) {
                System.out.println("TelemetryThread interrupted.");
            }
        }
    }
}