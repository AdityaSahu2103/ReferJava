import java.util.Scanner;

/**
 * Assignment 3: Parameter Effects on Membership Functions
 * Demonstrates how changing parameters affects membership function shape
 */
public class Assignment3 {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Assignment 3: Parameter Effects");
        System.out.println("==========================================\n");
        
        Scanner scanner = new Scanner(System.in);
        
        // Demonstrate Triangular MF parameter changes
        System.out.println("--- Triangular Membership Function ---");
        System.out.println("Original: Triangular(10, 20, 30)");
        System.out.println("Modified: Triangular(10, 25, 30) - Peak shifted right");
        System.out.println("\nComparison at different x values:\n");
        
        TriangularMF original = new TriangularMF(10, 20, 30);
        TriangularMF modified = new TriangularMF(10, 25, 30);
        
        System.out.println("x      Original  Modified  Change");
        System.out.println("------------------------------------");
        for (int i = 10; i <= 30; i += 2) {
            double orig = original.getMembership(i);
            double mod = modified.getMembership(i);
            double change = mod - orig;
            System.out.printf("%2d      %.4f    %.4f    %+.4f\n", i, orig, mod, change);
        }
        
        // Demonstrate Gaussian MF parameter changes
        System.out.println("\n--- Gaussian Membership Function ---");
        System.out.println("Original: Gaussian(mean=20, σ=5)");
        System.out.println("Modified: Gaussian(mean=20, σ=3) - Narrower spread");
        System.out.println("\nComparison at different x values:\n");
        
        GaussianMF gaussOriginal = new GaussianMF(20, 5, 10, 30);
        GaussianMF gaussModified = new GaussianMF(20, 3, 10, 30);
        
        System.out.println("x      Original  Modified  Change");
        System.out.println("------------------------------------");
        for (int i = 10; i <= 30; i += 2) {
            double orig = gaussOriginal.getMembership(i);
            double mod = gaussModified.getMembership(i);
            double change = mod - orig;
            System.out.printf("%2d      %.4f    %.4f    %+.4f\n", i, orig, mod, change);
        }
        
        // Demonstrate Trapezoidal MF parameter changes
        System.out.println("\n--- Trapezoidal Membership Function ---");
        System.out.println("Original: Trapezoidal(15, 20, 25, 30)");
        System.out.println("Modified: Trapezoidal(15, 18, 27, 30) - Wider plateau");
        System.out.println("\nComparison at different x values:\n");
        
        TrapezoidalMF trapOriginal = new TrapezoidalMF(15, 20, 25, 30);
        TrapezoidalMF trapModified = new TrapezoidalMF(15, 18, 27, 30);
        
        System.out.println("x      Original  Modified  Change");
        System.out.println("------------------------------------");
        for (int i = 10; i <= 30; i += 2) {
            double orig = trapOriginal.getMembership(i);
            double mod = trapModified.getMembership(i);
            double change = mod - orig;
            System.out.printf("%2d      %.4f    %.4f    %+.4f\n", i, orig, mod, change);
        }
        
        // Demonstrate Bell-shaped MF parameter changes
        System.out.println("\n--- Bell-Shaped Membership Function ---");
        System.out.println("Original: Bell-Shaped(a=2, b=3, c=20)");
        System.out.println("Modified: Bell-Shaped(a=3, b=3, c=20) - Wider shape");
        System.out.println("\nComparison at different x values:\n");
        
        BellShapedMF bellOriginal = new BellShapedMF(2, 3, 20);
        BellShapedMF bellModified = new BellShapedMF(3, 3, 20);
        
        System.out.println("x      Original  Modified  Change");
        System.out.println("------------------------------------");
        for (int i = 10; i <= 30; i += 2) {
            double orig = bellOriginal.getMembership(i);
            double mod = bellModified.getMembership(i);
            double change = mod - orig;
            System.out.printf("%2d      %.4f    %.4f    %+.4f\n", i, orig, mod, change);
        }
        
