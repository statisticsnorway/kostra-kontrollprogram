package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1InneholderKodeFraKodelisteTest {
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "0A", "-y", "2020", "-r", "030100"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " ",
                "TEST av Kode i kodeliste", "Feil: fant ikke kode i kodeliste",
                Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK() {
        assertFalse(ControlFelt1InneholderKodeFraKodeliste.doControl(errorReport, errorReportEntry, "1", List.of("0", "1")));
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
    }

    @Test
    public void testFail() {
        assertTrue(ControlFelt1InneholderKodeFraKodeliste.doControl(errorReport, errorReportEntry, "0", List.of("1", "2")));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
    }


    @Test
    public void testBlank1() {
        assertFalse(ControlFelt1InneholderKodeFraKodeliste.doControl(errorReport, errorReportEntry, "", List.of("")));
    }
}
