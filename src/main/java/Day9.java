import java.util.Queue;

public class Day9 {

    public void callComputer(String program, Queue<Long> userInputs, Queue<Long> outputs) {
        IntCodeComputer intCodeComputer = new IntCodeComputer(program);
        intCodeComputer.process(userInputs, outputs);
    }
}
