package no.ssb.kostra.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BetweenTest {
    @Test
    public void testClassExists() {
        try {
            Class.forName("no.ssb.kostra.utils.Between");
        } catch (ClassNotFoundException e) {
            Assert.fail("Class 'Between' not found.");
        }
    }

    @Test
    public void testBetweenInclusiveExists() {
        try {
            Class<?> clazz = Class.forName("no.ssb.kostra.utils.Between");
            clazz.getMethod("betweenInclusive", int.class, int.class, int.class);

        } catch (ClassNotFoundException e) {
            Assert.fail("Class 'Between' not found.");
            e.printStackTrace();

        } catch (NoSuchMethodException | SecurityException e) {
            Assert.fail("method 'betweenInclusive(int , int, int)' not found.");
            e.printStackTrace();
        }
    }

    @Test
    public void testBetweenExclusiveExists() {
        try {
            Class<?> clazz = Class.forName("no.ssb.kostra.utils.Between");
            clazz.getMethod("betweenExclusive", int.class, int.class, int.class);

        } catch (ClassNotFoundException e) {
            Assert.fail("Class 'Between' not found.");
            e.printStackTrace();

        } catch (NoSuchMethodException | SecurityException e) {
            Assert.fail("method 'betweenExclusive(int , int, int)' not found.");
            e.printStackTrace();
        }
    }

    @Test
    public void testBetweenInclusive() {
        assertTrue(Between.betweenInclusive(1, 1, 3));
        assertTrue(Between.betweenInclusive(2, 1, 3));
        assertTrue(Between.betweenInclusive(3, 1, 3));

        assertFalse(Between.betweenInclusive(0, 1, 3));
        assertFalse(Between.betweenInclusive(4, 1, 3));
    }

    @Test
    public void testBetweenExclusive() {
        assertFalse(Between.betweenExclusive(1, 1, 3));
        assertTrue(Between.betweenExclusive(2, 1, 3));
        assertFalse(Between.betweenExclusive(3, 1, 3));

        assertFalse(Between.betweenExclusive(0, 1, 3));
        assertFalse(Between.betweenExclusive(4, 1, 3));
    }
}
