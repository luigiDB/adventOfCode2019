import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Day15 {

    private final IntCodeComputer computer;
    private final int[][] env;
    private final Queue<Long> input;
    private final Queue<Long> output;
    private int[] currentPos;
    private final int[] directions = new int[]{-1, 0, 1, 0, -1};
    private final int[] startingPoint;

    public Day15(String program) {
        this.computer = new IntCodeComputer(program, 100);
        input = new LinkedList<>();
        output = new LinkedList<>();
        env = new int[50][50];
        currentPos = new int[]{25, 25};
        startingPoint = new int[]{25, 25};
    }

    public void exploreEnv() {
        while (true) {
            Pair<int[], int[]> pair = null;
            try {
                pair = nextStepWithSideEffect();
            } catch (RuntimeException e) {
                //to new way to move
                System.out.println(e);
                break;
            }
            input.offer(directionToNumber(pair.getLeft()));

            computer.process(input, output);

            Long cellContent = output.poll();
            assert cellContent != null;
            if (cellContent == 0L) {//wall
                env[pair.getRight()[0]][pair.getRight()[1]] = 2;
            } else if (cellContent == 1L) {//movement
                env[pair.getRight()[0]][pair.getRight()[1]] = 1;
                currentPos = pair.getRight();
            } else if (cellContent == 2L) {//oxygen
                env[pair.getRight()[0]][pair.getRight()[1]] = 3;
                currentPos = pair.getRight();
            }
        }
        printEnv();
        printRawEnv();
    }

    private Pair<int[], int[]> nextStepWithSideEffect() {
        for (int i = 0; i < directions.length - 1; i++) {
            int[] newPos = new int[]{
                    currentPos[0] + directions[i],
                    currentPos[1] + directions[i + 1]
            };
            if (env[newPos[0]][newPos[1]] == 0) {
                return Pair.of(
                        new int[]{directions[i], directions[i + 1]},
                        newPos);
            }
        }
        //no new discovery possible
        //go to an old one and ban this position
        for (int i = 0; i < directions.length - 1; i++) {
            int[] newPos = new int[]{
                    currentPos[0] + directions[i],
                    currentPos[1] + directions[i + 1]
            };
            if (env[newPos[0]][newPos[1]] == 1) {
                //side effect
                switch (env[currentPos[0]][currentPos[1]]) {
                    case 1:
                        env[currentPos[0]][currentPos[1]] = 4;
                        break;
                    case 3:
                        env[currentPos[0]][currentPos[1]] = 5;
                        break;
                    default:
                        throw new RuntimeException("I'm compenetrated with something at " + currentPos[0] + " " + currentPos[1]);
                }
                return Pair.of(
                        new int[]{directions[i], directions[i + 1]},
                        newPos);
            }
        }
        throw new RuntimeException("I'm stuck at " + currentPos[0] + " " + currentPos[1]);
    }

    private Set<Pair<Integer, Integer>> nextStep(Pair<Integer, Integer> curr) {
        Set<Pair<Integer, Integer>> possibleSteps = new HashSet<>();
        for (int i = 0; i < directions.length - 1; i++) {
            Pair<Integer, Integer> newPos = Pair.of(
                    curr.getLeft() + directions[i],
                    curr.getRight() + directions[i + 1]
            );
            if (env[newPos.getLeft()][newPos.getRight()] != 2) {
                possibleSteps.add(newPos);
            }
        }
        return possibleSteps;
    }

    public int minStepsToReachOxygen() {
        PriorityQueue<Pair<Integer, Integer>> priorityQueue = new PriorityQueue<>();
        Map<Pair<Integer, Integer>, Integer> distance = new HashMap<>();

        priorityQueue.add(Pair.of(startingPoint[0], startingPoint[1]));
        distance.put(Pair.of(startingPoint[0], startingPoint[1]), 0);

        while (!priorityQueue.isEmpty()) {
            Pair<Integer, Integer> poll = priorityQueue.poll();

            Set<Pair<Integer, Integer>> nexts = nextStep(poll);
            for (Pair<Integer, Integer> next : nexts) {
                if (distance.getOrDefault(poll, Integer.MAX_VALUE) + 1 < distance.getOrDefault(next, Integer.MAX_VALUE)) {
                    distance.compute(next, (k, v) -> distance.getOrDefault(poll, Integer.MAX_VALUE) + 1);
                    priorityQueue.add(next);
                }
            }
        }

        for (int i = 0; i < env.length; i++) {
            for (int j = 0; j < env[i].length; j++) {
                if (env[i][j] == 3 || env[i][j] == 5)
                    return distance.get(Pair.of(i, j));
            }
        }

        throw new RuntimeException();
    }

    private long directionToNumber(int[] direction) {
        if (direction[0] == directions[0] && direction[1] == directions[1])
            return 3;
        if (direction[0] == directions[1] && direction[1] == directions[2])
            return 1;
        if (direction[0] == directions[2] && direction[1] == directions[3])
            return 4;
        if (direction[0] == directions[3] && direction[1] == directions[4])
            return 2;
        throw new RuntimeException();
    }

    private void printEnv() {
        for (int i = 0; i < env.length; i++) {
            for (int j = 0; j < env[i].length; j++) {
                if (i == currentPos[0] && j == currentPos[1])
                    System.out.print('D');
                else
                    System.out.print(charEquivalent(env[i][j]));
            }
            System.out.println(" ");
        }
    }

    private void printRawEnv() {
        for (int i = 0; i < env.length; i++) {
            for (int j = 0; j < env[i].length; j++) {
                if (i == currentPos[0] && j == currentPos[1])
                    System.out.print('D');
                else
                    System.out.print(env[i][j]);
            }
            System.out.println(" ");
        }
    }

    private char charEquivalent(int i) {
        char c = ' ';
        switch (i) {
            case 0:
                break;
            case 1:
                c = '.';
                break;
            case 2:
                c = '#';
                break;
            case 3:
            case 5:
                c = 'O';
                break;
            case 4:
                c = ' ';
                break;
        }
        return c;
    }
}
