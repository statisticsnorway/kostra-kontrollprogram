package no.ssb.kostra.control.felles;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComparatorTest {
    @Test
    public void testGreaterThan() {
        assertTrue(Comparator.compareIntegerOperatorInteger(1, "<", 2));
        assertFalse(Comparator.compareIntegerOperatorInteger(2, "<", 2));
        assertFalse(Comparator.compareIntegerOperatorInteger(3, "<", 2));
    }

    @Test
    public void testGreaterOrEqualThan() {
        assertFalse(Comparator.compareIntegerOperatorInteger(2, "<=", 1));
        assertTrue(Comparator.compareIntegerOperatorInteger(2, "<=", 2));
        assertTrue(Comparator.compareIntegerOperatorInteger(2, "<=", 3));
    }

    @Test
    public void testLessThan() {
        assertTrue(Comparator.compareIntegerOperatorInteger(2, ">", 1));
        assertFalse(Comparator.compareIntegerOperatorInteger(2, ">", 2));
        assertFalse(Comparator.compareIntegerOperatorInteger(2, ">", 3));
    }

    @Test
    public void testLessOrEqualThan() {
        assertFalse(Comparator.compareIntegerOperatorInteger(1, ">=", 2));
        assertTrue(Comparator.compareIntegerOperatorInteger(2, ">=", 2));
        assertTrue(Comparator.compareIntegerOperatorInteger(3, ">=", 2));
    }

    @Test
    public void testEqualTo() {
        assertFalse(Comparator.compareIntegerOperatorInteger(1, "==", 2));
        assertTrue(Comparator.compareIntegerOperatorInteger(2, "==", 2));
    }

    @Test
    public void testNotEqualTo() {
        assertTrue(Comparator.compareIntegerOperatorInteger(1, "!=", 2));
        assertFalse(Comparator.compareIntegerOperatorInteger(2, "!=", 2));
    }
}
