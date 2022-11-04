package no.ssb.kostra.control.felles;

import no.ssb.kostra.controlprogram.Arguments;
import no.ssb.kostra.felles.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ControlFilbeskrivelseTest {
    private ErrorReport er;
    private KostraRecord r;

    @Before
    public void beforeTest() {
        Arguments args = new Arguments(new String[]{"-s", "0T", "-y", "2020", "-r", "030100"});
        er = new ErrorReport(args);
    }

    @Test
    public void testFieldDefinitionLengthGreaterThan0() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
        );
        var record1 = "OK   ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testFieldDefinitionLengthEqualTo0() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, -1, new ArrayList<>(), "", false)
        );
        var record1 = "FAIL!";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


    @Test
    public void testOptionalStringOK1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
        );
        var record1 = "OK   ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalStringOK2() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
        );

        var record1 = "     ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testMandatoryStringOK() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", true)
        );
        var record1 = "OKstr";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testMandatoryStringFail() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", true)
        );
        var record1 = "     ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalStringWithCodeListOK1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
        );
        var record1 = "ABCDE";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalStringWithCodeListOK2() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
        );
        var record1 = "     ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalStringWithCodeListFail() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
        );
        var record1 = "FAIL!";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringWithCodeListOK() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
        );
        var record1 = "ABCDE";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testMandatoryStringWithCodeListFail1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
        );
        var record1 = "FAIL!";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringWithCodeListFail2() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
        );
        var record1 = "     ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalIntegerOk1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
        );
        var record1 = "    1";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalIntegerOk2() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
        );
        var record1 = "     ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalIntegerFail() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
        );
        var record1 = "FAIL!";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryIntegerOk1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
        );
        var record1 = "    1";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testMandatoryIntegerFail1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
        );
        var record1 = "     ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryIntegerFail2() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
        );
        var record1 = "FAIL!";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


    @Test
    public void testMandatoryDateOk1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );
        var record1 = "20200101";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testMandatoryDateFailIncorrectDate() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );
        var record1 = "20202020";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailValueMissing() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );
        var record1 = "        ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailDatePatternMissing() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "", true)
        );
        var record1 = "20200101";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailIncorrectValue() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );
        var record1 = "FAIL!   ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailInvalidDate() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );
        var record1 = "20200000";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateOk1() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );
        var record1 = "20200101";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalDateFailIncorrectDate() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );
        var record1 = "20202020";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailValueMissing() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );
        var record1 = "        ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertFalse(ControlFilbeskrivelse.doControl(List.of(r), er));
    }

    @Test
    public void testOptionalDateFailDatePatternMissing() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "", false)
        );
        var record1 = "20200101";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailIncorrectValue() {
        var fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );
        var record1 = "FAIL!   ";
        r = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
        assertTrue(ControlFilbeskrivelse.doControl(List.of(r), er));
        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }
}
