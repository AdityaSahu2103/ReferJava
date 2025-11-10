import java.util.Scanner;

/**
 * Assignment 2: Fuzzy Relations
 * Demonstrates fuzzy rules, inference, and defuzzification
 * Uses different types of membership functions (Triangular, Trapezoidal, Gaussian)
 * Uses temperature and humidity to determine comfort level
 */
public class Assignment2 {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Assignment 2: Fuzzy Relations");
        System.out.println("==========================================\n");
        
        System.out.println("Demonstrating fuzzy relations with different membership function types:\n");
        
        // Create membership functions for temperature using different types
        System.out.println("--- Temperature Membership Functions ---");
        TriangularMF coldTemp = new TriangularMF(10, 10, 25);
        TrapezoidalMF comfortableTemp = new TrapezoidalMF(15, 20, 25, 30);
        GaussianMF hotTemp = new GaussianMF(35, 5, 25, 40);
        
        System.out.println("Cold: Triangular(10, 10, 25)");
        System.out.println("Comfortable: Trapezoidal(15, 20, 25, 30)");
        System.out.println("Hot: Gaussian(mean=35, σ=5)");
        
        // Create membership functions for humidity using different types
        System.out.println("\n--- Humidity Membership Functions ---");
        TriangularMF dryHumidity = new TriangularMF(0, 0, 50);
        TrapezoidalMF comfortableHumidity = new TrapezoidalMF(30, 40, 60, 70);
        GaussianMF humidHumidity = new GaussianMF(75, 10, 50, 100);
        
        System.out.println("Dry: Triangular(0, 0, 50)");
        System.out.println("Comfortable: Trapezoidal(30, 40, 60, 70)");
        System.out.println("Humid: Gaussian(mean=75, σ=10)");
        
        // Create membership functions for output (comfort level) using different types
        System.out.println("\n--- Output (Comfort Level) Membership Functions ---");
        TriangularMF uncomfortable = new TriangularMF(0, 0, 50);
        TrapezoidalMF neutral = new TrapezoidalMF(30, 40, 60, 70);
        GaussianMF comfortable = new GaussianMF(75, 10, 50, 100);
        
        System.out.println("Uncomfortable: Triangular(0, 0, 50)");
        System.out.println("Neutral: Trapezoidal(30, 40, 60, 70)");
        System.out.println("Comfortable: Gaussian(mean=75, σ=10)");
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter temperature (10-40°C): ");
        double temperature = scanner.nextDouble();
        
        System.out.print("Enter humidity (0-100%): ");
        double humidity = scanner.nextDouble();
        
        if (temperature < 10 || temperature > 40 || humidity < 0 || humidity > 100) {
            System.out.println("Invalid input values");
            scanner.close();
            return;
        }
        
        // Calculate membership values for inputs
        double coldMem = coldTemp.getMembership(temperature);
        double comfTempMem = comfortableTemp.getMembership(temperature);
        double hotMem = hotTemp.getMembership(temperature);
        
        double dryMem = dryHumidity.getMembership(humidity);
        double comfHumMem = comfortableHumidity.getMembership(humidity);
        double humidMem = humidHumidity.getMembership(humidity);
        
        System.out.println("\n--- Input Membership Values ---");
        System.out.println("Temperature " + temperature + "°C:");
        System.out.printf("  Cold (Triangular):        %.4f\n", coldMem);
        System.out.printf("  Comfortable (Trapezoidal): %.4f\n", comfTempMem);
        System.out.printf("  Hot (Gaussian):           %.4f\n", hotMem);
        
        System.out.println("\nHumidity " + humidity + "%:");
        System.out.printf("  Dry (Triangular):         %.4f\n", dryMem);
        System.out.printf("  Comfortable (Trapezoidal): %.4f\n", comfHumMem);
        System.out.printf("  Humid (Gaussian):          %.4f\n", humidMem);
        
        // Apply fuzzy rules (Mamdani inference: min for AND, max for OR)
        System.out.println("\n--- Fuzzy Rules Evaluation (Mamdani Inference) ---");
        System.out.println("Using MIN for AND operation, MAX for OR operation\n");
        
