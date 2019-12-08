import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Day3 {
    public static int es1(String wire1, String wire2) {
        String[] wire1Moves = wire1.split(",");
        String[] wire2Moves = wire2.split(",");
        Set<PairWithDistance<Integer, Integer>> wire1OccupiedCells = findPath(wire1Moves);
        Set<PairWithDistance<Integer, Integer>> wire2OccupiedCells = findPath(wire2Moves);

        wire1OccupiedCells.retainAll(wire2OccupiedCells);

        wire1OccupiedCells.remove(Pair.of(0, 0));
        int minDist = Integer.MAX_VALUE;
        for (Pair<Integer, Integer> pair : wire1OccupiedCells) {
            minDist = Math.min(minDist, manhattanDistance(Pair.of(0, 0), pair));
        }
        return minDist;
    }

    private static int manhattanDistance(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        return Math.abs(start.getLeft() - end.getLeft()) + Math.abs(start.getRight() - end.getRight());
    }

    private static Set<PairWithDistance<Integer, Integer>> findPath(String[] wireMoves) {
        HashSet<PairWithDistance<Integer, Integer>> occupiedCells = new HashSet<>();

        PairWithDistance<Integer, Integer> currentPos = PairWithDistance.of(0, 0, 0);
        for (String move : wireMoves) {
            char direction = move.charAt(0);
            int distance = Integer.parseInt(move.substring(1));
            for (int i = 0; i < distance; i++) {
                switch (direction) {
                    case 'R':
                        currentPos = PairWithDistance.of(currentPos.getLeft() + 1,
                                currentPos.getRight(),
                                currentPos.getDistance() + 1);
                        break;
                    case 'U':
                        currentPos = PairWithDistance.of(currentPos.getLeft(),
                                currentPos.getRight() + 1,
                                currentPos.getDistance() + 1);
                        break;
                    case 'L':
                        currentPos = PairWithDistance.of(currentPos.getLeft() - 1,
                                currentPos.getRight(),
                                currentPos.getDistance() + 1);
                        break;
                    case 'D':
                        currentPos = PairWithDistance.of(currentPos.getLeft(),
                                currentPos.getRight() - 1,
                                currentPos.getDistance() + 1);
                        break;
                }
                occupiedCells.add(currentPos);
            }
        }
        return occupiedCells;
    }

    private static List<PairWithDistance<Integer, Integer>> findPathWithRepetions(String[] wireMoves) {
        List<PairWithDistance<Integer, Integer>> occupiedCells = new ArrayList<>();

        PairWithDistance<Integer, Integer> currentPos = PairWithDistance.of(0, 0, 0);
        for (String move : wireMoves) {
            char direction = move.charAt(0);
            int distance = Integer.parseInt(move.substring(1));
            for (int i = 0; i < distance; i++) {
                switch (direction) {
                    case 'R':
                        currentPos = PairWithDistance.of(currentPos.getLeft() + 1,
                                currentPos.getRight(),
                                currentPos.getDistance() + 1);
                        break;
                    case 'U':
                        currentPos = PairWithDistance.of(currentPos.getLeft(),
                                currentPos.getRight() + 1,
                                currentPos.getDistance() + 1);
                        break;
                    case 'L':
                        currentPos = PairWithDistance.of(currentPos.getLeft() - 1,
                                currentPos.getRight(),
                                currentPos.getDistance() + 1);
                        break;
                    case 'D':
                        currentPos = PairWithDistance.of(currentPos.getLeft(),
                                currentPos.getRight() - 1,
                                currentPos.getDistance() + 1);
                        break;
                }
                occupiedCells.add(currentPos);
            }
        }
        return occupiedCells;
    }

    public static int es2(String wire1, String wire2) {
        String[] wire1Moves = wire1.split(",");
        String[] wire2Moves = wire2.split(",");
        List<PairWithDistance<Integer, Integer>> wire1OccupiedCells = findPathWithRepetions(wire1Moves);
        List<PairWithDistance<Integer, Integer>> wire2OccupiedCells = findPathWithRepetions(wire2Moves);

        wire1OccupiedCells.retainAll(wire2OccupiedCells);
        wire2OccupiedCells.retainAll(wire1OccupiedCells);
        wire1OccupiedCells.sort(Comparator.comparingInt(PairWithDistance::getDistance));
        wire2OccupiedCells.sort(Comparator.comparingInt(PairWithDistance::getDistance));

        for (PairWithDistance position : wire1OccupiedCells) {
            System.out.println(position.toString());
        }
        System.out.println("-");
        for (PairWithDistance position : wire2OccupiedCells) {
            System.out.println(position.toString());
        }

        int minSteps = Integer.MAX_VALUE;
        for (PairWithDistance position : wire1OccupiedCells) {
            for (PairWithDistance cross : wire2OccupiedCells) {
                if (position.equals(cross))
                    minSteps = Math.min(minSteps, position.getDistance() + cross.getDistance());
            }
        }
        return minSteps;
    }

    public static final class PairWithDistance<L, R> extends Pair<L, R> {
        private static final PairWithDistance NULL = of((Object) null, (Object) null, 0);
        private static final long serialVersionUID = 4954918890077093861L;
        public final L left;
        public final R right;
        public final int distance;

        public static <L, R> PairWithDistance<L, R> nullPair() {
            return NULL;
        }

        public static <L, R> PairWithDistance<L, R> of(L left, R right, int distance) {
            return new PairWithDistance(left, right, distance);
        }

        public PairWithDistance(L left, R right, int distance) {
            this.left = left;
            this.right = right;
            this.distance = distance;
        }

        public L getLeft() {
            return this.left;
        }

        public R getRight() {
            return this.right;
        }

        public int getDistance() {
            return this.distance;
        }

        public R setValue(R value) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return "(" + this.getLeft() + ',' + this.getRight() + ',' + this.getDistance() + ')';
        }

        public String toString(String format) {
            return String.format(format, this.getLeft(), this.getRight(), this.getDistance());
        }
    }
}