        // Demonstrate Sigmoidal MF parameter changes
        System.out.println("\n--- Sigmoidal Membership Function ---");
        System.out.println("Original: Sigmoidal(a=0.5, c=20)");
        System.out.println("Modified: Sigmoidal(a=1.0, c=20) - Steeper slope");
        System.out.println("\nComparison at different x values:\n");
        
        SigmoidalMF sigOriginal = new SigmoidalMF(0.5, 20);
        SigmoidalMF sigModified = new SigmoidalMF(1.0, 20);
        
        System.out.println("x      Original  Modified  Change");
        System.out.println("------------------------------------");
        for (int i = 10; i <= 30; i += 2) {
            double orig = sigOriginal.getMembership(i);
            double mod = sigModified.getMembership(i);
            double change = mod - orig;
            System.out.printf("%2d      %.4f    %.4f    %+.4f\n", i, orig, mod, change);
        }
        
        // Interactive parameter exploration
        System.out.println("\n--- Interactive Parameter Exploration ---");
        System.out.print("Enter a value to test (10-30): ");
        double testValue = scanner.nextDouble();
        
        if (testValue >= 10 && testValue <= 30) {
            System.out.println("\nMembership values at x = " + testValue + ":");
            System.out.printf("Triangular(10,20,30):     %.4f\n", original.getMembership(testValue));
            System.out.printf("Triangular(10,25,30):     %.4f\n", modified.getMembership(testValue));
            System.out.printf("Gaussian(20,5):           %.4f\n", gaussOriginal.getMembership(testValue));
            System.out.printf("Gaussian(20,3):           %.4f\n", gaussModified.getMembership(testValue));
            System.out.printf("Trapezoidal(15,20,25,30): %.4f\n", trapOriginal.getMembership(testValue));
            System.out.printf("Trapezoidal(15,18,27,30): %.4f\n", trapModified.getMembership(testValue));
            System.out.printf("Bell-Shaped(2,3,20):     %.4f\n", bellOriginal.getMembership(testValue));
            System.out.printf("Bell-Shaped(3,3,20):    %.4f\n", bellModified.getMembership(testValue));
            System.out.printf("Sigmoidal(0.5,20):      %.4f\n", sigOriginal.getMembership(testValue));
            System.out.printf("Sigmoidal(1.0,20):      %.4f\n", sigModified.getMembership(testValue));
        }
        
        scanner.close();
    }
}

// Triangular Membership Function
class TriangularMF {
    private double a, b, c;
    
    public TriangularMF(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public double getMembership(double x) {
        if (x <= a) return 0;
        if (x > a && x <= b) return (x - a) / (b - a);
        if (x > b && x <= c) return (c - x) / (c - b);
        return 0;
    }
}

// Trapezoidal Membership Function
class TrapezoidalMF {
    private double a, b, c, d;
    
    public TrapezoidalMF(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    public double getMembership(double x) {
        if (x <= a) return 0;
        if (x > a && x <= b) return (x - a) / (b - a);
        if (x > b && x <= c) return 1;
        if (x > c && x <= d) return (d - x) / (d - c);
        return 0;
    }
}

// Gaussian Membership Function
class GaussianMF {
    private double mean, stdDev;
    private double lower, upper;
    
    public GaussianMF(double mean, double stdDev, double lower, double upper) {
        this.mean = mean;
        this.stdDev = stdDev;
        this.lower = lower;
        this.upper = upper;
    }
    
    public double getMembership(double x) {
        if (x < lower || x > upper) return 0;
        return Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2));
    }
}

// Bell-Shaped Membership Function
class BellShapedMF {
    private double a, b, c;
    
    public BellShapedMF(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public double getMembership(double x) {
        return 1.0 / (1.0 + Math.pow(Math.abs((x - c) / a), 2 * b));
    }
}

// Sigmoidal Membership Function
class SigmoidalMF {
    private double a, c;
    
    public SigmoidalMF(double a, double c) {
        this.a = a;
        this.c = c;
    }
    
    public double getMembership(double x) {
        return 1.0 / (1.0 + Math.exp(-a * (x - c)));
    }
}

