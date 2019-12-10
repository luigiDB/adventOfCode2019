import java.util.Queue;
import java.util.Stack;

public class IntCodeComputer {

    private final long[] program;
    private int programIndex = 0;
    private int relativeBase = 0;

    public IntCodeComputer(String inputProgram) {
        program = stringToIntArray(inputProgram, 1);
    }

    public IntCodeComputer(String inputProgram, int sizeMultiplier) {
        program = stringToIntArray(inputProgram, sizeMultiplier);
    }

    private static long[] stringToIntArray(String input, int sizeMultiplier) {
        String[] split = input.split(",");
        long[] program = new long[split.length * sizeMultiplier];
        for (int i = 0; i < split.length; i++) {
            program[i] = Long.parseLong(split[i]);
        }
        return program;
    }

    public long[] getProgram() {
        return program;
    }

    public boolean process(Queue<Long> userInputs, Queue<Long> outputs) {
        while (program[programIndex] != 99) {
            switch (Long.valueOf(program[programIndex] % 100).toString()) {
                case "1":
                    programIndex = opSum(program[programIndex] / 100, programIndex);
                    break;
                case "2":
                    programIndex = opMultiply(program[programIndex] / 100, programIndex);
                    break;
                case "3":
                    try {
                        programIndex = opUserInput(program[programIndex] / 100, programIndex, userInputs);
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                case "4":
                    programIndex = opOutput(program[programIndex] / 100, programIndex, outputs);
                    break;
                case "5":
                    programIndex = opJumpIfTrue(program[programIndex] / 100, programIndex);
                    break;
                case "6":
                    programIndex = opJumpIfFalse(program[programIndex] / 100, programIndex);
                    break;
                case "7":
                    programIndex = opLessThan(program[programIndex] / 100, programIndex);
                    break;
                case "8":
                    programIndex = opEquals(program[programIndex] / 100, programIndex);
                    break;
                case "9":
                    programIndex = opMoveRelativeBase(program[programIndex] / 100, programIndex);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        return true;
    }

    private int opSum(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[parseOutputPosition(parameterMode, 100, opIndex + 3)] = parameters.pop() + parameters.pop();
        return opIndex + 4;
    }

    private int opMultiply(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[parseOutputPosition(parameterMode, 100, opIndex + 3)] = parameters.pop() * parameters.pop();
        return opIndex + 4;
    }

    private int opUserInput(long parameterMode, int opIndex, Queue<Long> userInputs) throws Exception {
        if (userInputs.isEmpty())
            throw new Exception("Wait for user input");
        program[parseOutputPosition(parameterMode, 1, opIndex + 1)] = userInputs.poll();
        return opIndex + 2;
    }

    private int opOutput(long parameterMode, int opIndex, Queue<Long> outputs) {
        Stack<Long> parameters = parseParameters(parameterMode, 1, opIndex + 1);
        outputs.offer(parameters.pop());
        return opIndex + 2;
    }

    private int opJumpIfTrue(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        int possibleAddress = Math.toIntExact(parameters.pop());
        return (parameters.pop() != 0) ? possibleAddress : opIndex + 3;
    }

    private int opJumpIfFalse(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        int possibleAddress = Math.toIntExact(parameters.pop());
        return (parameters.pop() == 0) ? possibleAddress : opIndex + 3;
    }

    private int opLessThan(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[parseOutputPosition(parameterMode, 100, opIndex + 3)] = (parameters.pop().compareTo(parameters.pop()) > 0) ? 1 : 0;
        return opIndex + 4;
    }

    private int opEquals(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 2, opIndex + 1);
        program[parseOutputPosition(parameterMode, 100, opIndex + 3)] = (parameters.pop().equals(parameters.pop())) ? 1 : 0;
        return opIndex + 4;
    }

    private int opMoveRelativeBase(long parameterMode, int opIndex) {
        Stack<Long> parameters = parseParameters(parameterMode, 1, opIndex + 1);
        //int i = parseOutputPosition(parameterMode, 1, opIndex + 1);
        relativeBase += parameters.pop();
        return opIndex + 2;
    }

    private Stack<Long> parseParameters(long parametersMode, int size, int startIndex) {
        Stack<Long> parameters = new Stack<>();
        long mode = parametersMode;
        for (int i = 0; i < size; i++) {
            switch (Long.valueOf(mode % 10).toString()) {
                case "0":
                    parameters.push(program[(int) program[startIndex + i]]);
                    break;
                case "1":
                    parameters.push(program[startIndex + i]);
                    break;
                case "2":
                    parameters.push(program[(int) (program[startIndex + i]) + relativeBase]);
                    break;
            }
            mode /= 10;
        }
        return parameters;
    }

    private int parseOutputPosition(long parametersMode, int divisor, int index) {
        int resultAddress = 0;
        switch (Long.valueOf(parametersMode / divisor % 10).toString()) {
            case "0":
                resultAddress = (int) program[index];
                break;
            case "1":
                throw new RuntimeException();
            case "2":
                resultAddress = (int) program[index] + relativeBase;
                break;
        }
        return resultAddress;
    }
}