        // Rule 1: IF temp is cold AND humidity is dry THEN comfort is uncomfortable
        double rule1 = Math.min(coldMem, dryMem);
        System.out.printf("Rule 1: IF temp=cold AND humidity=dry THEN uncomfortable: %.4f\n", rule1);
        
        // Rule 2: IF temp is cold AND humidity is comfortable THEN comfort is neutral
        double rule2 = Math.min(coldMem, comfHumMem);
        System.out.printf("Rule 2: IF temp=cold AND humidity=comfortable THEN neutral: %.4f\n", rule2);
        
        // Rule 3: IF temp is cold AND humidity is humid THEN comfort is uncomfortable
        double rule3 = Math.min(coldMem, humidMem);
        System.out.printf("Rule 3: IF temp=cold AND humidity=humid THEN uncomfortable: %.4f\n", rule3);
        
        // Rule 4: IF temp is comfortable AND humidity is dry THEN comfort is neutral
        double rule4 = Math.min(comfTempMem, dryMem);
        System.out.printf("Rule 4: IF temp=comfortable AND humidity=dry THEN neutral: %.4f\n", rule4);
        
        // Rule 5: IF temp is comfortable AND humidity is comfortable THEN comfort is comfortable
        double rule5 = Math.min(comfTempMem, comfHumMem);
        System.out.printf("Rule 5: IF temp=comfortable AND humidity=comfortable THEN comfortable: %.4f\n", rule5);
        
        // Rule 6: IF temp is comfortable AND humidity is humid THEN comfort is neutral
        double rule6 = Math.min(comfTempMem, humidMem);
        System.out.printf("Rule 6: IF temp=comfortable AND humidity=humid THEN neutral: %.4f\n", rule6);
        
        // Rule 7: IF temp is hot AND humidity is dry THEN comfort is uncomfortable
        double rule7 = Math.min(hotMem, dryMem);
        System.out.printf("Rule 7: IF temp=hot AND humidity=dry THEN uncomfortable: %.4f\n", rule7);
        
        // Rule 8: IF temp is hot AND humidity is comfortable THEN comfort is neutral
        double rule8 = Math.min(hotMem, comfHumMem);
        System.out.printf("Rule 8: IF temp=hot AND humidity=comfortable THEN neutral: %.4f\n", rule8);
        
        // Rule 9: IF temp is hot AND humidity is humid THEN comfort is uncomfortable
        double rule9 = Math.min(hotMem, humidMem);
        System.out.printf("Rule 9: IF temp=hot AND humidity=humid THEN uncomfortable: %.4f\n", rule9);
        
        // Aggregate rules for each output fuzzy set (max for OR)
        double uncomfortableLevel = Math.max(rule1, Math.max(rule3, Math.max(rule7, rule9)));
        double neutralLevel = Math.max(rule2, Math.max(rule4, Math.max(rule6, rule8)));
        double comfortableLevel = rule5;
        
        System.out.println("\n--- Rule Aggregation (MAX operation) ---");
        System.out.println("Uncomfortable = MAX(Rule1, Rule3, Rule7, Rule9)");
        System.out.println("Neutral = MAX(Rule2, Rule4, Rule6, Rule8)");
        System.out.println("Comfortable = Rule5");
        
        System.out.println("\n--- Aggregated Output Membership Values ---");
        System.out.printf("Uncomfortable (Triangular): %.4f\n", uncomfortableLevel);
        System.out.printf("Neutral (Trapezoidal):     %.4f\n", neutralLevel);
        System.out.printf("Comfortable (Gaussian):     %.4f\n", comfortableLevel);
        
        // Defuzzification using weighted average (centroid method)
        double comfortLevel = defuzzify(uncomfortableLevel, neutralLevel, comfortableLevel, 
                                       uncomfortable, neutral, comfortable);
        
        System.out.println("\n--- Defuzzification (Centroid Method) ---");
        System.out.printf("Overall Comfort Level: %.2f%%\n", comfortLevel);
        
        // Interpretation
        System.out.println("\n--- Interpretation ---");
        if (comfortLevel < 30) {
            System.out.println("The environment is UNCOMFORTABLE");
        } else if (comfortLevel < 70) {
            System.out.println("The environment is NEUTRAL");
        } else {
            System.out.println("The environment is COMFORTABLE");
        }
        
