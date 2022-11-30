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
        var args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900", "-e" , "0"});
        var errorReport = new ErrorReport(args);

        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
        errorReport.incrementCount();

        errorReport.addEntry(new ErrorReportEntry(
                "Test", "1", " ", " "
                , "Test info"
                , "info message"
                , Constants.NO_ERROR
        ));

        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());

        errorReport.addEntry(new ErrorReportEntry(
                "Test", "2", " ", " "
                , "Test warning"
                , "warning message"
                , Constants.NORMAL_ERROR
        ));

        assertEquals(Constants.NORMAL_ERROR, errorReport.getErrorType());

        errorReport.addEntry(new ErrorReportEntry(
                "Test", "3", " ", " "
                , "Test error"
                , "error message"
                , Constants.CRITICAL_ERROR
        ));

        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());

        var errorReportHtml = errorReport.generateReport();
        assertTrue(errorReportHtml.contains("\n"));
        assertTrue(errorReportHtml.contains("Kontrollprogramversjon: LOCAL-SNAPSHOT"));
    }

    @Test
    public void generateErrorReportAsEmbeddedTest() {
        var args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900", "-e" , "1"});
        var errorReport = new ErrorReport(args);

        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
        errorReport.incrementCount();

        errorReport.addEntry(new ErrorReportEntry(
                "Embedded Test", "1", " ", " "
                , "Test info"
                , "info message"
                , Constants.NO_ERROR
        ));

        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());

        errorReport.addEntry(new ErrorReportEntry(
                "Embedded Test", "2", " ", " "
                , "Test warning"
                , "warning message"
                , Constants.NORMAL_ERROR
        ));

        assertEquals(Constants.NORMAL_ERROR, errorReport.getErrorType());

        errorReport.addEntry(new ErrorReportEntry(
                "Embedded Test", "3", " ", " "
                , "Test error"
                , "error message"
                , Constants.CRITICAL_ERROR
        ));

        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());

        assertFalse(errorReport.generateReport().contains("\n"));
    }
}
