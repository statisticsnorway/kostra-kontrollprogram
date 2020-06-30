package no.ssb.kostra.felles;

import no.ssb.kostra.control.*;
import no.ssb.kostra.control.felles.ControlFilbeskrivelse;
import no.ssb.kostra.controlprogram.Arguments;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControlFilbeskrivelseTest {
    private Arguments args;
    private ErrorReport er;
    private ErrorReportEntry ere;
    private List<FieldDefinition> fieldDefinitions;
    private Record r;

    @Before
    public void beforeTest() {
        args = new Arguments(new String[]{"-s", "0T", "-y", "2020", "-r", "030100"});
        er = new ErrorReport(args);
    }

    @Test
    public void testFieldDefinitionLengthGreaterThan0() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "OK   ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testFieldDefinitionLengthEqualTo0() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, -1, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }



    @Test
    public void testOptionalStringOK1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "OK   ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalStringOK2() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "     ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringOK() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "OKstr";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringFail() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "     ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalStringWithCodeListOK1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "ABCDE";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalStringWithCodeListOK2() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "     ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalStringWithCodeListFail() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringWithCodeListOK() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "ABCDE";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringWithCodeListFail1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryStringWithCodeListFail2() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "     ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalIntegerOk1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "    1";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalIntegerOk2() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "     ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalIntegerFail() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryIntegerOk1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "    1";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryIntegerFail1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "     ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryIntegerFail2() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


    @Test
    public void testMandatoryDateOk1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "20200101";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailIncorrectDate() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "20202020";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailValueMissing() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "        ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailDatePatternMissing() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "20200101";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testMandatoryDateFailIncorrectValue() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!   ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    //////////////////////////////////////////////

    @Test
    public void testOptionalDateOk1() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "20200101";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailIncorrectDate() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "20202020";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailValueMissing() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "        ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.NO_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailDatePatternMissing() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "20200101";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }

    @Test
    public void testOptionalDateFailIncorrectValue() {
        List<FieldDefinition> fieldDefinitions = List.of(
                new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
        );

        //                000000000111111111122222222223333333333
        //                123456789012345678901234567890123456789
        String record1 = "FAIL!   ";
        r = new Record(record1, fieldDefinitions);

        ControlFilbeskrivelse.doControl(r, er, 1);

        if (Constants.DEBUG){
            System.out.print(er.generateReport());
        }

        assertEquals(Constants.CRITICAL_ERROR, er.getErrorType());
    }


}
