package no.ssb.kostra.utils;

import org.junit.Test;


import static no.ssb.kostra.utils.Fnr.*;
import static org.junit.Assert.*;

public class FnrTest {
    @Test
    public void isValidDateTest() {
        assertTrue(isValidDate("010112", "ddMMyy"));
        assertTrue(isValidDate("01012012", "ddMMyyyy"));
        assertFalse(isValidDate("30022012", "ddMMyyyy"));
    }

    @Test
    public void isValidNorwIdTest() {
        assertTrue(isValidNorwId("01010150589"));
        assertTrue(isValidNorwId("41010150572"));
        assertFalse(isValidNorwId("01011200100"));
        assertFalse(isValidNorwId("01011200200"));
        assertFalse(isValidNorwId("01011255555"));
        assertFalse(isValidNorwId("01011299999"));
        assertFalse(isValidNorwId("           "));
    }

    @Test
    public void isValidDUFnrTest() {
        assertTrue(isValidDUFnr("201212345603"));
        assertFalse(isValidDUFnr("201234567890"));
        assertFalse(isValidDUFnr("            "));
        assertFalse(isValidDUFnr("12345678901"));
    }

    @Test
    public void getAlderFromFnrTest() {
        assertEquals(20, getAlderFromFnr("01010150589", 2021));
    }
}
