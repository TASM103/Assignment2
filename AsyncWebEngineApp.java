public class AsyncWebEngineApp {
    public static void main(String[] args) {
        // Display default JVM thread name
        System.out.println("Default JVM Thread Name: " + Thread.currentThread().getName());

        // Instantiate TelemetryThread and assign custom name
        TelemetryThread telemetryThread = new TelemetryThread();
        telemetryThread.setName("Telemetry-Worker");

        // Instantiate LogProcessorTask and pass to new Thread object with custom name
        LogProcessorTask logTask = new LogProcessorTask();
        Thread logThread = new Thread(logTask, "Log-Processor-Worker");

        // Start both concurrent threads
        telemetryThread.start();
        logThread.start();

        // Wait for threads to complete execution
        try {
            telemetryThread.join();
            logThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Async Web Engine Diagnostics Complete.");
    }
}