        // Show membership function comparison
        System.out.println("\n--- Membership Function Comparison at Input Values ---");
        System.out.println("Temperature " + temperature + "°C across different MF types:");
        System.out.printf("  Triangular(10,10,25):     %.4f\n", coldTemp.getMembership(temperature));
        System.out.printf("  Trapezoidal(15,20,25,30):  %.4f\n", comfortableTemp.getMembership(temperature));
        System.out.printf("  Gaussian(35,5):            %.4f\n", hotTemp.getMembership(temperature));
        
        System.out.println("\nHumidity " + humidity + "% across different MF types:");
        System.out.printf("  Triangular(0,0,50):        %.4f\n", dryHumidity.getMembership(humidity));
        System.out.printf("  Trapezoidal(30,40,60,70):  %.4f\n", comfortableHumidity.getMembership(humidity));
        System.out.printf("  Gaussian(75,10):           %.4f\n", humidHumidity.getMembership(humidity));
        
        scanner.close();
    }
    
    // Defuzzification using weighted average (centroid method)
    private static double defuzzify(double uncomfLevel, double neutralLevel, double comfLevel,
                                   MembershipFunction uncomfMF, MembershipFunction neutralMF, 
                                   MembershipFunction comfMF) {
        // Use centroids of output fuzzy sets as weights
        double uncomfCentroid = (uncomfMF.getLowerBound() + uncomfMF.getUpperBound()) / 2.0;
        double neutralCentroid = (neutralMF.getLowerBound() + neutralMF.getUpperBound()) / 2.0;
        double comfCentroid = (comfMF.getLowerBound() + comfMF.getUpperBound()) / 2.0;
        
        double numerator = uncomfLevel * uncomfCentroid + 
                          neutralLevel * neutralCentroid + 
                          comfLevel * comfCentroid;
        double denominator = uncomfLevel + neutralLevel + comfLevel;
        
        return denominator != 0 ? numerator / denominator : 50.0; // Default to neutral
    }
}

// Interface for membership functions
interface MembershipFunction {
    double getMembership(double x);
    double getLowerBound();
    double getUpperBound();
}

// Triangular Membership Function
class TriangularMF implements MembershipFunction {
    private double a, b, c;
    
    public TriangularMF(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public double getMembership(double x) {
        if (x <= a) return 0;
        if (x >= c) return 0;
        // Handle case when a == b (right-angled triangle)
        if (a == b) {
            if (x > a && x <= c) return (c - x) / (c - a);
            return 0;
        }
        // Handle case when b == c (left-angled triangle)
        if (b == c) {
            if (x > a && x <= b) return (x - a) / (b - a);
            return 0;
        }
        // Normal triangular case
        if (x > a && x <= b) return (x - a) / (b - a);
        if (x > b && x < c) return (c - x) / (c - b);
        return 0;
    }
    
    @Override
    public double getLowerBound() {
        return a;
    }
    
    @Override
    public double getUpperBound() {
        return c;
    }
}

// Trapezoidal Membership Function
class TrapezoidalMF implements MembershipFunction {
    private double a, b, c, d;
    
    public TrapezoidalMF(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    @Override
    public double getMembership(double x) {
        if (x <= a) return 0;
        if (x > a && x <= b) return (x - a) / (b - a);
        if (x > b && x <= c) return 1;
        if (x > c && x <= d) return (d - x) / (d - c);
        return 0;
    }
    
    @Override
    public double getLowerBound() {
        return a;
    }
    
    @Override
    public double getUpperBound() {
        return d;
    }
}

// Gaussian Membership Function
class GaussianMF implements MembershipFunction {
    private double mean, stdDev;
    private double lower, upper;
    
    public GaussianMF(double mean, double stdDev, double lower, double upper) {
        this.mean = mean;
        this.stdDev = stdDev;
        this.lower = lower;
        this.upper = upper;
    }
    
    @Override
    public double getMembership(double x) {
        if (x < lower || x > upper) return 0;
        return Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2));
    }
    
    @Override
    public double getLowerBound() {
        return lower;
    }
    
    @Override
    public double getUpperBound() {
        return upper;
    }
}
