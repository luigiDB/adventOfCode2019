import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Day14 {

    public int oreRequiredForOneFuel(String input) {
        Map<Resource, List<Resource>> dependencies = buildDependencies(input);

        Resource fuel = dependencies.keySet().stream().filter(x -> x.getName().equals("FUEL")).findFirst().get();

        List<Resource> productionLine = initEmptyResourceList(dependencies);
        List<Resource> totalOverproduction = initEmptyResourceList(dependencies);

        Set<Resource> round = Set.of(fuel);
        while (!(round.size() == 1 && round.stream().anyMatch(x -> x.getName().equals("ORE")))) {
            Set<Resource> nextRound = new HashSet<>();

            for (Resource resource : round) {
                if (resource.getName().equals("ORE"))
                    continue;

                Resource oldOverproduction = totalOverproduction.stream().filter(x -> x.equals(resource)).findFirst().get();
                int howManyToProduce = resource.getQty() - oldOverproduction.getQty();
                if (howManyToProduce < 0) {
                    oldOverproduction.setQty(Math.abs(howManyToProduce));
                    continue;
                }

                Map.Entry<Resource, List<Resource>> reaction = dependencies.entrySet().stream().filter(x -> x.getKey().getName().equals(resource.getName())).findFirst().get();

                int numberOfReactions = (howManyToProduce + reaction.getKey().getQty() - 1) / reaction.getKey().getQty();

                for (Resource qtyUpdate : reaction.getValue()) {
                    int newProductionLots = qtyUpdate.getQty() * numberOfReactions;
                    productionLine.get(productionLine.indexOf(qtyUpdate)).sumQty(newProductionLots);
                    if (nextRound.contains(qtyUpdate)) {
                        nextRound.stream().filter(x -> x.getName().equals(qtyUpdate.getName())).findFirst().get().sumQty(newProductionLots);
                    } else {
                        nextRound.add(new Resource(qtyUpdate.getName(), newProductionLots));
                    }
                }

                oldOverproduction.setQty((reaction.getKey().getQty() * numberOfReactions) - howManyToProduce);
            }
            round = nextRound;
        }

        return productionLine.stream().filter(x -> x.getName().equals("ORE")).findFirst().get().getQty();
    }

    private List<Resource> initEmptyResourceList(Map<Resource, List<Resource>> input) {
        Set<Resource> resourceSet = new HashSet<>();
        for (Map.Entry<Resource, List<Resource>> reaction : input.entrySet()) {
            resourceSet.add(reaction.getKey());
            resourceSet.addAll(reaction.getValue());
        }
        LinkedList<Resource> resourceList = new LinkedList<>();
        for (Resource resource : resourceSet) {
            resourceList.add(
                    new Resource(resource.getName())
            );
        }
        return resourceList;
    }

    private Map<Resource, List<Resource>> buildDependencies(String input) {
        Map<Resource, List<Resource>> dependenciesMap = new HashMap<>();
        for (String reaction : input.split("\n")) {
            String[] sections = reaction.split("=>");
            List<Resource> dependencies = new LinkedList<>();
            for (String dep : sections[0].split(",")) {
                dependencies.add(formatResource(dep));
            }
            dependenciesMap.put(formatResource(sections[1]), dependencies);
        }
        return dependenciesMap;
    }

    private Resource formatResource(String section) {
        String[] s = section.trim().split(" ");
        Resource resource = new Resource(s[1].trim());
        resource.sumQty(Integer.parseInt(s[0].trim()));
        return resource;
    }

    private class Resource {
        private String name;
        private int qty = 0;

        public Resource(String name) {
            this.name = name;
        }

        public Resource(String name, int qty) {
            this.name = name;
            this.qty = qty;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Resource resource = (Resource) o;
            return name.equals(resource.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public String getName() {
            return name;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public void sumQty(int qty) {
            this.qty += qty;
        }

        @Override
        public String toString() {
            return "Resource{" +
                    "name='" + name + '\'' +
                    ", qty=" + qty +
                    '}';
        }
    }
}
