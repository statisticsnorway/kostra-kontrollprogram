package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ControlFelt1DatoTest {
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Dato etter dato", "Feil: fant ikke dato etter dato", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1Dato.doControl(er, ere, "20200101", "yyyyMMdd"));
        assertTrue(ControlFelt1Dato.doControl(er, ere, "00000000", "yyyyMMdd"));
        assertTrue(ControlFelt1Dato.doControl(er, ere, "        ", "yyyyMMdd"));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlFelt1Dato.doControl(er, ere, "20200232", "yyyyMMdd"));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
