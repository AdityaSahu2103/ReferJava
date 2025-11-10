package optimize;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ACO_TSP {
    public static class Result {
        public final int[] bestTour;
        public final double bestLength;
        public final List<IterationLog> history;
        
        public Result(int[] bestTour, double bestLength, List<IterationLog> history) {
            this.bestTour = bestTour;
            this.bestLength = bestLength;
            this.history = history;
        }
    }

    public static class IterationLog {
        public final int iteration;
        public final double bestLength;
        public final double avgLength;
        public final double minLength;
        public final double maxLength;
        public final double avgPheromone;
        
        public IterationLog(int iteration, double bestLength, double avgLength,
                           double minLength, double maxLength, double avgPheromone) {
            this.iteration = iteration;
            this.bestLength = bestLength;
            this.avgLength = avgLength;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.avgPheromone = avgPheromone;
        }
    }

    private final int n;
    private final double[][] dist;
    private final int antCount;
    private final int iterations;
    private final double alpha;
    private final double beta;
    private final double rho;
    private final double Q;
    private final Random rnd;

    private final double[][] tau;
    private final double[][] eta;
    private final boolean verbose;
    private final int logInterval;

    public ACO_TSP(double[][] distanceMatrix, int antCount, int iterations,
                   double alpha, double beta, double rho, double Q, long seed) {
        this(distanceMatrix, antCount, iterations, alpha, beta, rho, Q, seed, true, 10);
    }

    public ACO_TSP(double[][] distanceMatrix, int antCount, int iterations,
                   double alpha, double beta, double rho, double Q, long seed,
                   boolean verbose, int logInterval) {
        this.n = distanceMatrix.length;
        this.dist = distanceMatrix;
        this.antCount = antCount;
        this.iterations = iterations;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.Q = Q;
        this.rnd = new Random(seed);
        this.verbose = verbose;
        this.logInterval = logInterval;
        this.tau = new double[n][n];
        this.eta = new double[n][n];

        double initTau = 1.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tau[i][j] = (i == j) ? 0.0 : initTau;
                eta[i][j] = (i == j || dist[i][j] == 0.0) ? 0.0 : (1.0 / dist[i][j]);
            }
        }
    }

    public Result solve() {
        if (verbose) {
            System.out.println("=== Ant Colony Optimization (ACO) for TSP ===");
            System.out.println("Parameters:");
            System.out.println("  Number of cities: " + n);
            System.out.println("  Ant count: " + antCount);
            System.out.println("  Iterations: " + iterations);
            System.out.println("  Alpha (pheromone importance): " + alpha);
            System.out.println("  Beta (heuristic importance): " + beta);
            System.out.println("  Rho (evaporation rate): " + rho);
            System.out.println("  Q (pheromone deposit constant): " + Q);
            System.out.println("----------------------------------------");
        }

        int[] globalBestTour = null;
        double globalBestLen = Double.POSITIVE_INFINITY;
        List<IterationLog> history = new ArrayList<>();

        if (verbose) {
            System.out.println("\nStarting optimization...\n");
            System.out.println(String.format("%-10s %-15s %-15s %-15s %-15s %-15s",
                "Iteration", "Best Length", "Avg Length", "Min Length", "Max Length", "Avg Pheromone"));
            System.out.println("--------------------------------------------------------------------------------");
        }

        for (int iter = 0; iter < iterations; iter++) {
            List<int[]> tours = new ArrayList<>(antCount);
            List<Double> lens = new ArrayList<>(antCount);

            // Each ant builds a tour
            for (int k = 0; k < antCount; k++) {
                int start = rnd.nextInt(n);
                int[] tour = buildTour(start);
                double len = tourLength(tour);
                tours.add(tour);
                lens.add(len);

                // Update global best
                if (len < globalBestLen) {
                    globalBestLen = len;
                    globalBestTour = tour.clone();
                }
            }

            // Evaporate pheromone
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tau[i][j] *= (1.0 - rho);
                }
            }

            // Deposit pheromone on best tour of this iteration
            int bestIdx = argMin(lens);
            int[] bestTour = tours.get(bestIdx);
            double bestLen = lens.get(bestIdx);
            double deposit = Q / bestLen;
            for (int i = 0; i < n; i++) {
                int a = bestTour[i];
                int b = bestTour[(i + 1) % n];
                tau[a][b] += deposit;
                tau[b][a] += deposit;
            }

            // Calculate statistics
            double avgLen = lens.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double minLen = Collections.min(lens);
            double maxLen = Collections.max(lens);
            double avgPheromone = calculateAvgPheromone();

            // Log iteration
            IterationLog log = new IterationLog(iter + 1, globalBestLen, avgLen, minLen, maxLen, avgPheromone);
            history.add(log);

            // Print progress
            if (verbose && ((iter + 1) % logInterval == 0 || iter == 0 || iter == iterations - 1)) {
                System.out.println(String.format("%-10d %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f",
                    iter + 1, globalBestLen, avgLen, minLen, maxLen, avgPheromone));
            }
        }

        if (verbose) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("\nOptimization Complete!");
            System.out.println("Final Best Tour Length: " + globalBestLen);
            System.out.println("Final Best Tour: " + Arrays.toString(globalBestTour));
        }

        return new Result(globalBestTour, globalBestLen, history);
    }

    private double calculateAvgPheromone() {
        double sum = 0.0;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    sum += tau[i][j];
                    count++;
                }
            }
        }
        return count > 0 ? sum / count : 0.0;
    }

    private int[] buildTour(int start) {
        boolean[] visited = new boolean[n];
        int[] tour = new int[n];
        tour[0] = start;
        visited[start] = true;

        for (int step = 1; step < n; step++) {
            int i = tour[step - 1];
            int j = selectNextCity(i, visited);
            tour[step] = j;
            visited[j] = true;
        }
        return tour;
    }

    private int selectNextCity(int current, boolean[] visited) {
        double[] probs = new double[n];
        double sum = 0.0;
        for (int j = 0; j < n; j++) {
            if (!visited[j]) {
                double val = Math.pow(tau[current][j], alpha) * Math.pow(eta[current][j], beta);
                probs[j] = val;
                sum += val;
            } else {
                probs[j] = 0.0;
            }
        }
        if (sum == 0.0) {
            List<Integer> choices = new ArrayList<>();
            for (int j = 0; j < n; j++) if (!visited[j]) choices.add(j);
            return choices.get(rnd.nextInt(choices.size()));
        }
        double r = rnd.nextDouble() * sum;
        double c = 0.0;
        for (int j = 0; j < n; j++) {
            c += probs[j];
            if (r <= c) return j;
        }
        for (int j = n - 1; j >= 0; j--) if (probs[j] > 0) return j;
        throw new IllegalStateException("No next city found.");
    }

    private double tourLength(int[] tour) {
        double len = 0.0;
        for (int i = 0; i < n; i++) {
            int a = tour[i];
            int b = tour[(i + 1) % n];
            len += dist[a][b];
        }
        return len;
    }

    private int argMin(List<Double> vals) {
        int idx = 0;
        for (int i = 1; i < vals.size(); i++) if (vals.get(i) < vals.get(idx)) idx = i;
        return idx;
    }

    public void exportToCSV(Result result, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("iteration,best_length,avg_length,min_length,max_length,avg_pheromone");

            // Write data
            for (IterationLog log : result.history) {
                writer.println(String.format("%d,%.6f,%.6f,%.6f,%.6f,%.6f",
                    log.iteration, log.bestLength, log.avgLength,
                    log.minLength, log.maxLength, log.avgPheromone));
            }
        }
    }

    public static void main(String[] args) {
        // ========================================================================
        // CONFIGURATION - TSP Problem Setup
        // ========================================================================
        
        // Example: 4-city TSP problem
        // Distance matrix (symmetric)
        double[][] distanceMatrix = {
            {0, 2, 9, 10},
            {2, 0, 6, 4},
            {9, 6, 0, 8},
            {10, 4, 8, 0}
        };
        
        // ACO Parameters
        int antCount = 20;
        int iterations = 200;
        double alpha = 1.0;   // Pheromone importance
        double beta = 3.0;     // Heuristic importance
        double rho = 0.5;      // Evaporation rate
        double Q = 100.0;      // Pheromone deposit constant
        long seed = 7L;
        
        // Problem name for CSV filename
        String problemName = "tsp_4city";

        // ========================================================================
        // RUN OPTIMIZATION
        // ========================================================================
        ACO_TSP aco = new ACO_TSP(distanceMatrix, antCount, iterations,
                                  alpha, beta, rho, Q, seed);
        Result r = aco.solve();
        
        // Export to CSV
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "aco_log_" + problemName + "_ants" + antCount + "_iter" + iterations + "_" + timestamp + ".csv";
            aco.exportToCSV(r, filename);
            System.out.println("\nLogbook saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}



