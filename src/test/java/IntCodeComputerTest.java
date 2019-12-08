import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

public class IntCodeComputerTest {
    private Day5 day5;
    private Queue<Integer> userInputs;
    private Queue<Integer> outputs;

    @Before
    public void setUp() throws Exception {
        day5 = new Day5();
        userInputs = new LinkedList<>();
        outputs = new LinkedList<>();
    }

    @Test
    public void opCode3Test() {
        userInputs.add(8);
        int[] program = day5.callComputer("3,0,99", userInputs, outputs);
        Assert.assertEquals(8, program[0]);
    }

    @Test
    public void opCode4Test() {
        day5.callComputer("4,2,99", userInputs, outputs);
        Assert.assertTrue(outputs.size() == 1);
        Assert.assertEquals(Integer.valueOf(99), outputs.poll());
    }

    @Test
    public void opCode3And4Test() {
        userInputs.add(8);
        int[] program = day5.callComputer("3,0,4,0,99", userInputs, outputs);
        Assert.assertEquals(8, program[0]);
    }

    @Test
    public void parameterModeTest() {
        int[] program = day5.callComputer("1002,4,3,0,99", userInputs, outputs);
        Assert.assertEquals(297, program[0]);
    }

    @Test
    public void accepatanceFor5_6_7_8() {
        //Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(8);
        day5.callComputer("3,9,8,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1), outputs.poll());

        userInputs.offer(10);
        day5.callComputer("3,9,8,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(0), outputs.poll());

        //Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(7);
        day5.callComputer("3,9,7,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1), outputs.poll());

        userInputs.offer(10);
        day5.callComputer("3,9,7,9,10,9,4,9,99,-1,8", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(0), outputs.poll());

        //Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(8);
        day5.callComputer("3,3,1108,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1), outputs.poll());

        userInputs.offer(10);
        day5.callComputer("3,3,1108,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(0), outputs.poll());

        //Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0 (if it is not)
        userInputs.offer(7);
        day5.callComputer("3,3,1107,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1), outputs.poll());

        userInputs.offer(10);
        day5.callComputer("3,3,1107,-1,8,3,4,3,99", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(0), outputs.poll());

        //Here are some jump tests that take an input, then output 0 if the input was zero or 1 if the input was non-zero:
        //using position mode
        userInputs.offer(0);
        day5.callComputer("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(0), outputs.poll());

        userInputs.offer(42);
        day5.callComputer("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1), outputs.poll());

        //using immediate mode
        userInputs.offer(0);
        day5.callComputer("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(0), outputs.poll());

        userInputs.offer(42);
        day5.callComputer("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1), outputs.poll());
    }

    @Test
    public void globalAcceptance() {
        String program = "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99";
        //The program will then output 999 if the input value is below 8, output 1000 if the input value is equal to 8,
        // or output 1001 if the input value is greater than 8
        userInputs.offer(7);
        day5.callComputer(program, userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(999), outputs.poll());

        userInputs.offer(8);
        day5.callComputer(program, userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1000), outputs.poll());

        userInputs.offer(9);
        day5.callComputer(program, userInputs, outputs);
        Assert.assertEquals(Integer.valueOf(1001), outputs.poll());
    }

}