package no.ssb.kostra.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BetweenTest {
    private Class<?> clazz = null;
    private Method m = null;

    @Test
    public void testClassExists(){
        try {
            Class.forName("no.ssb.kostra.utils.Between");
        } catch(ClassNotFoundException e) {
            Assert.fail("Class 'Between' not found.");
        }
    }

    @Test
    public void testBetweenInclusiveExists(){
        try {
            clazz = Class.forName("no.ssb.kostra.utils.Between");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (clazz == null) {
            Assert.fail("Class 'Between' not found.");
            return;
        }

        try {
            m = clazz.getMethod("betweenInclusive", int.class, int.class, int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (m == null) {
            Assert.fail("method 'betweenInclusive(int , int, int)' not found.");
            return;
        }
    }

    @Test
    public void testBetweenExclusiveExists(){
        try {
            clazz = Class.forName("no.ssb.kostra.utils.Between");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (clazz == null) {
            Assert.fail("Class 'Between' not found.");
            return;
        }

        try {
            m = clazz.getMethod("betweenExclusive", int.class, int.class, int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (m == null) {
            Assert.fail("method 'betweenExclusive(int , int, int)' not found.");
            return;
        }
    }

    @Test
    public void testInclusive(){
        assertTrue(Between.betweenInclusive(1,1,3));
        assertTrue(Between.betweenInclusive(2,1,3));
        assertTrue(Between.betweenInclusive(3,1,3));

        assertFalse(Between.betweenInclusive(0,1,3));
        assertFalse(Between.betweenInclusive(4,1,3));
    }

    @Test
    public void testExclusive(){
        assertFalse(Between.betweenExclusive(1,1,3));
        assertTrue(Between.betweenExclusive(2,1,3));
        assertFalse(Between.betweenExclusive(3,1,3));

        assertFalse(Between.betweenExclusive(0,1,3));
        assertFalse(Between.betweenExclusive(4,1,3));

    }

}
