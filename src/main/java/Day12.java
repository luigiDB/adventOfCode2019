import java.util.LinkedList;
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
        for (Moon moon: system) {
            moon.updatePos();
        }
    }

    public Moon[] systemAtTime(List<int[]> staringPositions, int steps) {
        Moon[] system = new Moon[staringPositions.size()];
        for (int i = 0; i < staringPositions.size(); i++) {
            system[i] = new Moon(staringPositions.get(i));
        }
        for (int i = 0; i < steps; i++) {
            updateGravity(system);
        }
        return system;
    }
}

