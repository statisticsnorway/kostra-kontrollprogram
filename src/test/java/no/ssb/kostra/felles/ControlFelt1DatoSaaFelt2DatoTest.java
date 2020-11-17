package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFelt1DatoSaaFelt2Dato;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControlFelt1DatoSaaFelt2DatoTest {
    InputStream sysInBackup;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private Record r1;
    private Record r2;
    private Record r3;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av Dato etter dato", "Feil: fant ikke dato etter dato", Constants.CRITICAL_ERROR);
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "felt1", "String", "", 1, 6, new ArrayList<>(), "", false),
                new FieldDefinition(2, "dato1", "Date", "", 7, 14, new ArrayList<>(), "yyyyMMdd", false),
                new FieldDefinition(3, "felt2", "String", "", 15, 20, new ArrayList<>(), "", false),
                new FieldDefinition(4, "dato2", "Date", "", 21, 28, new ArrayList<>(), "yyyyMMdd", false)
        );
        String inputFileContent = "Date1:20201101Date2:02122020";
        r1 = new Record("Date1:20200101Date2:20200202", fieldDefinitions);
        r2 = new Record("Date1:20200101Date2:00000000", fieldDefinitions);
        r3 = new Record("Date1:20200101Date2:        ", fieldDefinitions);
        sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);
    }

    @After
    public void afterTest() {
        System.setIn(sysInBackup);
    }

    @Test
    public void testOK1() {
        ControlFelt1DatoSaaFelt2Dato.doControl(r1, er, ere, "dato1", "dato2");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK2() {
        ControlFelt1DatoSaaFelt2Dato.doControl(r2, er, ere, "dato1", "dato2");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK3() {
        ControlFelt1DatoSaaFelt2Dato.doControl(r3, er, ere, "dato1", "dato2");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK4() {
        ControlFelt1DatoSaaFelt2Dato.doControl(r2, er, ere, "dato2", "dato1");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK5() {
        ControlFelt1DatoSaaFelt2Dato.doControl(r3, er, ere, "dato2", "dato1");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFail1() {
        ControlFelt1DatoSaaFelt2Dato.doControl(r1, er, ere, "dato2", "dato1");
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

}
