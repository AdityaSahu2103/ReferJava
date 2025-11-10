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

public class PSO {
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
        public final double gbestFitness;
        public final double[] gbestPosition;
        public final double avgFitness;
        public final double minFitness;
        public final double maxFitness;
        
        public IterationLog(int iteration, double gbestFitness, double[] gbestPosition,
                           double avgFitness, double minFitness, double maxFitness) {
            this.iteration = iteration;
            this.gbestFitness = gbestFitness;
            this.gbestPosition = Arrays.copyOf(gbestPosition, gbestPosition.length);
            this.avgFitness = avgFitness;
            this.minFitness = minFitness;
            this.maxFitness = maxFitness;
        }
    }

    private final int swarmSize;
    private final int dimensions;
    private final int iterations;
    private final double inertiaW;
    private final double c1;
    private final double c2;
    private final double[] lower;
    private final double[] upper;
    private final FitnessFunction fitness;
    private final Random rnd;
    private final boolean verbose;
    private final int logInterval;

    public PSO(int swarmSize, int dimensions, int iterations,
               double inertiaW, double c1, double c2,
               double[] lower, double[] upper,
               FitnessFunction fitness, long seed) {
        this(swarmSize, dimensions, iterations, inertiaW, c1, c2, lower, upper, fitness, seed, true, 10);
    }

    public PSO(int swarmSize, int dimensions, int iterations,
               double inertiaW, double c1, double c2,
               double[] lower, double[] upper,
               FitnessFunction fitness, long seed,
               boolean verbose, int logInterval) {
        this.swarmSize = swarmSize;
        this.dimensions = dimensions;
        this.iterations = iterations;
        this.inertiaW = inertiaW;
        this.c1 = c1;
        this.c2 = c2;
        this.lower = Arrays.copyOf(lower, lower.length);
        this.upper = Arrays.copyOf(upper, upper.length);
        this.fitness = fitness;
        this.rnd = new Random(seed);
        this.verbose = verbose;
        this.logInterval = logInterval;
    }

    public Result optimize() {
        if (verbose) {
            System.out.println("=== Particle Swarm Optimization (PSO) ===");
            System.out.println("Parameters:");
            System.out.println("  Swarm Size: " + swarmSize);
            System.out.println("  Dimensions: " + dimensions);
            System.out.println("  Iterations: " + iterations);
            System.out.println("  Inertia Weight (w): " + inertiaW);
            System.out.println("  Cognitive Coefficient (c1): " + c1);
            System.out.println("  Social Coefficient (c2): " + c2);
            System.out.println("  Bounds: [" + lower[0] + ", " + upper[0] + "]");
            System.out.println("----------------------------------------");
        }

        double[][] x = new double[swarmSize][dimensions];
        double[][] v = new double[swarmSize][dimensions];
        double[][] pbest = new double[swarmSize][dimensions];
        double[] pbestFit = new double[swarmSize];
        double[] currentFit = new double[swarmSize];

        // Initialize particles
        for (int i = 0; i < swarmSize; i++) {
            for (int d = 0; d < dimensions; d++) {
                x[i][d] = lower[d] + rnd.nextDouble() * (upper[d] - lower[d]);
                v[i][d] = 0.1 * (lower[d] + rnd.nextDouble() * (upper[d] - lower[d]));
                pbest[i][d] = x[i][d];
            }
            pbestFit[i] = fitness.evaluate(x[i]);
            currentFit[i] = pbestFit[i];
        }

        double[] gbest = Arrays.copyOf(pbest[argMin(pbestFit)], dimensions);
        double gbestFit = fitness.evaluate(gbest);

        List<IterationLog> history = new ArrayList<>();

        if (verbose) {
            System.out.println("\nStarting optimization...\n");
            System.out.println(String.format("%-10s %-15s %-30s %-15s %-15s %-15s",
                "Iteration", "gBest Fitness", "gBest Position", "Avg Fitness", "Min Fitness", "Max Fitness"));
            System.out.println("--------------------------------------------------------------------------------");
        }

        // Main optimization loop
        for (int t = 0; t < iterations; t++) {
            for (int i = 0; i < swarmSize; i++) {
                for (int d = 0; d < dimensions; d++) {
                    // Generate random coefficients r1, r2 for each dimension
                    double r1 = rnd.nextDouble();
                    double r2 = rnd.nextDouble();
                    
                    // Velocity update: v = w*v + c1*r1*(pBest - x) + c2*r2*(gBest - x)
                    v[i][d] = inertiaW * v[i][d]
                            + c1 * r1 * (pbest[i][d] - x[i][d])
                            + c2 * r2 * (gbest[d] - x[i][d]);
                    
                    // Position update: x = x + v
                    x[i][d] += v[i][d];
                    
                    // Boundary constraints
                    if (x[i][d] < lower[d]) { 
                        x[i][d] = lower[d]; 
                        v[i][d] = 0; 
                    }
                    if (x[i][d] > upper[d]) { 
                        x[i][d] = upper[d]; 
                        v[i][d] = 0; 
                    }
                }
                
                // Evaluate fitness
                double fit = fitness.evaluate(x[i]);
                currentFit[i] = fit;
                
                // Update personal best
                if (fit < pbestFit[i]) {
                    pbestFit[i] = fit;
                    System.arraycopy(x[i], 0, pbest[i], 0, dimensions);
                    
                    // Update global best
                    if (fit < gbestFit) {
                        gbestFit = fit;
                        System.arraycopy(x[i], 0, gbest, 0, dimensions);
                    }
                }
            }

            // Calculate statistics
            double avgFit = Arrays.stream(currentFit).average().orElse(0.0);
            double minFit = Arrays.stream(currentFit).min().orElse(0.0);
            double maxFit = Arrays.stream(currentFit).max().orElse(0.0);

            // Log iteration
            IterationLog log = new IterationLog(t + 1, gbestFit, gbest, avgFit, minFit, maxFit);
            history.add(log);

            // Print progress
            if (verbose && ((t + 1) % logInterval == 0 || t == 0 || t == iterations - 1)) {
                String posStr = Arrays.toString(gbest);
                if (posStr.length() > 28) posStr = posStr.substring(0, 25) + "...";
                System.out.println(String.format("%-10d %-15.6f %-30s %-15.6f %-15.6f %-15.6f",
                    t + 1, gbestFit, posStr, avgFit, minFit, maxFit));
            }
        }

        if (verbose) {
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("\nOptimization Complete!");
            System.out.println("Final gBest Fitness: " + gbestFit);
            System.out.println("Final gBest Position: " + Arrays.toString(gbest));
        }

        return new Result(gbest, gbestFit, history);
    }

    private int argMin(double[] arr) {
        int idx = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[idx]) idx = i;
        }
        return idx;
    }

    public void exportToCSV(Result result, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.print("iteration,gbest_fitness");
            for (int d = 0; d < dimensions; d++) {
                writer.print(",gbest_dim" + d);
            }
            writer.println(",avg_fitness,min_fitness,max_fitness");

            // Write data
            for (IterationLog log : result.history) {
                writer.print(log.iteration + "," + log.gbestFitness);
                for (int d = 0; d < dimensions; d++) {
                    writer.print("," + log.gbestPosition[d]);
                }
                writer.println("," + log.avgFitness + "," + log.minFitness + "," + log.maxFitness);
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
            double A = 10;
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
    
    public static FitnessFunction griewank() {
        // Griewank function: f(x) = 1 + sum(x_i^2/4000) - product(cos(x_i/sqrt(i)))
        // Global minimum: f(0,0,...,0) = 0
        // Bounds: [-600, 600]
        return (x) -> {
            double sum = 0;
            double product = 1;
            for (int i = 0; i < x.length; i++) {
                sum += x[i] * x[i] / 4000.0;
                product *= Math.cos(x[i] / Math.sqrt(i + 1));
            }
            return 1 + sum - product;
        };
    }

    public static void main(String[] args) {
        // ========================================================================
        // CONFIGURATION - Change these settings to test different scenarios
        // ========================================================================
        
        int dim = 2;  // Number of dimensions
        
        // Choose your objective function here:
        // Options: sphere(), rastrigin(), rosenbrock(), ackley(), griewank()
        FitnessFunction objective = sphere();  // <-- CHANGE THIS LINE
        
        // Set bounds according to the function you choose:
        // Sphere/Rastrigin: [-5.12, 5.12]
        // Rosenbrock: [-2.0, 2.0]
        // Ackley: [-5.0, 5.0]
        // Griewank: [-600, 600]
        double[] lo = new double[dim];
        double[] hi = new double[dim];
        Arrays.fill(lo, -5.12);  // <-- CHANGE LOWER BOUND
        Arrays.fill(hi, 5.12);   // <-- CHANGE UPPER BOUND
        
        // PSO Parameters
        int swarmSize = 30;
        int iterations = 200;
        double inertiaW = 0.72;
        double c1 = 1.49;
        double c2 = 1.49;
        long seed = 42L;
        
        // Function name for CSV filename
        String functionName = "sphere";  // <-- CHANGE THIS (sphere, rastrigin, rosenbrock, ackley, griewank)

        // ========================================================================
        // RUN OPTIMIZATION
        // ========================================================================
        PSO pso = new PSO(swarmSize, dim, iterations, inertiaW, c1, c2, lo, hi, objective, seed);
        Result r = pso.optimize();
        
        // Export to CSV
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "pso_log_" + functionName + "_N" + swarmSize + "_T" + iterations + "_" + timestamp + ".csv";
            pso.exportToCSV(r, filename);
            System.out.println("\nLogbook saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}



