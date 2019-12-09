import java.util.Queue;
import java.util.Stack;

public class IntCodeComputer {

    private final int[] program;
    private int programIndex = 0;
    private int relativeBase = 0;

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

    public boolean process(Queue<Integer> userInputs, Queue<Integer> outputs) {
        while (program[programIndex] != 99) {
            switch (program[programIndex] % 100) {
                case 1:
                    programIndex = opSum(program[programIndex] / 100, programIndex);
                    break;
                case 2:
                    programIndex = opMultiply(program[programIndex] / 100, programIndex);
                    break;
                case 3:
                    try {
                        programIndex = opUserInput(program[programIndex] / 100, programIndex, userInputs);
                    } catch (Exception e) {
                        return false;
                    }
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
                case 9:
                    programIndex = opMoveRelativeBase(program[programIndex] / 100, programIndex);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        return true;
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

    private int opUserInput(int parameterMode, int opIndex, Queue<Integer> userInputs) throws Exception {
        if (userInputs.isEmpty())
            throw new Exception("Wait for user input");
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

    private int opMoveRelativeBase(int parameterMode, int opIndex) {
        Stack<Integer> parameters = parseParameters(parameterMode, 1, opIndex + 1);
        relativeBase += parameters.pop();
        return opIndex + 2;
    }

    private Stack<Integer> parseParameters(int parametersMode, int size, int startIndex) {
        Stack<Integer> parameters = new Stack<>();
        int mode = parametersMode;
        for (int i = 0; i < size; i++) {
            switch (mode % 10) {
                case 0:
                    parameters.push(program[program[startIndex + i]]);
                    break;
                case 1:
                    parameters.push(program[startIndex + i]);
                    break;
                case 2:
                    parameters.push(program[program[startIndex + i] + relativeBase]);
                    break;
            }
            mode /= 10;
        }
        return parameters;
    }

}