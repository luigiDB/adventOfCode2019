import java.util.*;

public class Day7 {

    public long maxAmplification(String program) {
        long maxOutput = Integer.MIN_VALUE;
        for (List<Long> combination : allPermutations()) {
            long secondInput = 0;
            for (int i = 0; i < combination.size(); i++) {
                IntCodeComputer computer = new IntCodeComputer(program);
                Queue<Long> inputs = new LinkedList<>();
                inputs.add(combination.get(i));
                inputs.add(secondInput);
                Queue<Long> outputs = new LinkedList<>();
                computer.process(inputs, outputs);
                secondInput = outputs.poll();
            }
            maxOutput = Math.max(maxOutput, secondInput);
        }
        return maxOutput;
    }

    private List<List<Long>> allPermutations() {
        List<List<Long>> permutations = new LinkedList<>();
        evaluateAllPermutations(5, new Long[]{0L, 1L, 2L, 3L, 4L}, permutations);
        return permutations;
    }

    public void evaluateAllPermutations(int n, Long[] elements, List<List<Long>> combinations) {
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

    private void swap(Long[] input, int a, int b) {
        long tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private List<List<Long>> allFeedbackPermutations() {
        List<List<Long>> permutations = new LinkedList<>();
        evaluateAllPermutations(5, new Long[]{5L, 6L, 7L, 8L, 9L}, permutations);
        return permutations;
    }

    public long maxAmplificationInFeedbackLoop(String program) {
        long maxOutput = Integer.MIN_VALUE;
        for (List<Long> combination : allFeedbackPermutations()) {

            //create amplifier feedback loop
            Amplifier[] amplifiers = new Amplifier[5];
            Queue<Long>[] signals = new Queue[5];
            for (int i = 0; i < 5; i++) {
                signals[i] = new LinkedList<>();
                signals[i].offer(combination.get(i));
            }
            for (int i = 0; i < 5; i++) {
                amplifiers[i] = new Amplifier(program, signals[i], signals[(i + 1) % 5]);
            }

            //add first signal to first amplifier
            signals[0].offer(0L);

            boolean computationCleared = false;
            while (!computationCleared) {
                for (int i = 0; i < 5; i++) {
                    computationCleared = amplifiers[i].process();
                }
            }

            maxOutput = Math.max(maxOutput, amplifiers[4].pollOutputs());
        }
        return maxOutput;
    }

    private class Amplifier {
        private final String program;
        IntCodeComputer computer;
        Queue<Long> inputs;
        Queue<Long> outputs;

        public Amplifier(String program, Queue<Long> inputs, Queue<Long> outputs) {
            this.program = program;
            computer = new IntCodeComputer(program);
            this.inputs = inputs;
            this.outputs = outputs;
        }

        public long pollOutputs() {
            return outputs.poll();
        }

        public boolean process() {
            return computer.process(inputs, outputs);
        }
    }
}
