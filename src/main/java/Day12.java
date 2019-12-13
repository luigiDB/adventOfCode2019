import java.math.BigInteger;
import java.util.List;

public class Day12 {

    private void updateGravity(Moon[] system) {
        for (int i = 0; i < system.length; i++) {
            for (int j = i + 1; j < system.length; j++) {
                int[] posA = system[i].getPos();
                int[] posB = system[j].getPos();
                int[] velocityA = system[i].getVel();
                int[] velocityB = system[j].getVel();
                for (int k = 0; k < posA.length; k++) {
                    if (posA[k] < posB[k]) {
                        velocityA[k]++;
                        velocityB[k]--;
                    } else {
                        if (posA[k] > posB[k]) {
                            velocityA[k]--;
                            velocityB[k]++;
                        }
                    }
                }
                system[i].setVel(velocityA);
                system[j].setVel(velocityB);
            }
        }
        for (Moon moon : system) {
            moon.updatePos();
        }
    }

    public Moon[] systemAtTime(List<int[]> staringPositions, int steps) {
        Moon[] system = initUniverse(staringPositions);
        for (int i = 0; i < steps; i++) {
            updateGravity(system);
        }
        return system;
    }

    private Moon[] initUniverse(List<int[]> staringPositions) {
        Moon[] system = new Moon[staringPositions.size()];
        for (int i = 0; i < staringPositions.size(); i++) {
            system[i] = new Moon(staringPositions.get(i));
        }
        return system;
    }

    public String universeCycle(List<int[]> staringPositions) {
        Moon[] system = initUniverse(staringPositions);
        String bigBang = universeIdentifier(system);

        BigInteger universeCounter = BigInteger.ZERO;
        while (true) {
            updateGravity(system);
            universeCounter = universeCounter.add(BigInteger.ONE);
            if (bigBang.equals(universeIdentifier(system))) {
                break;
            }
            if (universeCounter.mod(BigInteger.valueOf(1000000L)) == BigInteger.ZERO) {
                System.out.println(
                        "Counter: " + universeCounter.toString() +
                                "system: " + universeIdentifier(system)
                );
            }
        }

        return universeCounter.toString();
    }

    private String universeIdentifier(Moon[] system) {
        StringBuilder sb = new StringBuilder();
        for (Moon moon : system) {
            sb.append(moon.toString());
        }
        return sb.toString();
    }

    private boolean compareDirectionalStatus(Moon[] system, int direction, int[] reference) {
        for (int i = 0; i < system.length; i++) {
            if (reference[i] != system[i].getPos()[direction]) {
                return false;
            }
            if (system[i].getVel()[direction] != 0)
                return false;
        }
        return true;
    }

    public String universeCycleLcm(List<int[]> staringPositions) {
        Moon[] system = initUniverse(staringPositions);
        String bigBang = universeIdentifier(system);

        int[][] startingVectors = new int[3][4];
        for (int i = 0; i < staringPositions.size(); i++) {
            startingVectors[0][i] = staringPositions.get(i)[0];
            startingVectors[1][i] = staringPositions.get(i)[1];
            startingVectors[2][i] = staringPositions.get(i)[2];
        }

        BigInteger[] lcm = new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO};
        BigInteger universeCounter = BigInteger.ZERO;
        while (true) {
            updateGravity(system);

            universeCounter = universeCounter.add(BigInteger.ONE);
            if (bigBang.equals(universeIdentifier(system))) {
                break;
            }

            for (int i = 0; i < lcm.length; i++) {
                if (lcm[i] == BigInteger.ZERO) {
                    if (compareDirectionalStatus(system, i, startingVectors[i])) {
                        lcm[i] = universeCounter;
                    }
                }
            }

            if (!lcm[0].equals(BigInteger.ZERO) &&
                    !lcm[1].equals(BigInteger.ZERO) &&
                    !lcm[2].equals(BigInteger.ZERO)) {
                break;
            }


            if (universeCounter.mod(BigInteger.valueOf(1000000L)) == BigInteger.ZERO) {
                System.out.println(
                        "Counter: " + universeCounter.toString() +
                                "system: " + universeIdentifier(system)
                );
            }

        }

        return lcm(lcm(lcm[0], lcm[1]), lcm[2]).toString();
    }

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        BigInteger gcd = number1.gcd(number2);
        BigInteger absProduct = number1.multiply(number2).abs();
        return absProduct.divide(gcd);
    }
}

