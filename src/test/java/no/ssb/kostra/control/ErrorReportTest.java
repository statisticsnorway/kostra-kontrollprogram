package no.ssb.kostra.control;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ErrorReportTest {
    Arguments args;
    ErrorReport er;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900", "-i", "src/test/resources/15F/Testfil_21_OK_2020_15F_for_3401.xml"});
        er = new ErrorReport(args);
    }

    @Test
    public void generateErrorReportTest() {
        assertEquals(Constants.NO_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                " ", " ", " ", " "
                , "Test"
                , "error message"
                , Constants.NO_ERROR
        ));

        assertEquals(Constants.NO_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                " ", " ", " ", " "
                , "Test"
                , "error message"
                , Constants.NORMAL_ERROR
        ));

        assertEquals(Constants.NORMAL_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                " ", " ", " ", " "
                , "Test"
                , "error message"
                , Constants.CRITICAL_ERROR
        ));

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
