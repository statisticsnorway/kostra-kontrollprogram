package no.ssb.kostra.control.felles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PosteringTest {
    String lineOK1;
    String lineFail;

    @Before
    public void beforeTest() {
        lineOK1 = "0A20194040200                  1120 010    36328";
        lineFail = "0A20194040200                  1120 010WILL FAIL";
    }

//    @Test
//    public void testPosteringOK1() {
//        Record p = new Record(lineOK1);
//        assertTrue(p.getRecord().equalsIgnoreCase(lineOK1));
//        assertTrue(p.getLinjenummer() >  0);
//    }
//
//    @Test
//    public void testPosteringFail1() {
//        Record p = new Record(lineFail);
//        assertTrue(p.getRecord().equalsIgnoreCase(lineFail));
//        assertFalse(p.getRecord().equalsIgnoreCase(lineOK1));
//        assertTrue(p.getLinjenummer() >  0);
//
//    }
}
