package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.Constants;
import no.ssb.kostra.felles.ErrorReport;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class ControlRecordLengthTest {
    private ErrorReport er;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        er = new ErrorReport(args);
    }

    @Test
    public void testOK() {
        assertFalse(ControlRecordLengde.doControl(List.of("12", "13", "14"), er, 2));
    }

    @Test
    public void testIncorrectLengthFail() {
        assertTrue(ControlRecordLengde.doControl(List.of("12", "13", "14"), er, 1));
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
        assertTrue(er.generateReport().contains("Gjelder for linjene"));
    }

    @Test
    public void testNoDataFail() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-a", "0"});
        er = new ErrorReport(args);

        assertTrue(ControlRecordLengde.doControl(List.of(""), er, 1));
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
    }

    @Test
    public void testNoDataOK() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000", "-a", "0"});
        er = new ErrorReport(args);

        assertFalse(ControlRecordLengde.doControl(List.of(), er, 1));
        assertEquals(er.getErrorType(), Constants.NO_ERROR);
    }
}
