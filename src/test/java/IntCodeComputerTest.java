import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class IntCodeComputerTest {
    private CallComputerUtility computerWrapper;
    private Queue<Long> userInputs;
    private Queue<Long> outputs;

    @Before
    public void setUp() throws Exception {
        computerWrapper = new CallComputerUtility();
        userInputs = new LinkedList<>();
        outputs = new LinkedList<>();
    }

    private class CallComputerUtility {

        public long[] callComputer(String program, Queue<Long> userInputs, Queue<Long> outputs) {
            IntCodeComputer intCodeComputer = new IntCodeComputer(program);
            intCodeComputer.process(userInputs, outputs);
            return intCodeComputer.getProgram();
        }
    }

    @Test
    public void opCode3Test() {
        userInputs.add(8L);
        long[] program = computerWrapper.callComputer("3,0,99", userInputs, outputs);
        Assert.assertEquals(8, program[0]);

        userInputs.add(8L);
        program = computerWrapper.callComputer("203,0,99", userInputs, outputs);
        Assert.assertEquals(8, program[0]);

    }

    @Test
    public void opCode4Test() {
        computerWrapper.callComputer("4,2,99", userInputs, outputs);
        Assert.assertTrue(outputs.size() == 1);
        Assert.assertEquals(Long.valueOf(99), outputs.poll());
    }

    @Test
    public void opCode3And4Test() {
        userInputs.add(8L);
        long[] program = computerWrapper.callComputer("3,0,4,0,99", userInputs, outputs);
        Assert.assertEquals(8, program[0]);
    }

    @Test
    public void parameterModeTest() {
        long[] program = computerWrapper.callComputer("1002,4,3,0,99", userInputs, outputs);
        Assert.assertEquals(297, program[0]);
    }

    @Test
    public void accepatanceFor5_6_7_8() {
        //Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(8L);
        computerWrapper.callComputer("3,9,8,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1), outputs.poll());

        userInputs.offer(10L);
        computerWrapper.callComputer("3,9,8,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(0), outputs.poll());

        //Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(7L);
        computerWrapper.callComputer("3,9,7,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1), outputs.poll());

        userInputs.offer(10L);
        computerWrapper.callComputer("3,9,7,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(0), outputs.poll());

        //Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(8L);
        computerWrapper.callComputer("3,3,1108,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1), outputs.poll());

        userInputs.offer(10L);
        computerWrapper.callComputer("3,3,1108,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(0), outputs.poll());

        //Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(7L);
        computerWrapper.callComputer("3,3,1107,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1), outputs.poll());

        userInputs.offer(10L);
        computerWrapper.callComputer("3,3,1107,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(0), outputs.poll());

        //Here are some jump tests that take an input, then output 0 if the input was zero or 1 if the input was non-zero:
        //using position mode
        userInputs.offer(0L);
        computerWrapper.callComputer("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(0), outputs.poll());

        userInputs.offer(42L);
        computerWrapper.callComputer("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1), outputs.poll());

        //using immediate mode
        userInputs.offer(0L);
        computerWrapper.callComputer("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(0), outputs.poll());

        userInputs.offer(42L);
        computerWrapper.callComputer("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1), outputs.poll());
    }

    @Test
    public void globalAcceptance() {
        String program = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
        //The program will then output 999 if the input value is below 8, output 1000 if the input value is equal to 8,
        // or output 1001 if the input value is greater than 8
        userInputs.offer(7L);
        computerWrapper.callComputer(program, userInputs, outputs);
        Assert.assertEquals(Long.valueOf(999), outputs.poll());

        userInputs.offer(8L);
        computerWrapper.callComputer(program, userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1000), outputs.poll());

        userInputs.offer(9L);
        computerWrapper.callComputer(program, userInputs, outputs);
        Assert.assertEquals(Long.valueOf(1001), outputs.poll());
    }

    @Test
    public void testParameterMode2AndOpCode9() {
        computerWrapper.callComputer("109,5,204,4,99,0,0,0,0,42", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(42), outputs.poll());
    }

    @Test
    public void testSupportForLrgeNumbers() {
        computerWrapper.callComputer("104,1125899906842624,99", userInputs, outputs);
        Assert.assertEquals(Long.valueOf("1125899906842624"), outputs.poll());

        computerWrapper.callComputer("1102,34915192,34915192,7,4,7,99,0", userInputs, outputs);
        Assert.assertTrue(outputs.poll().toString().length() == 16);
    }

    @Test
    public void quineTest() {
        List<Long> programNumbers = List.of(109L, 1L, 204L, -1L, 1001L, 100L, 1L, 100L, 1008L, 100L, 16L, 101L, 1006L, 101L, 0L, 99L);
        String program = programNumbers.stream().map(x -> x.toString()).collect(Collectors.joining(","));
        IntCodeComputer intCodeComputer = new IntCodeComputer(program, 10);
        intCodeComputer.process(userInputs, outputs);
        Assert.assertEquals(programNumbers.size(), outputs.size());
        for (int i = 0; i < programNumbers.size(); i++) {
            Assert.assertEquals(programNumbers.get(i), outputs.poll());
        }
    }

    @Test
    public void testParameterMode2() {
        computerWrapper.callComputer("109,1,9,9,209,9,99,0,0,2,0,0,1,0,0", userInputs, outputs);
    }

    @Test
    public void testParameterMode3() {
        computerWrapper.callComputer("109,1,21101,22,20,0,4,1,99", userInputs, outputs);
        Assert.assertEquals(Long.valueOf(42), outputs.poll());
    }
}