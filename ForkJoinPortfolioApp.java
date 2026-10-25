import java.util.concurrent.ForkJoinPool;
import java.util.Random;

public class ForkJoinPortfolioApp {
    public static void main(String[] args) {
        int size = 10000;
        double[] assets = new double[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            assets[i] = 100.0 + (random.nextDouble() * 1900.0);
        }

        ForkJoinPool pool = new ForkJoinPool();

        long serialStart = System.nanoTime();
        double serialSum = 0.0;
        for (double val : assets) {
            serialSum += val;
        }
        long serialEnd = System.nanoTime();
        long serialDuration = serialEnd - serialStart;

        System.out.println("--- Serial Processing ---");
        System.out.println("Serial Cumulative Risk Score: " + serialSum);
        System.out.println("Serial Execution Time: " + serialDuration + " ns\n");

        long parallelStart = System.nanoTime();
        ParallelRiskCalculator riskCalculator = new ParallelRiskCalculator(assets, 0, assets.length);
        double parallelSum = pool.invoke(riskCalculator);
        long parallelEnd = System.nanoTime();
        long parallelDuration = parallelEnd - parallelStart;

        System.out.println("--- Parallel Fork/Join Processing ---");
        System.out.println("Parallel Cumulative Risk Score: " + parallelSum);
        System.out.println("Parallel Execution Time: " + parallelDuration + " ns\n");

        double riskCap = 1000.0;
        PortfolioThresholdTask thresholdTask = new PortfolioThresholdTask(assets, 0, assets.length, riskCap);
        pool.invoke(thresholdTask);
        
        System.out.println("Status: Applied 5% volatility stress-test penalty to assets exceeding risk cap: " + riskCap);
    }
}
