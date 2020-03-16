package no.ssb.kostra.test.control.utils;

import no.ssb.kostra.utils.FamilievernKontorNrChecker;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FamilievernKontorNrCheckerTest {
    @Test
    public void testKontorNrOK1() {
        assertTrue(FamilievernKontorNrChecker.hasCorrectKontorNr("667600", "18"));
    }

    @Test
    public void testKontorNrFail1() {
        assertFalse(FamilievernKontorNrChecker.hasCorrectKontorNr("667600", "09"));
    }

    @Test
    public void testRegionNrFail1() {
        assertFalse(FamilievernKontorNrChecker.hasCorrectKontorNr("123456", "11"));
    }

}