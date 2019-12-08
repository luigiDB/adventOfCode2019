import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Day4 {


    public static int es1(int start, int end) {
        int counter = 0;

        for (int i = start; i <= end; i++) {
            if (checkNumber(i))
                counter++;
        }

        return counter;
    }

    private static boolean checkNumber(int i) {
        int number = i / 10;
        Stack<Integer> stack = new Stack<>();
        stack.push(i % 10);
        int countDoubles = 0;
        for (int j = 0; j < 6; j++) {
            int cipher = number % 10;
            if (cipher > stack.peek()) {
                return false;
            }
            if (cipher == stack.peek()) {
                countDoubles++;
            }
            stack.push(cipher);
            number /= 10;
        }
        return countDoubles > 0;
    }

    public static int es2(int start, int end) {
        int counter = 0;

        for (int i = start; i <= end; i++) {
            if (checkNumber3Conditions(i))
                counter++;
        }

        return counter;
    }

    private static boolean checkNumber3Conditions(int i) {
        int number = i / 10;
        int[] occurrences = new int[10];
        Stack<Integer> stack = new Stack<>();
        stack.push(i % 10);
        occurrences[i % 10]++;
        for (int j = 0; j < 6; j++) {
            int cipher = number % 10;
            if (cipher > stack.peek()) {
                return false;
            }
            occurrences[cipher]++;
            stack.push(cipher);
            number /= 10;
        }
        return Arrays.stream(occurrences).anyMatch(x -> x == 2);
    }
}
