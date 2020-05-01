package no.ssb.kostra.control;

import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ErrorReportTest {
    Arguments args;
    ErrorReport er;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900", "-i", "src/test/resources/15F_V2019_R040200_OK.xml"});
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
