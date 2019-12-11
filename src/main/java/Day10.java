import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Day10 {

    public int maxVisibleAsteroids(String[] map) {
        Set<Pair<Double, Double>> asteroids = getAsteroids(map);
        Pair<Double, Double> baseAsteroid = null;

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
            if (visibilityLines.size() > maxVisibility) {
                maxVisibility = Math.max(maxVisibility, visibilityLines.size());
                baseAsteroid = asteroid;
            }
        }

        System.out.println(baseAsteroid);
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

    public Pair<Double, Double> get200Asteroid(String[] map, Pair<Double, Double> laserPlace, int pos) {
        Set<Pair<Double, Double>> asteroids = getAsteroids(map);

        Map<LineDirection, List<Pair<Double, Pair<Double, Double>>>> visibilityLines = new HashMap<>();

        for (Pair<Double, Double> asteroid : asteroids) {
            if (asteroid.equals(laserPlace))
                continue;
            double yDistance = laserPlace.getRight() - asteroid.getRight();
            double xDistance = laserPlace.getLeft() - asteroid.getLeft();
            LineDirection direction = new LineDirection(
                    yDistance / xDistance,
                    xDistance > 0 ? 1 : -1,
                    yDistance > 0 ? 1 : -1
            );
            visibilityLines.compute(direction,
                    (k, v) -> {
                        if (v == null) {
                            v = new LinkedList<>();
                        }
                        double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
                        v.add(Pair.of(distance, asteroid));
                        return v;
                    });
        }

        for (List<Pair<Double, Pair<Double, Double>>> list : visibilityLines.values()) {
            list.sort(Comparator.comparingDouble(Pair::getLeft));
        }

        List<LineDirection> directions = new LinkedList<>();
        directions.addAll(visibilityLines.keySet());
        directions.sort(Comparator.comparingInt(LineDirection::getQuadrant).thenComparing(LineDirection::getCoefficient));
        Map<Integer, List<LineDirection>> collect = directions.stream().collect(Collectors.groupingBy(LineDirection::getQuadrant));

        directions = new LinkedList<>();
        collect.get(0).sort(Comparator.comparing(x -> x.getCoefficient()));
        directions.addAll(collect.get(0));
        collect.get(1).sort(Comparator.comparing(x -> Math.abs(x.getCoefficient())));
        directions.addAll(collect.get(1));
        collect.get(2).sort(Comparator.comparing(x -> x.getCoefficient()));
        directions.addAll(collect.get(2));
        collect.get(3).sort(Comparator.comparing(x -> Math.abs(x.getCoefficient())));
        directions.addAll(collect.get(3));

        LineDirection infinityDirection = new LineDirection(Double.POSITIVE_INFINITY, -1, 1);
        boolean removed = directions.remove(infinityDirection);
        if(removed)
            directions.add(0, infinityDirection);

        for (LineDirection dir : directions)
            System.out.println(dir.toString() + "\t\t" + visibilityLines.get(dir).toString());

        List<Pair<Double, Pair<Double, Double>>> pairs = visibilityLines.get(directions.get(pos));

        return pairs.get(0).getRight();
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

        public double getCoefficient() {
            return coefficient;
        }

        public int getQuadrant() {
            if (xDirection == '+' && yDirection == '+')
                return 3;
            if (xDirection == '+' && yDirection == '-')
                return 2;
            if (xDirection == '-' && yDirection == '-')
                return 1;
            if (xDirection == '-' && yDirection == '+')
                return 0;
            throw new RuntimeException();
        }

        @Override
        public String toString() {
            return xDirection +
                    "  " + yDirection +
                    "  " + coefficient;
        }
    }


}
