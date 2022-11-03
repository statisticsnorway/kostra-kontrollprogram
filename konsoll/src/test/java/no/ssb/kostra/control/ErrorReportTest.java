package no.ssb.kostra.control;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import no.ssb.kostra.felles.ErrorReportEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorReportTest {

    @Test
    public void generateErrorReportTest() {
        Arguments args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900", "-e" , "0"});
        ErrorReport er = new ErrorReport(args);

        assertEquals(Constants.NO_ERROR, er.getErrorType());
        er.incrementCount();

        er.addEntry(new ErrorReportEntry(
                "Test", "1", " ", " "
                , "Test info"
                , "info message"
                , Constants.NO_ERROR
        ));

        assertEquals(Constants.NO_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                "Test", "2", " ", " "
                , "Test warning"
                , "warning message"
                , Constants.NORMAL_ERROR
        ));

        assertEquals(Constants.NORMAL_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                "Test", "3", " ", " "
                , "Test error"
                , "error message"
                , Constants.CRITICAL_ERROR
        ));

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

        assertTrue(er.generateReport().contains("\n"));
    }

    @Test
    public void generateErrorReportAsEmbeddedTest() {
        Arguments args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900", "-e" , "1"});
        ErrorReport er = new ErrorReport(args);

        assertEquals(Constants.NO_ERROR, er.getErrorType());
        er.incrementCount();

        er.addEntry(new ErrorReportEntry(
                "Embedded Test", "1", " ", " "
                , "Test info"
                , "info message"
                , Constants.NO_ERROR
        ));

        assertEquals(Constants.NO_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                "Embedded Test", "2", " ", " "
                , "Test warning"
                , "warning message"
                , Constants.NORMAL_ERROR
        ));

        assertEquals(Constants.NORMAL_ERROR, er.getErrorType());

        er.addEntry(new ErrorReportEntry(
                "Embedded Test", "3", " ", " "
                , "Test error"
                , "error message"
                , Constants.CRITICAL_ERROR
        ));

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

        assertFalse(er.generateReport().contains("\n"));
    }
}
