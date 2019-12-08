import java.util.Arrays;

public class Day1 {

    public static int es1(int[] input) {
        return Arrays.stream(input)
                .map(x -> (x / 3) - 2)
                .sum();
    }

    public static int es2(int[] input) {
        return Arrays.stream(input)
                .map(x -> {
                    int fuel = 0;
                    int mass = x;
                    while ((mass / 3) - 2 > 0) {
                        fuel += (mass / 3) - 2;
                        mass = (mass / 3) - 2;
                    }
                    return fuel;
                })
                .sum();
    }
}
