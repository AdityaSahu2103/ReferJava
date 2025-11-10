import java.util.*;

public class SimpleSelections {

    public static void main(String[] args) {
        String[] individuals = {"A", "B", "C", "D", "E"};
        double[] fitness = {10, 20, 30, 40, 50};
        Random rand = new Random();

        System.out.println("Population:");
        for (int i = 0; i < individuals.length; i++)
            System.out.println(individuals[i] + " → " + fitness[i]);

        System.out.println("\n--- Selection Methods ---");
        System.out.println("Roulette Wheel: " + roulette(individuals, fitness));
        System.out.println("Rank Selection: " + rank(individuals, fitness));
        System.out.println("Tournament Selection: " + tournament(individuals, fitness, 3));
        System.out.println("Stochastic Universal Sampling: " + sus(individuals, fitness, 2));
        System.out.println("Random Selection: " + random(individuals, rand));
        System.out.println("Elitism (Top 2): " + elitism(individuals, fitness, 2));
    }

    static String roulette(String[] ind, double[] fit) {
        double total = 0, sum = 0;
        for (double f : fit) total += f;
        double r = Math.random() * total;
        for (int i = 0; i < fit.length; i++) {
            sum += fit[i];
            if (sum >= r) return ind[i];
        }
        return ind[ind.length - 1];
    }

    static String rank(String[] ind, double[] fit) {
        int n = ind.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, Comparator.comparingDouble(i -> fit[i]));
        double totalRank = n * (n + 1) / 2.0, sum = 0;
        double r = Math.random() * totalRank;
        for (int i = 0; i < n; i++) {
            sum += (i + 1);
            if (sum >= r) return ind[idx[i]];
        }
        return ind[idx[n - 1]];
    }

    static String tournament(String[] ind, double[] fit, int k) {
        Random rand = new Random();
        double bestFit = -1;
        String best = "";
        for (int i = 0; i < k; i++) {
            int idx = rand.nextInt(ind.length);
            if (fit[idx] > bestFit) {
                bestFit = fit[idx];
                best = ind[idx];
            }
        }
        return best;
    }

    static List<String> sus(String[] ind, double[] fit, int numSelect) {
        List<String> selected = new ArrayList<>();
        double total = 0;
        for (double f : fit) total += f;
        double dist = total / numSelect;
        double start = Math.random() * dist;
        for (int s = 0; s < numSelect; s++) {
            double pointer = start + s * dist, sum = 0;
            for (int i = 0; i < fit.length; i++) {
                sum += fit[i];
                if (sum >= pointer) {
                    selected.add(ind[i]);
                    break;
                }
            }
        }
        return selected;
    }

    static String random(String[] ind, Random rand) {
        return ind[rand.nextInt(ind.length)];
    }

    static List<String> elitism(String[] ind, double[] fit, int numElites) {
        int n = ind.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Double.compare(fit[b], fit[a]));
        List<String> elites = new ArrayList<>();
        for (int i = 0; i < numElites; i++) elites.add(ind[idx[i]]);
        return elites;
    }
}