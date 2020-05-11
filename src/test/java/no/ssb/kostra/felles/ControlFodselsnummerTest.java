package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFodselsnummer;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControlFodselsnummerTest {
    private final String className = "ControlFodselsnummer";
    private final String packageName = "no.ssb.kostra.control.felles";
    private final String fullClassName = packageName + "." + className;
    InputStream systemInBackup;
    private Class<?> clazz = null;
    private Method m = null;
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;
    private String inputFileContent;
    private Record r;


    @Before
    public void beforeTest() {
        fieldDefinitions = List.of(
                new FieldDefinition(1, "ssn", "String", "", 1, 11, new ArrayList<>(), "", false)
        );

        inputFileContent = "010101    01012001";
        systemInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(inputFileContent.getBytes(StandardCharsets.ISO_8859_1));
        System.setIn(in);

        args = new Arguments(new String[]{"-s", "Test", "-y", "9999", "-r", "888888"});
        er = new ErrorReport(args);
        ere = new ErrorReportEntry(" ", " ", " ", " "
                , "TEST av dato", "Feil i dato", Constants.CRITICAL_ERROR);
        r = new Record("123456785", fieldDefinitions);

    }

    @After
    public void afterTest() {
        System.setIn(systemInBackup);
    }

    @Test
    public void testClassExists() {
        try {
            Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            Assert.fail("Class 'Between' not found.");
        }
    }

    @Test
    public void testDoControlExists() {
        try {
            Class.forName(fullClassName)
                    .getMethod("doControl",
                            Record.class,
                            ErrorReport.class,
                            ErrorReportEntry.class,
                            String.class
                    );

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            Assert.fail("method 'doControl(Record, ErrorReport, ErrorReportEntry, String)' not found.");
        }
    }

    @Test
    public void testOK1() {
        r = new Record("01010150589", fieldDefinitions);

        ControlFodselsnummer.doControl(r, er, ere, "ssn");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOK2() {
        r = new Record("41010150572", fieldDefinitions);

        ControlFodselsnummer.doControl(r, er, ere, "ssn");
        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFail1() {
        r = new Record("01010150590", fieldDefinitions);
        ControlFodselsnummer.doControl(r, er, ere, "ssn");
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

    }

    @Test
    public void testFail2() {
        r = new Record("010101     ", fieldDefinitions);
        ControlFodselsnummer.doControl(r, er, ere, "ssn");
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());

    }
}
