import java.util.Queue;

public class Day5 {

    public int[] callComputer(String program, Queue<Integer> userInputs, Queue<Integer> outputs) {
        IntCodeComputer intCodeComputer = new IntCodeComputer(program);
        intCodeComputer.process(userInputs, outputs);
        return intCodeComputer.getProgram();
    }

}
