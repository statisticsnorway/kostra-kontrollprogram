package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ControlUlovligTegnITallfeltTest {
    private ErrorReport errorReport;
    private ErrorReportEntry errorReportEntry;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        errorReport = new ErrorReport(arguments);
        errorReportEntry = new ErrorReportEntry(" ", " ", " ", " ",
                "TEST av Ulovlige tegn i tallfelt", "Ulovlige tegn i tallfelt",
                Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK() {
        assertFalse(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "1"));
        assertFalse(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "-1"));
        assertFalse(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, " 1"));
        assertFalse(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, " -1"));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "\t1"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "-\t1"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, " 1\t"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "\t -1"));
    }

    @Test
    public void testFail2() {
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "1A"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "-1A"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, " 1A"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(errorReport, errorReportEntry, "A -1"));
    }
}
