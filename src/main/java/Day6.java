import java.util.*;

public class Day6 {

    public int evaluateOrbits(String input) {
        Map<String, Node> nodes = new HashMap<>();

        for (String orbit : input.split("\n")) {
            String[] planets = orbit.split("\\)");
            nodes.computeIfAbsent(planets[0], k -> new Node(k));
            nodes.computeIfAbsent(planets[1], k -> new Node(k));
            nodes.get(planets[0]).addOrbitingPlanets(nodes.get(planets[1]));
        }

        Node com = nodes.get("COM");
        populateLevel(com, 0);

        return nodes.values().stream().map(x -> x.getLevel()).reduce(0, (a, b) -> (a + b));
    }

    private void populateLevel(Node node, int level) {
        node.setLevel(level);
        for (Node son : node.getSons()) {
            populateLevel(son, level + 1);
        }
    }

    public int evaluateOrbitsBetweenMeAndSanta(String input) {
        Map<String, Node> nodes = new HashMap<>();
        Node youOrbiting = null;
        Node santaOrbiting = null;
        for (String orbit : input.split("\n")) {
            String[] planets = orbit.split("\\)");
            nodes.computeIfAbsent(planets[0], k -> new Node(k));
            nodes.computeIfAbsent(planets[1], k -> new Node(k));
            nodes.get(planets[0]).addOrbitingPlanets(nodes.get(planets[1]));

            if (planets[1].equals("YOU")) {
                youOrbiting = nodes.get(planets[0]);
            }

            if (planets[1].equals("SAN")) {
                santaOrbiting = nodes.get(planets[0]);
            }
        }

        Node com = nodes.get("COM");
        populateLevel(com, 0);

        Node lca = lowestCommonAncestor(com, youOrbiting, santaOrbiting);

        int distance = youOrbiting.getLevel() + santaOrbiting.getLevel() - lca.getLevel() * 2;
        return distance;
    }

    private Node lowestCommonAncestor(Node root, Node one, Node two) {
        List<Node> pathToOne = new ArrayList<>();
        findPath(root, one, pathToOne);
        List<Node> pathToTwo = new ArrayList<>();
        findPath(root, two, pathToTwo);

        int i = 0;
        for (; i < pathToOne.size() && i < pathToTwo.size(); i++) {
            if (!pathToOne.get(i).equals(pathToTwo.get(i)))
                break;
        }

        return pathToOne.get(i - 1);
    }

    private boolean findPath(Node root, Node end, List<Node> path) {
        path.add(root);
        if(root.equals(end))
            return true;
        for (Node son : root.getSons()) {
            if (findPath(son, end, path))
                return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    private class Node {
        String name;
        int level;
        List<Node> sons;

        public Node(String name) {
            this.name = name;
            sons = new ArrayList<>();
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public void addOrbitingPlanets(Node orbitingPlanet) {
            sons.add(orbitingPlanet);
        }

        public List<Node> getSons() {
            return sons;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
