package don.avdey;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A weasel program
 */
public class WeaselProgram {

    public static final Random RANDOM = new Random();
    public static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";

    public static void main(String[] args) {
        String groundTruth = "METHINKS IT IS LIKE A WEASEL";
        String actualTruth = "ABCDEFG HIJKLMNOPQR STUVWXYZ";

        int steps = 0;
        float mutationFactor =  args.length > 0 ? Integer.parseInt(args[1]) : 0.05f;
        int reproductionFactor =  args.length > 1 ? Integer.parseInt(args[2]) : 100;
        long start = System.nanoTime();
        while (!actualTruth.equals(groundTruth)) {
            Set<String> mutants = new HashSet<>(reproductionFactor);
            for (int i = 0; i < reproductionFactor; i++) {
                mutants.add(mutate(actualTruth, mutationFactor));
            }
            int bestMutantDistance = levenshteinDistance(actualTruth, groundTruth);
            for (String mutant: mutants) {
                int mutantDistance = levenshteinDistance(mutant, groundTruth);
                if (mutantDistance < bestMutantDistance) {
                    actualTruth = mutant;
                    bestMutantDistance = mutantDistance;
                }
            }
            steps++;
        }
        long end = System.nanoTime();

        System.out.println("Number of steps taken to converge to ground truth: " + steps);
        System.out.println("Time taken, seconds: " + (end-start)/1000000000);
    }

    private static String mutate(String source, float factor) {
        StringWriter result = new StringWriter();
        for (char ch : source.toCharArray()) {
            if (RANDOM.nextFloat() < factor) {
                result.write(SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length())));
            } else {
                result.write(ch);
            }
        }
        return result.toString();
    }

    private static int levenshteinDistance(String source, String destination) {
        int[][] dp = new int[source.length() + 1][destination.length() + 1];
        for (int i = 0; i <= source.length(); i++) {
            for (int j = 0; j <= destination.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + ((source.charAt(i - 1) == destination.charAt(j - 1)) ? 0 : 1)
                    );
                }
            }
        }
        return dp[source.length()][destination.length()];
    }
}
