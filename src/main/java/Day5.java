import java.util.Queue;

public class Day5 {

    public long[] callComputer(String program, Queue<Long> userInputs, Queue<Long> outputs) {
        IntCodeComputer intCodeComputer = new IntCodeComputer(program);
        intCodeComputer.process(userInputs, outputs);
        return intCodeComputer.getProgram();
    }

}
