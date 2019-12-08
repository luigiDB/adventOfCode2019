public class Day2 {

    public static int es1(String input) {
        int[] program = stringToIntArray(input);

        program[1] = 12;
        program[2] = 2;

        return intCodeComputer(program);
    }

    public static int[] stringToIntArray(String input) {
        String[] split = input.split(",");
        int[] program = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            program[i] = Integer.parseInt(split[i]);
        }
        return program;
    }

    private static int intCodeComputer(int[] inputProgram) {
        int[] program = new int[inputProgram.length];
        System.arraycopy(inputProgram, 0, program, 0, inputProgram.length);
        int programIndex = 0;
        while (program[programIndex] != 99) {
            switch (program[programIndex]) {
                case 1:
                    program[program[programIndex + 3]] = program[program[programIndex + 1]] + program[program[programIndex + 2]];
                    break;
                case 2:
                    program[program[programIndex + 3]] = program[program[programIndex + 1]] * program[program[programIndex + 2]];
                    break;
                default:
                    throw new RuntimeException();
            }
            programIndex += 4;
        }
        return program[0];
    }

    public static int es2(String input, int target) {
        int[] program = stringToIntArray(input);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                program[1] = i;
                program[2] = j;
                if (intCodeComputer(program) == target)
                    return 100 * program[1] + program[2];
            }
        }
        throw new RuntimeException();
    }
}
