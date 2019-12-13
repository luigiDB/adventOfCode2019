import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Day11 {


    public Pair<Set<Pair<Integer, Integer>>, Map<Pair<Integer, Integer>, Integer>> coloredPanels(String program, int startingColor) {
        IntCodeComputer computer = new IntCodeComputer(program, 10000);
        Queue<Long> userInputs = new LinkedList<>();
        Queue<Long> outputs = new LinkedList<>();

        Map<Pair<Integer, Integer>, Integer> tileMap = new HashMap<>();
        Set<Pair<Integer, Integer>> paintedTiles = new HashSet<>();

        Pair<Integer, Integer> currentTile = Pair.of(0, 0);
        tileMap.put(currentTile, startingColor);
        Position currentPosition = new Position(currentTile, Direction.U);

        boolean finished = false;
        while (!finished) {
            int currentColor = tileMap.getOrDefault(currentPosition.getTile(), 0);
            userInputs.add((long) currentColor);
            computer.process(userInputs, outputs);
            if (outputs.size() == 0) {
                finished = true;
                continue;
            }

            int newColor = Math.toIntExact(outputs.poll());
            if (currentColor != newColor) {
                tileMap.put(currentPosition.getTile(), newColor);
                paintedTiles.add(currentPosition.getTile());
            }
            currentPosition.rotate(Math.toIntExact(outputs.poll()));
        }

        return Pair.of(paintedTiles, tileMap);
    }

    private enum Direction {
        U, L, D, R;
    }

    private class Position {
        Pair<Integer, Integer> tile;
        Direction direction;

        public Position(Pair<Integer, Integer> tile, Direction direction) {
            this.tile = tile;
            this.direction = direction;
        }

        public Pair<Integer, Integer> getTile() {
            return tile;
        }

        public void rotate(int rotation) {
            Direction newDir = newDir(rotation);
            switch (newDir) {
                case U:
                    tile = Pair.of(tile.getLeft(), tile.getRight() + 1);
                    break;
                case L:
                    tile = Pair.of(tile.getLeft() - 1, tile.getRight());
                    break;
                case D:
                    tile = Pair.of(tile.getLeft(), tile.getRight() - 1);
                    break;
                case R:
                    tile = Pair.of(tile.getLeft() + 1, tile.getRight());
                    break;
            }
            direction = newDir;
        }

        private Direction newDir(int rotation) {
            if (rotation == 0) {
                if (direction.equals(Direction.U)) {
                    return Direction.L;
                }
                if (direction.equals(Direction.L)) {
                    return Direction.D;
                }
                if (direction.equals(Direction.D)) {
                    return Direction.R;
                }
                if (direction.equals(Direction.R)) {
                    return Direction.U;
                }
            } else {
                if (direction.equals(Direction.U)) {
                    return Direction.R;
                }
                if (direction.equals(Direction.R)) {
                    return Direction.D;
                }
                if (direction.equals(Direction.D)) {
                    return Direction.L;
                }
                if (direction.equals(Direction.L)) {
                    return Direction.U;
                }
            }
            throw new RuntimeException();
        }
    }
}
