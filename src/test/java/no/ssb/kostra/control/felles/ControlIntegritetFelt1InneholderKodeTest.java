package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControlIntegritetFelt1InneholderKodeTest {
    private ErrorReport er;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlIntegritetFelt1InneholderKode.doControl(er, 1, "title", "korrekt verdi", "korrekt verdi", Constants.NO_ERROR));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlIntegritetFelt1InneholderKode.doControl(er, 1, "title", "feil verdi", "korrekt verdi", Constants.CRITICAL_ERROR));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
