import java.util.Queue;
import java.util.Stack;

public class IntCodeComputer {

    private final int[] program;

    public IntCodeComputer(int[] inputProgram) {
        program = new int[inputProgram.length];
        System.arraycopy(inputProgram, 0, program, 0, inputProgram.length);
    }

    public IntCodeComputer(String inputProgram) {
        program = stringToIntArray(inputProgram);
    }

    private static int[] stringToIntArray(String input) {
        String[] split = input.split(",");
        int[] program = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            program[i] = Integer.parseInt(split[i]);
        }
        return program;
    }

    public int[] getProgram() {
        return program;
    }

    public void process(Queue<Integer> userInputs, Queue<Integer> outputs) {
        int programIndex = 0;
        while (program[programIndex] != 99) {
            switch (program[programIndex] % 100) {
                case 1:
                    programIndex = opSum(program[programIndex] / 100, programIndex);
                    break;
                case 2:
                    programIndex = opMultiply(program[programIndex] / 100, programIndex);
                    break;
                case 3:
                    programIndex = opUserInput(program[programIndex] / 100, programIndex, userInputs);
                    break;
                case 4:
                    programIndex = opOutput(program[programIndex] / 100, programIndex, outputs);
                    break;
                case 5:
                    programIndex = opJumpIfTrue(program[programIndex] / 100, programIndex);
                    break;
                case 6:
                    programIndex = opJumpIfFalse(program[programIndex] / 100, programIndex);
                    break;
                case 7:
                    programIndex = opLessThan(program[programIndex] / 100, programIndex);
                    break;
                case 8:
                    programIndex = opEquals(program[programIndex] / 100, programIndex);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    private int opSum(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[program[opIndex + 3]] = parameters.pop() + parameters.pop();
        return opIndex + 4;
    }

    private int opMultiply(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[program[opIndex + 3]] = parameters.pop() * parameters.pop();
        return opIndex + 4;
    }

    private int opUserInput(int parameterMode, int opIndex, Queue<Integer> userInputs) {
        program[program[opIndex + 1]] = userInputs.poll();
        return opIndex + 2;
    }

    private int opOutput(int parameterMode, int opIndex, Queue<Integer> outputs) {
        Stack<Integer> parameters = parseParameters(parameterMode, 1, opIndex + 1);
        outputs.offer(parameters.pop());
        return opIndex + 2;
    }

    private int opJumpIfTrue(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        int possibleAddress = parameters.pop();
        return (parameters.pop() != 0) ? possibleAddress : opIndex + 3;
    }

    private int opJumpIfFalse(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        int possibleAddress = parameters.pop();
        return (parameters.pop() == 0) ? possibleAddress : opIndex + 3;
    }

    private int opLessThan(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[program[opIndex + 3]] = (parameters.pop().compareTo(parameters.pop()) > 0) ? 1 : 0;
        return opIndex + 4;
    }

    private int opEquals(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[program[opIndex + 3]] = (parameters.pop().equals(parameters.pop())) ? 1 : 0;
        return opIndex + 4;
    }

    private Stack<Integer> parseParameters(int parametersMode, int size, int startIndex) {
        Stack<Integer> parameters = new Stack<>();
        int mode = parametersMode;
        for (int i = 0; i < size; i++) {
            if (mode % 10 == 1) {
                parameters.push(program[startIndex + i]);
            } else {
                parameters.push(program[program[startIndex + i]]);
            }
            mode /= 10;
        }
        return parameters;
    }

}