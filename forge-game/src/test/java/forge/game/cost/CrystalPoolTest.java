package forge.game.cost;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrystalPoolTest {
    @Test
    public void testAddAndCanPay() {
        CrystalPool pool = new CrystalPool();
        pool.add(CrystalElement.FIRE, 3);
        CrystalPool cost = new CrystalPool();
        cost.add(CrystalElement.FIRE, 2);
        assertTrue(pool.canPay(cost));
        assertTrue(pool.pay(cost));
        assertEquals(1, pool.get(CrystalElement.FIRE));
    }

    @Test
    public void testCannotPayWhenInsufficient() {
        CrystalPool pool = new CrystalPool();
        pool.add(CrystalElement.WATER, 1);
        CrystalPool cost = new CrystalPool();
        cost.add(CrystalElement.WATER, 2);
        assertFalse(pool.canPay(cost));
        assertFalse(pool.pay(cost));
    }

    @Test
    public void testCopyIsIndependent() {
        CrystalPool pool = new CrystalPool();
        pool.add(CrystalElement.EARTH, 1);
        CrystalPool copy = pool.copy();
        pool.add(CrystalElement.ICE, 1);
        assertEquals(1, copy.get(CrystalElement.EARTH));
        assertEquals(0, copy.get(CrystalElement.ICE));
    }

    @Test
    public void testPayWithMismatchedElements() {
        CrystalPool pool = new CrystalPool();
        pool.add(CrystalElement.FIRE, 1);
        pool.add(CrystalElement.WATER, 2);

        CrystalPool cost = new CrystalPool();
        cost.add(CrystalElement.EARTH, 2);

        assertTrue(pool.canPay(cost));
        assertTrue(pool.pay(cost));
        assertEquals(1, pool.total());
    }
}
