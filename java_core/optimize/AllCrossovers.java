import java.util.*;

public class AllCrossovers {

    static Random rand = new Random();

    public static void main(String[] args) {
        String parent1 = "110011";
        String parent2 = "101101";

        System.out.println("Parent 1: " + parent1);
        System.out.println("Parent 2: " + parent2);
        System.out.println();

        singlePointCrossover(parent1, parent2);
        twoPointCrossover(parent1, parent2);
        uniformCrossover(parent1, parent2);
        arithmeticCrossover(new double[]{1.2, 3.4, 5.6}, new double[]{2.2, 1.4, 4.6});
        blendCrossover(new double[]{1.2, 3.4, 5.6}, new double[]{2.2, 1.4, 4.6});
    }

    static void singlePointCrossover(String p1, String p2) {
        int point = rand.nextInt(p1.length() - 1) + 1;
        String c1 = p1.substring(0, point) + p2.substring(point);
        String c2 = p2.substring(0, point) + p1.substring(point);
        System.out.println("=== Single Point Crossover ===");
        System.out.println("Point: " + point);
        System.out.println("Child 1: " + c1);
        System.out.println("Child 2: " + c2);
        System.out.println();
    }

    static void twoPointCrossover(String p1, String p2) {
        int a = rand.nextInt(p1.length() - 2);
        int b = a + 1 + rand.nextInt(p1.length() - a - 1);
        String c1 = p1.substring(0, a) + p2.substring(a, b) + p1.substring(b);
        String c2 = p2.substring(0, a) + p1.substring(a, b) + p2.substring(b);
        System.out.println("=== Two Point Crossover ===");
        System.out.println("Points: " + a + ", " + b);
        System.out.println("Child 1: " + c1);
        System.out.println("Child 2: " + c2);
        System.out.println();
    }

    static void uniformCrossover(String p1, String p2) {
        StringBuilder c1 = new StringBuilder();
        StringBuilder c2 = new StringBuilder();
        for (int i = 0; i < p1.length(); i++) {
            if (rand.nextBoolean()) {
                c1.append(p1.charAt(i));
                c2.append(p2.charAt(i));
            } else {
                c1.append(p2.charAt(i));
                c2.append(p1.charAt(i));
            }
        }
        System.out.println("=== Uniform Crossover ===");
        System.out.println("Child 1: " + c1);
        System.out.println("Child 2: " + c2);
        System.out.println();
    }

    static void arithmeticCrossover(double[] p1, double[] p2) {
        double alpha = rand.nextDouble();
        double[] c1 = new double[p1.length];
        double[] c2 = new double[p1.length];
        for (int i = 0; i < p1.length; i++) {
            c1[i] = alpha * p1[i] + (1 - alpha) * p2[i];
            c2[i] = alpha * p2[i] + (1 - alpha) * p1[i];
        }
        System.out.println("=== Arithmetic Crossover ===");
        System.out.println("Alpha: " + alpha);
        System.out.println("Child 1: " + Arrays.toString(c1));
        System.out.println("Child 2: " + Arrays.toString(c2));
        System.out.println();
    }

    static void blendCrossover(double[] p1, double[] p2) {
        double alpha = 0.5;
        double[] c1 = new double[p1.length];
        double[] c2 = new double[p1.length];
        for (int i = 0; i < p1.length; i++) {
            double d = Math.abs(p1[i] - p2[i]);
            double min = Math.min(p1[i], p2[i]) - alpha * d;
            double max = Math.max(p1[i], p2[i]) + alpha * d;
            c1[i] = min + rand.nextDouble() * (max - min);
            c2[i] = min + rand.nextDouble() * (max - min);
        }
        System.out.println("=== Blend (BLX-α) Crossover ===");
        System.out.println("Alpha: " + alpha);
        System.out.println("Child 1: " + Arrays.toString(c1));
        System.out.println("Child 2: " + Arrays.toString(c2));
        System.out.println();
    }
}