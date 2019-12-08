import org.junit.Assert;
import org.junit.Test;

public class Day4Test {

    @Test
    public void es1Acceptance() {
        Assert.assertEquals(1, Day4.es1(111111, 111111));
        Assert.assertEquals(0, Day4.es1(223450, 223450));
        Assert.assertEquals(0, Day4.es1(123789, 123789));
    }

    @Test
    public void es1() {
        Assert.assertEquals(1764, Day4.es1(152085, 670283));
    }

    @Test
    public void es2Acceptance() {
        Assert.assertEquals(1, Day4.es2(112233, 112233));
        Assert.assertEquals(0, Day4.es2(123444, 123444));
        Assert.assertEquals(1, Day4.es2(111122, 111122));
    }

    @Test
    public void es2() {
        Assert.assertEquals(1196, Day4.es2(152085, 670283));
    }
}