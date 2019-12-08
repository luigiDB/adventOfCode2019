import java.util.*;

public class Day7 {

    public int maxAmplification(String program) {
        int maxOutput = Integer.MIN_VALUE;
        for (List<Integer> combination : allPermutations()) {
            int secondInput = 0;
            for (int i = 0; i < combination.size(); i++) {
                IntCodeComputer computer = new IntCodeComputer(program);
                Queue<Integer> inputs = new LinkedList<>();
                inputs.add(combination.get(i));
                inputs.add(secondInput);
                Queue<Integer> outputs = new LinkedList<>();
                computer.process(inputs, outputs);
                secondInput = outputs.poll();
            }
            maxOutput = Math.max(maxOutput, secondInput);
        }
        return maxOutput;
    }

    private List<List<Integer>> allPermutations() {
        List<List<Integer>> permutations = new LinkedList<>();
        evaluateAllPermutations(5, new Integer[]{0, 1, 2, 3, 4}, permutations);
        return permutations;
    }

    public void evaluateAllPermutations(int n, Integer[] elements, List<List<Integer>> combinations) {
        if (n == 1) {
            combinations.add(new ArrayList<>(Arrays.asList(elements)));
        } else {
            for (int i = 0; i < n - 1; i++) {
                evaluateAllPermutations(n - 1, elements, combinations);
                if (n % 2 == 0) {
                    swap(elements, i, n - 1);
                } else {
                    swap(elements, 0, n - 1);
                }
            }
            evaluateAllPermutations(n - 1, elements, combinations);
        }
    }

    private void swap(Integer[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private List<List<Integer>> allFeedbackPermutations() {
        List<List<Integer>> permutations = new LinkedList<>();
        //evaluateAllPermutations(5, new Integer[]{5, 6, 7, 8, 9}, permutations);
        permutations.add(List.of(5, 6, 7, 8, 9));
        return permutations;
    }

    public int maxAmplificationInFeedbackLoop(String program) {
        int maxOutput = Integer.MIN_VALUE;
        for (List<Integer> combination : allFeedbackPermutations()) {
            Amplifier[] amplifiers = new Amplifier[5];
            Queue<Integer>[] signals = new Queue[5];
            for (int i = 0; i < 5; i++) {
                signals[i] = new LinkedList<>();
                signals[i].offer(combination.get(i));
            }
            for (int i = 0; i < 5; i++) {
                amplifiers[0] = new Amplifier(program, signals[i], signals[(i + 1) % 5]);
            }



            maxOutput = Math.max(maxOutput, amplifiers[4].pollOutputs());
        }
        return maxOutput;
    }

    private class Amplifier {
        private final String program;
        IntCodeComputer computer;
        Queue<Integer> inputs;
        Queue<Integer> outputs;

        public Amplifier(String program, Queue<Integer> inputs, Queue<Integer> outputs) {
            this.program = program;
            computer = new
            this.inputs = inputs;
            this.outputs = outputs;
        }

        public Integer pollOutputs() {
            return outputs.poll();
        }
    }
}
