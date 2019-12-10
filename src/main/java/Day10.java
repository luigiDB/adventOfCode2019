import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day10 {

    public int maxVisibleAsteroids(String[] map) {
        Set<Pair<Double, Double>> asteroids = getAsteroids(map);

        int maxVisibility = Integer.MIN_VALUE;

        for (Pair<Double, Double> asteroid : asteroids) {
            Set<LineDirection> visibilityLines = new HashSet<>();

            for (Pair<Double, Double> otherAsteroid : asteroids) {
                if (asteroid.equals(otherAsteroid))
                    continue;
                visibilityLines.add(new LineDirection(
                        (otherAsteroid.getRight() - asteroid.getRight()) / (otherAsteroid.getLeft() - asteroid.getLeft()),
                        otherAsteroid.getLeft() - asteroid.getLeft(),
                        otherAsteroid.getRight() - asteroid.getRight()
                ));
            }
            maxVisibility = Math.max(maxVisibility, visibilityLines.size());
            System.out.println(asteroid.toString() + "  -  " + visibilityLines.size());
        }

        return maxVisibility;
    }

    private Set<Pair<Double, Double>> getAsteroids(String[] map) {
        Set<Pair<Double, Double>> asteroids = new HashSet<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length(); j++) {
                if (map[i].charAt(j) == '#') {
                    asteroids.add(Pair.of((double) j, (double) i));
                }
            }
        }
        return asteroids;
    }

    private class LineDirection {
        private double coefficient;
        private char xDirection;
        private char yDirection;

        public LineDirection(double coefficient, double xDirection, double yDirection) {
            this.coefficient = coefficient;
            this.xDirection = (xDirection >= 0) ? '+' : '-';
            this.yDirection = (yDirection >= 0) ? '+' : '-';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LineDirection that = (LineDirection) o;
            return Double.compare(that.coefficient, coefficient) == 0 &&
                    xDirection == that.xDirection &&
                    yDirection == that.yDirection;
        }

        @Override
        public int hashCode() {
            return Objects.hash(coefficient, xDirection, yDirection);
        }
    }
}
