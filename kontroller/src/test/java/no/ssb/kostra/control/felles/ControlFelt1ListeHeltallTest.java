package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ControlFelt1ListeHeltallTest {
    private final String title = "navn på felt";
    private ErrorReport errorReport;

    @Before
    public void beforeTest() {
        var arguments = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        errorReport = new ErrorReport(arguments);
    }

    @Test
    public void testOK1() {
        assertFalse(ControlFelt1ListeHeltall.doControl(errorReport, title, Arrays.asList(1, 2), Constants.NO_ERROR));
        assertEquals(Constants.NO_ERROR, errorReport.getErrorType());
        System.out.println(errorReport.generateReport());
    }

    @Test
    public void testFail1() {
        List<Integer> listOfValues = Arrays.asList(0, null);
        assertTrue(ControlFelt1ListeHeltall.doControl(errorReport, title, listOfValues, Constants.CRITICAL_ERROR));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
        System.out.println(errorReport.generateReport());
    }

    @Test
    public void testFail2() {
        List<Integer> felt1Liste = Arrays.asList(0, 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
                , null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        );
        assertTrue(ControlFelt1ListeHeltall.doControl(errorReport, title, felt1Liste, Constants.CRITICAL_ERROR));
        assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
        System.out.println(errorReport.generateReport());
    }
}
