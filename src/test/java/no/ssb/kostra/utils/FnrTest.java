package no.ssb.kostra.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class FnrTest {
    @Test
    public void isValidDateTest() {
        assertTrue(Fnr.isValidDate("010112", "ddMMyy"));
        assertTrue(Fnr.isValidDate("01012012", "ddMMyyyy"));
        assertFalse(Fnr.isValidDate("30022012", "ddMMyyyy"));
    }

    @Test
    public void isValidNorwIdTest() {
        assertTrue(Fnr.isValidNorwId("01010150589"));
        assertTrue(Fnr.isValidNorwId("41010150572"));
        assertFalse(Fnr.isValidNorwId("01011200100"));
        assertFalse(Fnr.isValidNorwId("01011200200"));
        assertFalse(Fnr.isValidNorwId("01011255555"));
        assertFalse(Fnr.isValidNorwId("01011299999"));
    }

    @Test
    public void isValidDUFnrTest() {
        assertTrue(Fnr.isValidDUFnr("201212345603"));
        assertFalse(Fnr.isValidDUFnr("201234567890"));
        assertFalse(Fnr.isValidDUFnr("12345678901"));
    }
}
