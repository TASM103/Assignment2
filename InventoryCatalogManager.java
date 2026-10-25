import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InventoryCatalogManager {
    
    public static BenchmarkResult benchmarkSynchronizedMap() throws InterruptedException {
        Map<String, Integer> rawMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            rawMap.put("PROD-" + i, 1000);
        }
        Map<String, Integer> syncMap = Collections.synchronizedMap(rawMap);
        
        int numThreads = 10;
        int operationsPerThread = 50000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        long startTime = System.nanoTime();
        
        for (int t = 0; t < numThreads; t++) {
            executor.submit(() -> {
                for (int i = 0; i < operationsPerThread; i++) {
                    String key = "PROD-" + (i % 100);
                    synchronized (syncMap) {
                        Integer stock = syncMap.get(key);
                        if (stock != null) {
                            syncMap.put(key, stock + 1);
                        }
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.nanoTime();
        
        long durationNs = endTime - startTime;
        double durationSec = durationNs / 1_000_000_000.0;
        long totalOps = (long) numThreads * operationsPerThread;
        double throughput = totalOps / durationSec;
        
        return new BenchmarkResult("Collections.synchronizedMap (HashMap)", durationNs, throughput);
    }
    
    public static BenchmarkResult benchmarkConcurrentMap() throws InterruptedException {
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            concurrentMap.put("PROD-" + i, 1000);
        }
        
        int numThreads = 10;
        int operationsPerThread = 50000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        long startTime = System.nanoTime();
        
        for (int t = 0; t < numThreads; t++) {
            executor.submit(() -> {
                for (int i = 0; i < operationsPerThread; i++) {
                    String key = "PROD-" + (i % 100);
                    concurrentMap.compute(key, (k, v) -> (v == null) ? 1 : v + 1);
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.nanoTime();
        
        long durationNs = endTime - startTime;
        double durationSec = durationNs / 1_000_000_000.0;
        long totalOps = (long) numThreads * operationsPerThread;
        double throughput = totalOps / durationSec;
        
        return new BenchmarkResult("ConcurrentHashMap", durationNs, throughput);
    }
    
    public static class BenchmarkResult {
        String name;
        long durationNs;
        double throughput;
        
        public BenchmarkResult(String name, long durationNs, double throughput) {
            this.name = name;
            this.durationNs = durationNs;
            this.throughput = throughput;
        }
        
        public void printReport() {
            System.out.println("--- " + name + " Benchmark ---");
            System.out.println("Execution Time: " + durationNs + " ns (" + (durationNs / 1_000_000.0) + " ms)");
            System.out.println("Throughput: " + String.format("%.2f", throughput) + " ops/sec\n");
        }
    }
}
