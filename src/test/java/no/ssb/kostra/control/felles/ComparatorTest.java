package no.ssb.kostra.control.felles;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class ComparatorTest {
    @Test
    public void testGreaterThan() {
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(1, "<", 2));
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(2, "<", 2));
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(3, "<", 2));
    }

    @Test
    public void testGreaterOrEqualThan() {
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(2, "<=", 1));
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(2, "<=", 2));
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(2, "<=", 3));
    }

    @Test
    public void testLessThan() {
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(2, ">", 1));
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(2, ">", 2));
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(2, ">", 3));
    }

    @Test
    public void testLessOrEqualThan() {
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(1, ">=", 2));
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(2, ">=", 2));
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(3, ">=", 2));
    }

    @Test
    public void testEqualTo() {
        Assert.assertFalse("1 == 2 -> false", Comparator.compareIntegerOperatorInteger(1, "==", 2));
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(2, "==", 2));
    }

    @Test
    public void testNotEqualTo() {
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(1, "!=", 2));
        Assert.assertFalse(Comparator.compareIntegerOperatorInteger(2, "!=", 2));
    }

    @Test
    public void testAIsNull() {
        Assert.assertTrue(Comparator.compareIntegerOperatorInteger(null, "==", 0));
    }

    @Test
    public void isCodeInCodelist() {
        Assert.assertTrue(Comparator.isCodeInCodelist("code1", List.of("code1", "code2")));
        Assert.assertFalse(Comparator.isCodeInCodelist("notInCodelist", List.of("code1", "code2")));
    }

    @Test
    public void isValidOrgnr() {
        Assert.assertTrue(Comparator.isValidOrgnr("944117784"));
        Assert.assertTrue(Comparator.isValidOrgnr("999999999"));

        Assert.assertFalse(Comparator.isValidOrgnr("000000000"));
        Assert.assertFalse(Comparator.isValidOrgnr("123456789"));
    }

    @Test
    public void testBetweenInclusive() {
        Assert.assertTrue(Comparator.between(1, 1, 3));
        Assert.assertTrue(Comparator.between(2, 1, 3));
        Assert.assertTrue(Comparator.between(3, 1, 3));

        Assert.assertFalse(Comparator.between(0, 1, 3));
        Assert.assertFalse(Comparator.between(4, 1, 3));
    }
}
