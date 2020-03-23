package no.ssb.kostra.common;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.felles.ControlPosteringLengde;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ControlRecordLengthTest {
    private List<String> listOK;
    private List<String> listFail;
    private int lengthOK;
    private int lengthFail;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private String inputFileContent;

    @Before
    public void beforeTest() {
        listOK = Arrays.asList("line01", "line02", "line03", "line04", "line05");
        listFail = Arrays.asList("line01", "line 02", "line03", "line 04", "line05");
        lengthOK = 6;
        lengthFail = 5;
        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontrol Recordlengde", "Feil: feil antall posisjoner", Constants.CRITICAL_ERROR);
        inputFileContent = "0A20194040200                  1120 010    36328\n" +
                "0A20194040200                  1121 010     4306\n" +
                "0A20194040200                  1130 010      864";
    }

    @Test
    public void testOK1() {
        assertFalse(ControlPosteringLengde.run(er, ere, listOK, lengthOK));
        assertTrue(er.isEmpty());
    }

    @Test
    public void testFail1() {
        assertTrue(ControlPosteringLengde.run(er, ere, listOK, lengthFail));
    }

    @Test
    public void testFail2() {
        assertTrue(ControlPosteringLengde.run(er, ere, listFail, lengthOK));
    }

    @Test
    public void testFail3() {
        assertTrue(ControlPosteringLengde.run(er, ere, listFail, lengthFail));
    }
}
