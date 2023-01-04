package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControlRecordLengthTest {
    private ErrorReport errorReport;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        errorReport = new ErrorReport(arguments);
    }

    @Test
    public void testOK() {
        var result = ControlRecordLengde.doControl(List.of("12", "13", "14"), errorReport, 2);
        if (Constants.DEBUG) {
            System.out.println(errorReport.generateReport());
        }

        assertFalse(result);
    }

    @Test
    public void testIncorrectLengthFail() {
        var result = ControlRecordLengde.doControl(List.of("12", "13", "14"), errorReport, 1);
        if (Constants.DEBUG) {
            System.out.println(errorReport.generateReport());
        }

        assertTrue(result);
        assertEquals(errorReport.getErrorType(), Constants.CRITICAL_ERROR);
        assertTrue(errorReport.generateReport().contains("Gjelder for linjene"));
    }

    @Test
    public void testNoDataInvalidFileFail() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-a", "0"});
        errorReport = new ErrorReport(arguments);

        var result = ControlRecordLengde.doControl(List.of("Invalid file"), errorReport, 1);
        if (Constants.DEBUG) {
            System.out.println(errorReport.generateReport());
        }

        assertTrue(result);
        assertEquals(errorReport.getErrorType(), Constants.CRITICAL_ERROR);
    }

    @Test
    public void testNoDataOnlySpacesOK() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-a", "0"});
        errorReport = new ErrorReport(arguments);

        var result = ControlRecordLengde.doControl(List.of(" "), errorReport, 1);
        if (Constants.DEBUG) {
            System.out.println(errorReport.generateReport());
        }

        assertFalse(result);
        assertEquals(errorReport.getErrorType(), Constants.NO_ERROR);
    }

    @Test
    public void testInvalidCharsFail() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-a", "0"});
        errorReport = new ErrorReport(arguments);

        var result = ControlRecordLengde.doControl(List.of("\t"), errorReport, 1);
        if (Constants.DEBUG) {
            System.out.println(errorReport.generateReport());
        }

        assertTrue(result);
        assertEquals(errorReport.getErrorType(), Constants.CRITICAL_ERROR);
    }
}
