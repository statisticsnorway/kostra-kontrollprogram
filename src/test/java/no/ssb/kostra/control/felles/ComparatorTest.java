package no.ssb.kostra.control.felles;


import org.junit.Test;

import java.util.List;

import static no.ssb.kostra.control.felles.Comparator.*;
import static org.junit.Assert.*;


public class ComparatorTest {
    @Test
    public void compareIntegerOperatorIntegerGreaterThanTest() {
        assertTrue(compareIntegerOperatorInteger(1, "<", 2));
        assertFalse(compareIntegerOperatorInteger(2, "<", 2));
        assertFalse(compareIntegerOperatorInteger(3, "<", 2));
    }

    @Test
    public void compareIntegerOperatorIntegerGreaterOrEqualThanTest() {
        assertFalse(compareIntegerOperatorInteger(2, "<=", 1));
        assertTrue(compareIntegerOperatorInteger(2, "<=", 2));
        assertTrue(compareIntegerOperatorInteger(2, "<=", 3));
    }

    @Test
    public void compareIntegerOperatorIntegerLessThanTest() {
        assertTrue(compareIntegerOperatorInteger(2, ">", 1));
        assertFalse(compareIntegerOperatorInteger(2, ">", 2));
        assertFalse(compareIntegerOperatorInteger(2, ">", 3));
    }

    @Test
    public void compareIntegerOperatorIntegerLessOrEqualThanTest() {
        assertFalse(compareIntegerOperatorInteger(1, ">=", 2));
        assertTrue(compareIntegerOperatorInteger(2, ">=", 2));
        assertTrue(compareIntegerOperatorInteger(3, ">=", 2));
    }

    @Test
    public void compareIntegerOperatorIntegerEqualToTest() {
        assertFalse(compareIntegerOperatorInteger(1, "==", 2));
        assertTrue(compareIntegerOperatorInteger(2, "==", 2));
    }

    @Test
    public void compareIntegerOperatorIntegerNotEqualToTest() {
        assertTrue(compareIntegerOperatorInteger(1, "!=", 2));
        assertFalse(compareIntegerOperatorInteger(2, "!=", 2));
    }

    @Test
    public void compareIntegerOperatorIntegerAIsNullTest() {
        assertTrue(compareIntegerOperatorInteger(null, "==", 0));
    }

    @Test
    public void isCodeInCodelistWithValidCodeTest() {
        assertTrue(isCodeInCodelist("code1", List.of("code1", "code2")));
    }

    @Test
    public void isCodeInCodelistWithInvalidCodeTest() {
        assertFalse(isCodeInCodelist("notInCodelist", List.of("code1", "code2")));
    }

    @Test
    public void removeCodesFromCodelistTest() {
        assertEquals(List.of("code1", "code2", "code3"), removeCodesFromCodelist(List.of("code1", "code2", "code3"), List.of("notInCodelist")));
        assertEquals(List.of("code1", "code3"), removeCodesFromCodelist(List.of("code1", "code2", "code3"), List.of("code2")));
    }

    @Test
    public void isValidOrgnrTest() {
        assertTrue(isValidOrgnr("944117784"));
        assertTrue(isValidOrgnr("999999999"));

        assertFalse(isValidOrgnr("000000000"));
        assertFalse(isValidOrgnr("123456789"));
    }

    @Test
    public void betweenTest() {
        assertTrue(between(1, 1, 3));
        assertTrue(between(2, 1, 3));
        assertTrue(between(3, 1, 3));

        assertFalse(between(0, 1, 3));
        assertFalse(between(4, 1, 3));
    }

    @Test
    public void outsideOfTest() {
        assertFalse(outsideOf(1, 1, 3));
        assertFalse(outsideOf(2, 1, 3));
        assertFalse(outsideOf(3, 1, 3));

        assertTrue(outsideOf(0, 1, 3));
        assertTrue(outsideOf(4, 1, 3));
    }

    @Test
    public void isValidDateWithValidDateTest() {
        assertTrue(isValidDate("01-01-2000", "dd-MM-yyyy"));
    }

    @Test
    public void isValidDateWithValidLeapYearDateTest() {
        assertTrue(isValidDate("29-02-1900", "dd-MM-yyyy"));
    }

    @Test
    public void isValidDateWithInValidDateTest() {
        assertFalse(isValidDate("42-01-2000", "dd-MM-yyyy"));
    }
    @Test
    public void isValidDateBlankZeroDateTest() {
        assertFalse(isValidDate("0000000000", "dd-MM-yyyy"));
    }

    @Test
    public void isValidDateBlankSpaceDateTest() {
        assertFalse(isValidDate("         ", "dd-MM-yyyy"));
    }

    @Test
    public void isEmptyTest() {
        assertFalse(isEmpty("true"));
        assertTrue(isEmpty(""));
        assertTrue(isEmpty(null));
    }

    @Test
    public void defaultStringTest() {
        assertEquals("string", defaultString("string", "defaultString"));
        assertEquals("defaultString", defaultString(null, "defaultString"));
    }
}
