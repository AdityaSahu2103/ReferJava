import java.util.*;

public class AllMutations {

    static Random rand = new Random();

    public static void main(String[] args) {
        String binary = "110011";
        int[] permutation = {1, 2, 3, 4, 5, 6};

        System.out.println("Original Binary: " + binary);
        bitFlipMutation(binary, 0.2);
        scrambleMutation(binary, 0.5);
        System.out.println();

        System.out.println("Original Permutation: " + Arrays.toString(permutation));
        swapMutation(permutation.clone());
        inversionMutation(permutation.clone());
        scrambleMutationPermutation(permutation.clone());
    }

    static void bitFlipMutation(String chromosome, double rate) {
        StringBuilder mutated = new StringBuilder();
        for (int i = 0; i < chromosome.length(); i++) {
            if (rand.nextDouble() < rate) {
                mutated.append(chromosome.charAt(i) == '0' ? '1' : '0');
            } else {
                mutated.append(chromosome.charAt(i));
            }
        }
        System.out.println("=== Bit Flip Mutation ===");
        System.out.println("Mutated: " + mutated);
    }

    static void scrambleMutation(String chromosome, double rate) {
        char[] chars = chromosome.toCharArray();
        if (rand.nextDouble() < rate) {
            int start = rand.nextInt(chars.length - 1);
            int end = start + 1 + rand.nextInt(chars.length - start - 1);
            char[] segment = Arrays.copyOfRange(chars, start, end);
            for (int i = segment.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                char temp = segment[i];
                segment[i] = segment[j];
                segment[j] = temp;
            }
            System.arraycopy(segment, 0, chars, start, segment.length);
        }
        System.out.println("=== Scramble Mutation (Binary) ===");
        System.out.println("Mutated: " + new String(chars));
    }

    static void swapMutation(int[] chrom) {
        int i = rand.nextInt(chrom.length);
        int j = rand.nextInt(chrom.length);
        int temp = chrom[i];
        chrom[i] = chrom[j];
        chrom[j] = temp;
        System.out.println("=== Swap Mutation ===");
        System.out.println("Mutated: " + Arrays.toString(chrom));
    }

    static void inversionMutation(int[] chrom) {
        int start = rand.nextInt(chrom.length - 1);
        int end = start + rand.nextInt(chrom.length - start);
        while (start < end) {
            int temp = chrom[start];
            chrom[start] = chrom[end];
            chrom[end] = temp;
            start++;
            end--;
        }
        System.out.println("=== Inversion Mutation ===");
        System.out.println("Mutated: " + Arrays.toString(chrom));
    }

    static void scrambleMutationPermutation(int[] chrom) {
        int start = rand.nextInt(chrom.length - 1);
        int end = start + 1 + rand.nextInt(chrom.length - start - 1);
        int[] segment = Arrays.copyOfRange(chrom, start, end);
        for (int i = segment.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = segment[i];
            segment[i] = segment[j];
            segment[j] = temp;
        }
        System.arraycopy(segment, 0, chrom, start, segment.length);
        System.out.println("=== Scramble Mutation (Permutation) ===");
        System.out.println("Mutated: " + Arrays.toString(chrom));
    }
}