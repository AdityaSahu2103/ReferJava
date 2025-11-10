package optimize;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GWO {
    public interface FitnessFunction {
        double evaluate(double[] x);
    }

    public static class Result {
        public final double[] bestPosition;
        public final double bestFitness;
        public final List<IterationLog> history;
        
        public Result(double[] bestPosition, double bestFitness, List<IterationLog> history) {
            this.bestPosition = bestPosition;
            this.bestFitness = bestFitness;
            this.history = history;
        }
    }

    public static class IterationLog {
        public final int iteration;
        public final double alphaFitness;
        public final double[] alphaPosition;
        public final double avgFitness;
        public final double minFitness;
        public final double maxFitness;
        public final double aValue;  // Convergence parameter
        
        public IterationLog(int iteration, double alphaFitness, double[] alphaPosition,
                           double avgFitness, double minFitness, double maxFitness, double aValue) {
            this.iteration = iteration;
            this.alphaFitness = alphaFitness;
            this.alphaPosition = Arrays.copyOf(alphaPosition, alphaPosition.length);
            this.avgFitness = avgFitness;
            this.minFitness = minFitness;
            this.maxFitness = maxFitness;
            this.aValue = aValue;
        }
    }

    private final int wolfCount;
    private final int dimensions;
    private final int iterations;
    private final double[] lower;
    private final double[] upper;
    private final FitnessFunction fitness;
    private final Random rnd;
    private final boolean verbose;
    private final int logInterval;

    public GWO(int wolfCount, int dimensions, int iterations,
               double[] lower, double[] upper,
               FitnessFunction fitness, long seed) {
        this(wolfCount, dimensions, iterations, lower, upper, fitness, seed, true, 10);
    }

    public GWO(int wolfCount, int dimensions, int iterations,
               double[] lower, double[] upper,
               FitnessFunction fitness, long seed,
               boolean verbose, int logInterval) {
        this.wolfCount = wolfCount;
        this.dimensions = dimensions;
        this.iterations = iterations;
        this.lower = Arrays.copyOf(lower, lower.length);
        this.upper = Arrays.copyOf(upper, upper.length);
        this.fitness = fitness;
        this.rnd = new Random(seed);
        this.verbose = verbose;
        this.logInterval = logInterval;
    }

    public Result optimize() {
        if (verbose) {
            System.out.println("=== Grey Wolf Optimizer (GWO) ===");
            System.out.println("Parameters:");
            System.out.println("  Wolf Count: " + wolfCount);
            System.out.println("  Dimensions: " + dimensions);
            System.out.println("  Iterations: " + iterations);
            System.out.println("  Bounds: [" + lower[0] + ", " + upper[0] + "]");
            System.out.println("----------------------------------------");
        }

        double[][] X = new double[wolfCount][dimensions];
        double[] currentFit = new double[wolfCount];
        
        // Initialize wolves randomly
        for (int i = 0; i < wolfCount; i++) {
            for (int d = 0; d < dimensions; d++) {
                X[i][d] = lower[d] + rnd.nextDouble() * (upper[d] - lower[d]);
            }
            currentFit[i] = fitness.evaluate(X[i]);
        }

        double[] Xalpha = null, Xbeta = null, Xdelta = null;
        double falpha = Double.POSITIVE_INFINITY;
        double fbeta = Double.POSITIVE_INFINITY;
        double fdelta = Double.POSITIVE_INFINITY;

        // Initialize alpha, beta, delta (top 3 wolves)
        for (int i = 0; i < wolfCount; i++) {
            double f = currentFit[i];
            if (f < falpha) {
                fdelta = fbeta;
                Xdelta = Xbeta;
                fbeta = falpha;
                Xbeta = Xalpha;
                falpha = f;
                Xalpha = Arrays.copyOf(X[i], dimensions);
            } else if (f < fbeta) {
                fdelta = fbeta;
                Xdelta = Xbeta;
                fbeta = f;
                Xbeta = Arrays.copyOf(X[i], dimensions);
            } else if (f < fdelta) {
                fdelta = f;
                Xdelta = Arrays.copyOf(X[i], dimensions);
            }
        }

        List<IterationLog> history = new ArrayList<>();

        if (verbose) {
            System.out.println("\nStarting optimization...\n");
            System.out.println(String.format("%-10s %-15s %-30s %-15s %-15s %-15s %-10s",
                "Iteration", "Alpha Fitness", "Alpha Position", "Avg Fitness", "Min Fitness", "Max Fitness", "a value"));
            System.out.println("--------------------------------------------------------------------------------");
        }

        for (int t = 0; t < iterations; t++) {
            // Convergence parameter: decreases linearly from 2 to 0
            double a = 2.0 - 2.0 * (t / (double) iterations);
            
            for (int i = 0; i < wolfCount; i++) {
                for (int d = 0; d < dimensions; d++) {
                    // Generate random coefficients for alpha
                    double r1 = rnd.nextDouble();
                    double r2 = rnd.nextDouble();
                    double A1 = 2 * a * r1 - a;
                    double C1 = 2 * r2;

                    // Generate random coefficients for beta
                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    double A2 = 2 * a * r1 - a;
                    double C2 = 2 * r2;

                    // Generate random coefficients for delta
                    r1 = rnd.nextDouble();
                    r2 = rnd.nextDouble();
                    double A3 = 2 * a * r1 - a;
                    double C3 = 2 * r2;

                    // Calculate distances
                    double Dalpha = Math.abs(C1 * Xalpha[d] - X[i][d]);
                    double Dbeta  = Math.abs(C2 * Xbeta[d]  - X[i][d]);
                    double Ddelta = Math.abs(C3 * Xdelta[d] - X[i][d]);

                    // Calculate new positions based on alpha, beta, delta
                    double X1 = Xalpha[d] - A1 * Dalpha;
                    double X2 = Xbeta[d]  - A2 * Dbeta;
                    double X3 = Xdelta[d] - A3 * Ddelta;

                    // Update position (average of three leaders)
                    double newPos = (X1 + X2 + X3) / 3.0;
                    
                    // Apply boundary constraints
                    if (newPos < lower[d]) newPos = lower[d];
                    if (newPos > upper[d]) newPos = upper[d];
                    X[i][d] = newPos;
                }
                
                // Evaluate fitness
                double f = fitness.evaluate(X[i]);
                currentFit[i] = f;
                
                // Update alpha, beta, delta
                if (f < falpha) {
                    fdelta = fbeta;
                    Xdelta = Xbeta;
                    fbeta = falpha;
                    Xbeta = Xalpha;
                    falpha = f;
                    Xalpha = Arrays.copyOf(X[i], dimensions);
                } else if (f < fbeta) {
                    fdelta = fbeta;
                    Xdelta = Xbeta;
                    fbeta = f;
                    Xbeta = Arrays.copyOf(X[i], dimensions);
                } else if (f < fdelta) {
                    fdelta = f;
                    Xdelta = Arrays.copyOf(X[i], dimensions);
                }
            }

            // Calculate statistics
            double avgFit = Arrays.stream(currentFit).average().orElse(0.0);
            double minFit = Arrays.stream(currentFit).min().orElse(0.0);
            double maxFit = Arrays.stream(currentFit).max().orElse(0.0);

            // Log iteration
            IterationLog log = new IterationLog(t + 1, falpha, Xalpha, avgFit, minFit, maxFit, a);
            history.add(log);

            // Print progress
            if (verbose && ((t + 1) % logInterval == 0 || t == 0 || t == iterations - 1)) {
                String posStr = Arrays.toString(Xalpha);
                if (posStr.length() > 28) posStr = posStr.substring(0, 25) + "...";
                System.out.println(String.format("%-10d %-15.6f %-30s %-15.6f %-15.6f %-15.6f %-10.4f",
                    t + 1, falpha, posStr, avgFit, minFit, maxFit, a));
            }
        }

        if (verbose) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("\nOptimization Complete!");
            System.out.println("Final Alpha Fitness: " + falpha);
            System.out.println("Final Alpha Position: " + Arrays.toString(Xalpha));
        }

        return new Result(Xalpha, falpha, history);
    }

    public void exportToCSV(Result result, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.print("iteration,alpha_fitness");
            for (int d = 0; d < dimensions; d++) {
                writer.print(",alpha_dim" + d);
            }
            writer.println(",avg_fitness,min_fitness,max_fitness,a_value");

            // Write data
            for (IterationLog log : result.history) {
                writer.print(log.iteration + "," + log.alphaFitness);
                for (int d = 0; d < dimensions; d++) {
                    writer.print("," + log.alphaPosition[d]);
                }
                writer.println("," + log.avgFitness + "," + log.minFitness + "," + log.maxFitness + "," + log.aValue);
            }
        }
    }

    // ============================================================================
    // BENCHMARK FUNCTIONS - Change the function here to test different objectives
    // ============================================================================
    
    public static FitnessFunction sphere() {
        // Sphere function: f(x) = sum(x_i^2)
        // Global minimum: f(0,0,...,0) = 0
        // Bounds: [-5.12, 5.12]
        return (x) -> {
            double sum = 0;
            for (double v : x) sum += v * v;
            return sum;
        };
    }
    
    public static FitnessFunction rastrigin() {
        // Rastrigin function: f(x) = 10*n + sum(x_i^2 - 10*cos(2*pi*x_i))
        // Global minimum: f(0,0,...,0) = 0
        // Bounds: [-5.12, 5.12]
        return (x) -> {
            double A = 10.0;
            double sum = A * x.length;
            for (double v : x) {
                sum += v * v - A * Math.cos(2 * Math.PI * v);
            }
            return sum;
        };
    }
    
    public static FitnessFunction rosenbrock() {
        // Rosenbrock function: f(x) = sum(100*(x_{i+1} - x_i^2)^2 + (1 - x_i)^2)
        // Global minimum: f(1,1,...,1) = 0
        // Bounds: [-2.0, 2.0]
        return (x) -> {
            double sum = 0;
            for (int i = 0; i < x.length - 1; i++) {
                sum += 100 * Math.pow(x[i + 1] - x[i] * x[i], 2) + Math.pow(1 - x[i], 2);
            }
            return sum;
        };
    }
    
    public static FitnessFunction ackley() {
        // Ackley function: f(x) = -20*exp(-0.2*sqrt(mean(x^2))) - exp(mean(cos(2*pi*x))) + 20 + e
        // Global minimum: f(0,0,...,0) = 0
        // Bounds: [-5.0, 5.0]
        return (x) -> {
            double a = 20;
            double b = 0.2;
            double c = 2 * Math.PI;
            
            double sumSq = 0;
            double sumCos = 0;
            for (double v : x) {
                sumSq += v * v;
                sumCos += Math.cos(c * v);
            }
            
            double n = x.length;
            double term1 = -a * Math.exp(-b * Math.sqrt(sumSq / n));
            double term2 = -Math.exp(sumCos / n);
            
            return term1 + term2 + a + Math.E;
        };
    }

    public static void main(String[] args) {
        // ========================================================================
        // CONFIGURATION - Change these settings to test different scenarios
        // ========================================================================
        
        int dim = 2;  // Number of dimensions
        
        // Choose your objective function here:
        // Options: sphere(), rastrigin(), rosenbrock(), ackley()
        FitnessFunction objective = rastrigin();  // <-- CHANGE THIS LINE
        
        // Set bounds according to the function you choose:
        // Sphere/Rastrigin: [-5.12, 5.12]
        // Rosenbrock: [-2.0, 2.0]
        // Ackley: [-5.0, 5.0]
        double[] lo = new double[dim];
        double[] hi = new double[dim];
        Arrays.fill(lo, -5.12);  // <-- CHANGE LOWER BOUND
        Arrays.fill(hi, 5.12);   // <-- CHANGE UPPER BOUND
        
        // GWO Parameters
        int wolfCount = 25;
        int iterations = 300;
        long seed = 123L;
        
        // Function name for CSV filename
        String functionName = "rastrigin";  // <-- CHANGE THIS (sphere, rastrigin, rosenbrock, ackley)

        // ========================================================================
        // RUN OPTIMIZATION
        // ========================================================================
        GWO gwo = new GWO(wolfCount, dim, iterations, lo, hi, objective, seed);
        Result r = gwo.optimize();
        
        // Export to CSV
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "gwo_log_" + functionName + "_N" + wolfCount + "_T" + iterations + "_" + timestamp + ".csv";
            gwo.exportToCSV(r, filename);
            System.out.println("\nLogbook saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}



