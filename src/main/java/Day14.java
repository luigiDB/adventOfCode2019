import java.math.BigInteger;
import java.util.*;

public class Day14 {


    private final Map<Resource, List<Resource>> dependencies;
    private List<Resource> totalOverproduction;

    public Day14(String input) {
        dependencies = buildDependencies(input);
    }

    public Resource oreRequiredForFuel(int fuelAmount) {
        Resource fuel = new Resource("FUEL", new BigInteger(String.valueOf(fuelAmount)));

        List<Resource> productionLine = initEmptyResourceList(dependencies);
        totalOverproduction = initEmptyResourceList(dependencies);

        Set<Resource> round = Set.of(fuel);
        while (!(round.size() == 1 && round.stream().anyMatch(x -> x.getName().equals("ORE")))) {
            Set<Resource> nextRound = new HashSet<>();

            for (Resource resource : round) {
                if (resource.getName().equals("ORE"))
                    continue;

                Resource oldOverproduction = totalOverproduction.stream().filter(x -> x.equals(resource)).findFirst().get();
                BigInteger howManyToProduce = resource.getQty().subtract(oldOverproduction.getQty());

                Map.Entry<Resource, List<Resource>> reaction = dependencies.entrySet().stream().filter(x -> x.getKey().getName().equals(resource.getName())).findFirst().get();

                BigInteger numberOfReactions = howManyToProduce.add(reaction.getKey().getQty()).subtract(BigInteger.ONE).divide(reaction.getKey().getQty());

                for (Resource qtyUpdate : reaction.getValue()) {
                    BigInteger newProductionLots = qtyUpdate.getQty().multiply(numberOfReactions);
                    productionLine.get(productionLine.indexOf(qtyUpdate)).sumQty(newProductionLots);
                    if (nextRound.contains(qtyUpdate)) {
                        nextRound.stream().filter(x -> x.getName().equals(qtyUpdate.getName())).findFirst().get().sumQty(newProductionLots);
                    } else {
                        nextRound.add(new Resource(qtyUpdate.getName(), newProductionLots));
                    }
                }

                oldOverproduction.setQty((reaction.getKey().getQty().multiply(numberOfReactions)).subtract(howManyToProduce));
            }
            round = nextRound;
        }

        return productionLine.stream().filter(x -> x.getName().equals("ORE")).findFirst().get();
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
        resource.sumQty(new BigInteger(s[0].trim()));
        return resource;
    }

    public String maximumAmountOfFuel() {
        BigInteger maximumOres = new BigInteger("1000000000000");

        int maximumI = 0;
        int i = Integer.MAX_VALUE / 2;
        int step = 4;
        while (step >= 1) {
            Resource ores = oreRequiredForFuel(i);
            System.out.println(i + "\t\t" + ores.toString());
            BigInteger distance = maximumOres.subtract(ores.getQty());
            if (distance.signum() > 0) {
                maximumI = Math.max(maximumI, i);
            }
            if (ores.getQty().compareTo(maximumOres) > 0) {
                i = i - (Integer.MAX_VALUE / step);
            } else {
                i = i + (Integer.MAX_VALUE / step);
            }
            step *= 2;
            if (step == 0) {
                step = 1;
            }
        }

        int counter = maximumI;
        while (true) {
            Resource ores = oreRequiredForFuel(counter);
            if (ores.getQty().compareTo(maximumOres) > 0)
                break;
            counter++;
        }

        return Integer.valueOf(counter-1).toString();
    }

    public class Resource {
        private String name;
        private BigInteger qty = BigInteger.ZERO;

        public Resource(String name) {
            this.name = name;
        }

        public Resource(String name, BigInteger qty) {
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

        public void setQty(BigInteger qty) {
            this.qty = qty;
        }

        public void sumQty(BigInteger qty) {
            this.qty = this.qty.add(qty);
        }

        public BigInteger getQty() {
            return qty;
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
