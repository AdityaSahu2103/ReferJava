package optimize;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Assignment 1: Fuzzy Set Operations and Relations
 * Demonstrates union, intersection, complement, and other fuzzy operations
 * on entire fuzzy sets (universe-based operations)
 */
public class Assignment1 {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Assignment 1: Fuzzy Set Operations");
        System.out.println("==========================================\n");
        
        // Create fuzzy sets for temperature
        TriangularMF cold = new TriangularMF(10, 10, 25);
        TriangularMF comfortable = new TriangularMF(15, 25, 30);
        TriangularMF hot = new TriangularMF(25, 40, 40);
        
        // Create common universe for operations
        List<Double> universe = createUniverse(10, 40, 50);
        
        // Evaluate membership functions over the universe
        List<Double> coldValues = new ArrayList<>();
        List<Double> comfortableValues = new ArrayList<>();
        List<Double> hotValues = new ArrayList<>();
        
        for (double x : universe) {
            coldValues.add(cold.getMembership(x));
            comfortableValues.add(comfortable.getMembership(x));
            hotValues.add(hot.getMembership(x));
        }
        
        // Perform fuzzy operations on entire sets
        List<Double> unionColdHot = union(coldValues, hotValues);
        List<Double> intersectionColdComfortable = intersection(coldValues, comfortableValues);
        List<Double> complementComfortable = complement(comfortableValues);
        List<Double> algebraicProduct = algebraicProduct(coldValues, comfortableValues);
        List<Double> algebraicSum = algebraicSum(coldValues, comfortableValues);
        List<Double> difference = difference(hotValues, comfortableValues);
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter temperature (10-40°C): ");
        double temp = scanner.nextDouble();
        
        if (temp < 10 || temp > 40) {
            System.out.println("Temperature must be between 10 and 40°C");
            scanner.close();
            return;
        }
        
        // Find index in universe
        int index = findClosestIndex(universe, temp);
        
        // Calculate membership values
        double coldMem = cold.getMembership(temp);
        double comfMem = comfortable.getMembership(temp);
        double hotMem = hot.getMembership(temp);
        
        System.out.println("\n--- Membership Values for " + temp + "°C ---");
        System.out.printf("Cold:        %.4f\n", coldMem);
        System.out.printf("Comfortable: %.4f\n", comfMem);
        System.out.printf("Hot:         %.4f\n", hotMem);
        
        // Display operation results for the input temperature
        System.out.println("\n--- Fuzzy Set Operations (at " + temp + "°C) ---");
        System.out.printf("Union (Cold ∪ Hot):              %.4f\n", unionColdHot.get(index));
        System.out.printf("Intersection (Cold ∩ Comfortable): %.4f\n", intersectionColdComfortable.get(index));
        System.out.printf("Complement (Comfortable'):       %.4f\n", complementComfortable.get(index));
        System.out.printf("Algebraic Product (Cold · Comfortable): %.4f\n", algebraicProduct.get(index));
        System.out.printf("Algebraic Sum (Cold + Comfortable): %.4f\n", algebraicSum.get(index));
        System.out.printf("Difference (Hot - Comfortable): %.4f\n", difference.get(index));
        
        // Display membership function values across range
        System.out.println("\n--- Membership Functions Across Range ---");
        System.out.println("Temp(°C)  Cold    Comfortable  Hot");
        System.out.println("----------------------------------------");
        for (int i = 10; i <= 40; i += 5) {
            double c = cold.getMembership(i);
            double co = comfortable.getMembership(i);
            double h = hot.getMembership(i);
            System.out.printf("%3d      %.3f      %.3f       %.3f\n", i, c, co, h);
        }
        
        // Show operation results across range
        System.out.println("\n--- Union (Cold ∪ Hot) Across Range ---");
        System.out.println("Temp(°C)  Union Value");
        System.out.println("----------------------");
        for (int i = 0; i < universe.size(); i += 5) {
            System.out.printf("%6.1f      %.4f\n", universe.get(i), unionColdHot.get(i));
        }
        
        scanner.close();
    }
    
    // Create universe of discourse
    private static List<Double> createUniverse(double lower, double upper, int resolution) {
        List<Double> universe = new ArrayList<>();
        double step = (upper - lower) / (resolution - 1);
        for (int i = 0; i < resolution; i++) {
            universe.add(lower + i * step);
        }
        return universe;
    }
    
    // Find closest index in universe
    private static int findClosestIndex(List<Double> universe, double value) {
        int closestIndex = 0;
        double minDiff = Double.MAX_VALUE;
        for (int i = 0; i < universe.size(); i++) {
            double diff = Math.abs(universe.get(i) - value);
            if (diff < minDiff) {
                minDiff = diff;
                closestIndex = i;
            }
        }
        return closestIndex;
    }
    
    // Fuzzy set operations
    private static List<Double> union(List<Double> setA, List<Double> setB) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < setA.size(); i++) {
            result.add(Math.max(setA.get(i), setB.get(i)));
        }
        return result;
    }
    
    private static List<Double> intersection(List<Double> setA, List<Double> setB) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < setA.size(); i++) {
            result.add(Math.min(setA.get(i), setB.get(i)));
        }
        return result;
    }
    
    private static List<Double> complement(List<Double> set) {
        List<Double> result = new ArrayList<>();
        for (Double value : set) {
            result.add(1 - value);
        }
        return result;
    }
    
    private static List<Double> algebraicProduct(List<Double> setA, List<Double> setB) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < setA.size(); i++) {
            result.add(setA.get(i) * setB.get(i));
        }
        return result;
    }
    
    private static List<Double> algebraicSum(List<Double> setA, List<Double> setB) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < setA.size(); i++) {
            double a = setA.get(i);
            double b = setB.get(i);
            result.add(a + b - a * b);
        }
        return result;
    }
    
    private static List<Double> difference(List<Double> setA, List<Double> setB) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < setA.size(); i++) {
            result.add(Math.max(setA.get(i) - setB.get(i), 0));
        }
        return result;
    }
}

// Simple Triangular Membership Function
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

