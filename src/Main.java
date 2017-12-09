import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class Main {

    /**
     * Fisher-Yates shuffle
     * @param data array to be sorted
     */
    private void shuffleArray(int[] data, Random rnd) {
        // To shuffle an array a of n elements (indices 0..n-1):
        int t, j;
        // for i from n−1 downto 1 do
        for (int i = data.length - 1; i >= 1; i--) {
            // j ← random integer such that 0 ≤ j ≤ i
            j = rnd.nextInt(i + 1);
            // exchange a[j] and a[i]
            t = data[j];
            data[j] = data[i];
            data[i] = t;
        }
    }

    private void printArray(int[] data) {
        System.out.println(Arrays.toString(data));
    }

    private void fillArray(int[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
    }

    private int[] getShuffledArray(int length, Random rnd) {
        int[] array = new int[length];
        fillArray(array);
        shuffleArray(array, rnd);
        return array;
    }

    private void validateArray(int[] data) {
        int[] counters = new int[data.length]; // initialized with 0
        for (int d : data) {
            counters[d]++;
        }
        for (int c : counters) {
            if (c != 1) {
                throw new RuntimeException("Data is corrupted!");
            }
        }
    }

    /**
     * Magic approach: everyone starts with a box with his own number and continues with number found in it.
     */
    private boolean magicApproach(int[] boxes, int attemptsAllowed, int verbosity) {
        boolean isTeamFailed = false;
        for (int i = 0; i < boxes.length; i++) {
            int boxToLookAt = i; // start with box with player's number
            int attemptsLeft = attemptsAllowed; // attempts limit
            int discovered; // number seen in box
            do {
                discovered = boxes[boxToLookAt];
                boxToLookAt = discovered; // next time look into the box which number is written inside the current one
            } while (discovered != i && --attemptsLeft > 0);
            if (discovered == i) {
                if (verbosity >= 3) System.out.println("Magic player #" + i + " succeeded, attempts left: " + attemptsLeft);
            } else {
                assert attemptsLeft == 0;
                if (verbosity >= 3) System.out.println("Magic player #" + i + " failed");
                isTeamFailed = true;
                break;
            }
        }
        return !isTeamFailed;
    }

    /**
     * Random approach: everyone opens the boxes in random order.
     */
    private boolean randomApproach(int[] boxes, int attemptsAllowed, int verbosity, Random rnd) {
        boolean isTeamFailed = false;
        for (int i = 0; i < boxes.length; i++) {
            int[] randomSequence = getShuffledArray(boxes.length, rnd);
            int randomIndex = 0;
            int attemptsLeft = attemptsAllowed; // attempts limit
            int discovered; // number seen in box
            do {
                discovered = boxes[randomSequence[randomIndex++]]; // каждый раз заглядываем в новый случайный ящик
            } while (discovered != i && --attemptsLeft > 0);
            if (discovered == i) {
                if (verbosity >= 3) System.out.println("Random player #" + i + " succeeded, attempts left: " + attemptsLeft);
            } else {
                assert attemptsLeft == 0;
                if (verbosity >= 3) System.out.println("Random player #" + i + " failed");
                isTeamFailed = true;
                break;
            }
        }
        return !isTeamFailed;
    }

    /**
     * Stubborn approach: everyone always opens the boxes sequentially from the beginning.
     */
    private boolean stubbornApproach(int[] boxes, int attemptsAllowed, int verbosity) {
        boolean isTeamFailed = false;
        for (int i = 0; i < boxes.length; i++) {
            int stubbornIndex = 0;
            int attemptsLeft = attemptsAllowed; // attempts limit
            int discovered; // number seen in box
            do {
                discovered = boxes[stubbornIndex++]; // moving through boxes in a row
            } while (discovered != i && --attemptsLeft > 0);
            if (discovered == i) {
                if (verbosity >= 3) System.out.println("Stubborn player #" + i + " succeeded, attempts left: " + attemptsLeft);
            } else {
                assert attemptsLeft == 0;
                if (verbosity >= 3) System.out.println("Stubborn player #" + i + " failed");
                isTeamFailed = true;
                break;
            }
        }
        return !isTeamFailed;
    }

    /**
     * @param rnd: entropy source
     * @param teamSize: 100 in the original version or 14 to give some chances to the Random team
     * @param gameRounds: 1000000 if you have modern CPU
     * @param verbosity: 0 = silent, 1 = say something, 2 = tell me more, ...
     */
    private void runContest(Random rnd, int teamSize, int gameRounds, int verbosity) {
        final int attemptsAllowed = teamSize > 1 ? teamSize / 2 : 1; // at least 1 attempt

        int magicScore = 0;
        int randomScore = 0;
        int stubbornScore = 0;
        int[] boxes = new int[teamSize];
        fillArray(boxes);

        for (int i = 0; i < gameRounds; i++) {
            shuffleArray(boxes, rnd);
            if (verbosity >= 2) printArray(boxes);

            if (magicApproach(boxes, attemptsAllowed, verbosity)) {
                magicScore++;
                if (verbosity >= 2) System.out.println("Magic team succeeded!");
            }
            if (randomApproach(boxes, attemptsAllowed, verbosity, rnd)) {
                randomScore++;
                if (verbosity >= 2) System.out.println("Random team succeeded!");
            }
            if (stubbornApproach(boxes, attemptsAllowed, verbosity)) {
                stubbornScore++;
                if (verbosity >= 2) System.out.println("Stubborn team succeeded!");
            }
        }

        if (verbosity >= 1) System.out.println(
                String.format("Final Magic vs. Random vs. Stubborn score is [%d : %d : %d] " +
                                "after %d games played with %d players.",
                    magicScore, randomScore, stubbornScore, gameRounds, teamSize));

        validateArray(boxes); // just another data integrity check
    }

    public static void main(String[] args) {
        Main main = new Main();

        Random pseudoRnd = new Random(); // do not re-create Random each time or else this will spoil statistics
        Random trueRandom = new SecureRandom(); // you can use SecureRandom but with significantly fewer game rounds

        main.runContest(trueRandom,42, 5, 3);
        main.runContest(pseudoRnd,17, 1000000, 1);
        main.runContest(trueRandom,1, 1, 1);
        main.runContest(pseudoRnd,100, 1000000, 1);
    }
}
