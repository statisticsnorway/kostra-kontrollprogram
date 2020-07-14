package no.ssb.kostra.felles;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.control.ErrorReport;
import no.ssb.kostra.control.ErrorReportEntry;
import no.ssb.kostra.control.FieldDefinition;
import no.ssb.kostra.control.felles.ControlRecordLengde;
import no.ssb.kostra.control.regnskap.FieldDefinitions;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class ControlRecordLengthTest {
    InputStream sysInBackup;
    private int lengthOK;
    private int lengthFail;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private String inputFileContent;
    private List<FieldDefinition> fieldDefinitions;
    private List<String> content;

    @Before
    public void beforeTest() {
        lengthOK = 48;
        lengthFail = 4;
        //                  000000000111111111122222222223333333333444444444
        //                  123456789012345678901234567890123456789012345678
        inputFileContent = "0A20194040200                  1120 010    36328\n" +
                "0A20194040200                  1121 010     4306\n" +
                "0A20194040200                  1130 010      864";

        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        fieldDefinitions = FieldDefinitions.getFieldDefinitions();

        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "000000"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "Kontrol Recordlengde", "Feil: feil antall posisjoner", Constants.CRITICAL_ERROR);
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testContentOK1() {
        content = args.getInputContentAsStringList();

        assertNotNull("Has content", content);
    }

    @Test
    public void testPosteringListOK1() {
        content = args.getInputContentAsStringList();
        assertFalse(content.isEmpty());
    }

    @Test
    public void testControlRecordLengthOK1() {
        content = args.getInputContentAsStringList();

        ControlRecordLengde.doControl(content, er, lengthOK);

//        assertTrue(er.isEmpty());
        assertEquals(er.getErrorType(), Constants.NO_ERROR);
    }

    @Test
    public void testControlRecordLengthFail1() {
        content = args.getInputContentAsStringList();
        ControlRecordLengde.doControl(content, er, lengthFail);

        assertFalse(er.isEmpty());
        assertEquals(er.getErrorType(), Constants.CRITICAL_ERROR);
    }
}
