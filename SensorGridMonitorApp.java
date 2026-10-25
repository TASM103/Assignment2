import java.util.Random;

public class SensorGridMonitorApp {
    public static void main(String[] args) throws InterruptedException {
        int datasetSize = 50000;
        double[] telemetryArray = new double[datasetSize];
        Random random = new Random();

        for (int i = 0; i < datasetSize; i++) {
            telemetryArray[i] = -50.0 + (random.nextDouble() * 200.0);
        }

        int numWorkers = 4;
        int chunkSize = datasetSize / numWorkers;

        ParallelMinSearch[] minTasks = new ParallelMinSearch[numWorkers];
        ParallelMaxSearch[] maxTasks = new ParallelMaxSearch[numWorkers];
        Thread[] minThreads = new Thread[numWorkers];
        Thread[] maxThreads = new Thread[numWorkers];

        long startTime = System.nanoTime();

        // Instantiate worker threads across partitioned array segments
        for (int i = 0; i < numWorkers; i++) {
            int start = i * chunkSize;
            int end = (i == numWorkers - 1) ? datasetSize : start + chunkSize;

            minTasks[i] = new ParallelMinSearch(telemetryArray, start, end);
            maxTasks[i] = new ParallelMaxSearch(telemetryArray, start, end);

            minThreads[i] = new Thread(minTasks[i], "MinWorker-" + i);
            maxThreads[i] = new Thread(maxTasks[i], "MaxWorker-" + i);

            minThreads[i].start();
            maxThreads[i].start();
        }

        // Wait for all worker threads to complete execution
        for (int i = 0; i < numWorkers; i++) {
            minThreads[i].join();
            maxThreads[i].join();
        }

        // Aggregate final global critical threshold metrics
        double globalMin = Double.MAX_VALUE;
        double globalMax = Double.MIN_VALUE;

        for (int i = 0; i < numWorkers; i++) {
            if (minTasks[i].getResult() < globalMin) {
                globalMin = minTasks[i].getResult();
            }
            if (maxTasks[i].getResult() > globalMax) {
                globalMax = maxTasks[i].getResult();
            }
        }

        long endTime = System.nanoTime();

        System.out.println("--- IoT Sensor Grid Monitoring Engine ---");
        System.out.println("Global Min Temperature: " + globalMin);
        System.out.println("Global Max Peak Vibration: " + globalMax);
        System.out.println("Parallel Reduction Execution Time: " + (endTime - startTime) + " ns");
    }
}