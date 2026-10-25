import java.util.Random;

public class CacheLocalityBenchmark {
    private static final int N = 2000;
    private static final int ITERATIONS = 10;

    public static double traverseRowMajor(double[][] matrix) {
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static double traverseColumnMajor(double[][] matrix) {
        double sum = 0.0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("Initializing 2000 x 2000 double-precision matrix...");
        double[][] matrix = new double[N][N];
        Random random = new Random();

        // Populate matrix with random values
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = random.nextDouble() * 100.0;
            }
        }

        // Warm up JVM / JIT compiler
        traverseRowMajor(matrix);
        traverseColumnMajor(matrix);

        long totalRowMajorTimeNs = 0;
        long totalColMajorTimeNs = 0;
        double dummySum = 0.0;

        // Execute across 10 repeated iterations
        for (int it = 1; it <= ITERATIONS; it++) {
            // Row-Major Benchmark
            long startRow = System.nanoTime();
            dummySum += traverseRowMajor(matrix);
            long endRow = System.nanoTime();
            totalRowMajorTimeNs += (endRow - startRow);

            // Column-Major Benchmark
            long startCol = System.nanoTime();
            dummySum += traverseColumnMajor(matrix);
            long endCol = System.nanoTime();
            totalColMajorTimeNs += (endCol - startCol);
        }

        // Convert times to milliseconds
        double avgRowMajorMs = (totalRowMajorTimeNs / (double) ITERATIONS) / 1_000_000.0;
        double avgColMajorMs = (totalColMajorTimeNs / (double) ITERATIONS) / 1_000_000.0;

        // Compute speedup ratio of Row-Major over Column-Major
        double speedupRatio = avgColMajorMs / avgRowMajorMs;

        // Print Execution Report
        System.out.println("\n==================================================");
        System.out.println("          CACHE LOCALITY PERFORMANCE REPORT       ");
        System.out.println("==================================================");
        System.out.println("Matrix Size: " + N + " x " + N + " (" + (N * N) + " elements)");
        System.out.println("Iterations : " + ITERATIONS);
        System.out.println("--------------------------------------------------");
        System.out.println("Row-Major Average Time    : " + String.format("%.4f", avgRowMajorMs) + " ms");
        System.out.println("Column-Major Average Time : " + String.format("%.4f", avgColMajorMs) + " ms");
        System.out.println("Speedup Ratio (Col/Row)   : " + String.format("%.2f", speedupRatio) + "x");
        System.out.println("--------------------------------------------------");
        System.out.println("WRITTEN EXPLANATION & ARCHITECTURAL ANALYSIS:");
        System.out.println("1. Spatial Locality & CPU Cache Lines:");
        System.out.println("   Modern CPUs fetch memory into L1/L2 caches in contiguous blocks ");
        System.out.println("   called cache lines (typically 64 bytes).");
        System.out.println("2. Row-Major Traversal Efficiency:");
        System.out.println("   Java stores 2D arrays as arrays of arrays where rows are stored");
        System.out.println("   contiguously in memory. Traversing row-by-row accesses elements");
        System.out.println("   sequentially, maximizing cache hits and prefetching efficiency.");
        System.out.println("3. Column-Major Stalls:");
        System.out.println("   Traversing column-by-column jumps across memory strides (skipping");
        System.out.println("   2000 elements per step), causing frequent cache misses, L3 cache");
        System.out.println("   evictions, and memory bus stalls while waiting for RAM.");
        System.out.println("==================================================\n");
    }
}
