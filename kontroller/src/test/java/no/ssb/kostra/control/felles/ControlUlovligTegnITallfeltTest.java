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
    private ErrorReport er;
    private ErrorReportEntry ere;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Ulovlige tegn i tallfelt", "Ulovlige tegn i tallfelt", Constants.CRITICAL_ERROR);
    }

    @Test
    public void testOK() {
        assertFalse(ControlUlovligTegnITallfelt.doControl(er, ere, "1"));
        assertFalse(ControlUlovligTegnITallfelt.doControl(er, ere, "-1"));
        assertFalse(ControlUlovligTegnITallfelt.doControl(er, ere, " 1"));
        assertFalse(ControlUlovligTegnITallfelt.doControl(er, ere, " -1"));
    }

    @Test
    public void testFail1() {
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, "\t1"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, "-\t1"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, " 1\t"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, "\t -1"));
    }

    @Test
    public void testFail2() {
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, "1A"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, "-1A"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, " 1A"));
        assertTrue(ControlUlovligTegnITallfelt.doControl(er, ere, "A -1"));
    }
}
