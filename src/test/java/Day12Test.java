import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day12Test {

    private Day12 day12;

    @Before
    public void setUp() throws Exception {
        day12 = new Day12();
    }

    @Test
    public void es1Acceptance() {
        List<int[]> staringPositions = List.of(
                new int[]{-1, 0, 2, 0, 0, 0},
                new int[]{2, -10, -7, 0, 0, 0},
                new int[]{4, -8, 8, 0, 0, 0},
                new int[]{3, 5, -1, 0, 0, 0}
        );

        verifyMoonStatus(createASystem(List.of(
                new int[]{-1, 0, 2, 0, 0, 0},
                new int[]{2, -10, -7, 0, 0, 0},
                new int[]{4, -8, 8, 0, 0, 0},
                new int[]{3, 5, -1, 0, 0, 0})), day12.systemAtTime(staringPositions, 0));

        verifyMoonStatus(createASystem(List.of(
                new int[]{2, -1, 1, 3, -1, -1},
                new int[]{3, -7, -4, 1, 3, 3},
                new int[]{1, -7, 5, -3, 1, -3},
                new int[]{2, 2, 0, -1, -3, 1})), day12.systemAtTime(staringPositions, 1));

        verifyMoonStatus(createASystem(List.of(
                new int[]{5, -3, -1, 3, -2, -2},
                new int[]{1, -2, 2, -2, 5, 6},
                new int[]{1, -4, -1, 0, 3, -6},
                new int[]{1, -4, 2, -1, -6, 2})), day12.systemAtTime(staringPositions, 2));

        verifyMoonStatus(createASystem(List.of(
                new int[]{5, -6, -1, 0, -3, 0},
                new int[]{0, 0, 6, -1, 2, 4},
                new int[]{2, 1, -5, 1, 5, -4},
                new int[]{1, -8, 2, 0, -4, 0})), day12.systemAtTime(staringPositions, 3));

        verifyMoonStatus(createASystem(List.of(
                new int[]{2, -8, 0, -3, -2, 1},
                new int[]{2, 1, 7, 2, 1, 1},
                new int[]{2, 3, -6, 0, 2, -1},
                new int[]{2, -9, 1, 1, -1, -1})), day12.systemAtTime(staringPositions, 4));

        verifyMoonStatus(createASystem(List.of(
                new int[]{-1, -9, 2, -3, -1, 2},
                new int[]{4, 1, 5, 2, 0, -2},
                new int[]{2, 2, -4, 0, -1, 2},
                new int[]{3, -7, -1, 1, 2, -2})), day12.systemAtTime(staringPositions, 5));

        verifyMoonStatus(createASystem(List.of(
                new int[]{-1, -7, 3, 0, 2, 1},
                new int[]{3, 0, 0, -1, -1, -5},
                new int[]{3, -2, 1, 1, -4, 5},
                new int[]{3, -4, -2, 0, 3, -1})), day12.systemAtTime(staringPositions, 6));

        verifyMoonStatus(createASystem(List.of(
                new int[]{2, -2, 1, 3, 5, -2},
                new int[]{1, -4, -4, -2, -4, -4},
                new int[]{3, -7, 5, 0, -5, 4},
                new int[]{2, 0, 0, -1, 4, 2})), day12.systemAtTime(staringPositions, 7));

        verifyMoonStatus(createASystem(List.of(
                new int[]{5, 2, -2, 3, 4, -3},
                new int[]{2, -7, -5, 1, -3, -1},
                new int[]{0, -9, 6, -3, -2, 1},
                new int[]{1, 1, 3, -1, 1, 3})), day12.systemAtTime(staringPositions, 8));

        verifyMoonStatus(createASystem(List.of(
                new int[]{5, 3, -4, 0, 1, -2},
                new int[]{2, -9, -3, 0, -2, 2},
                new int[]{0, -8, 4, 0, 1, -2},
                new int[]{1, 1, 5, 0, 0, 2})), day12.systemAtTime(staringPositions, 9));

        Moon[] actualSystem = day12.systemAtTime(staringPositions, 10);
        verifyMoonStatus(createASystem(List.of(
                new int[]{2, 1, -3, -3, -2, 1},
                new int[]{1, -8, 0, -1, 1, 3},
                new int[]{3, -6, 1, 3, 2, -3},
                new int[]{2, 0, 4, 1, -1, -1})), actualSystem);
        Assert.assertEquals(179L, Arrays.stream(actualSystem).mapToLong(Moon::energy).sum());
    }

    private Moon[] createASystem(List<int[]> init) {
        Moon[] system = new Moon[init.size()];
        for (int i = 0; i < init.size(); i++) {
            system[i] = new Moon(init.get(i));
        }
        return system;
    }

    private void verifyMoonStatus(Moon[] expectedSystem, Moon[] actualSystem) {
        Assert.assertEquals(expectedSystem.length, actualSystem.length);
        for (int i = 0; i < expectedSystem.length; i++) {
            Assert.assertArrayEquals(expectedSystem[i].getPos(), actualSystem[i].getPos());
            Assert.assertArrayEquals(expectedSystem[i].getVel(), actualSystem[i].getVel());
        }
    }

    @Test
    public void es1() {
        List<int[]> staringPositions = List.of(
                new int[]{-7, 17, -11, 0, 0, 0},
                new int[]{9, 12, 5, 0, 0, 0},
                new int[]{-9, 0, -4, 0, 0, 0},
                new int[]{4, 6, 0, 0, 0, 0}
        );

        Moon[] actualSystem = day12.systemAtTime(staringPositions, 1000);
        Assert.assertEquals(7013L, Arrays.stream(actualSystem).mapToLong(Moon::energy).sum());
    }
}