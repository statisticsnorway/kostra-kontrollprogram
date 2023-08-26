package no.ssb.kostra.validation.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.STRING_TYPE
import no.ssb.kostra.validation.report.Severity

class Rule002FileDescriptionTest : BehaviorSpec({
    Given("context and field definitions") {
        forAll(
            row(
                "Optional string",
                FieldDefinition(number = 1, name = "OptionalString", STRING_TYPE, from = 1, to = 5),
                "     ",
                null,
            ),

        ) { description, fieldDefinition, recordString, expectedResult ->
            When(description) {
                val sut = Rule002FileDescription(listOf(fieldDefinition))
                val result = sut.validate(
                    context = listOf(recordString)
                )

                Then("result should be null") {
                    result.shouldNotBeNull()
                    result.size shouldBe 1
                    result.first().severity shouldBe expectedResult
                }
            }
        }
    }
})

/*

@Test
public void testFieldDefinitionLengthGreaterThan0() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
    );
    var record1 = "OK   ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testFieldDefinitionLengthEqualTo0() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, -1, new ArrayList<>(), "", false)
    );
    var record1 = "FAIL!";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}


@Test
public void testOptionalStringOK1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
    );
    var record1 = "OK   ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalStringOK2() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", false)
    );

    var record1 = "     ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testMandatoryStringOK() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", true)
    );
    var record1 = "OKstr";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testMandatoryStringFail() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, new ArrayList<>(), "", true)
    );
    var record1 = "     ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testOptionalStringWithCodeListOK1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
    );
    var record1 = "ABCDE";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalStringWithCodeListOK2() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
    );
    var record1 = "     ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalStringWithCodeListFail() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "optionalString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", false)
    );
    var record1 = "FAIL!";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryStringWithCodeListOK() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
    );
    var record1 = "ABCDE";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testMandatoryStringWithCodeListFail1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
    );
    var record1 = "FAIL!";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryStringWithCodeListFail2() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryString", "String", "", 1, 5, List.of(new Code("ABCDE", "ABCDE")), "", true)
    );
    var record1 = "     ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testOptionalIntegerOk1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
    );
    var record1 = "    1";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalIntegerOk2() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
    );
    var record1 = "     ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalIntegerFail() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalInteger", "Integer", "", 1, 5, new ArrayList<>(), "", false)
    );
    var record1 = "FAIL!";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryIntegerOk1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
    );
    var record1 = "    1";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testMandatoryIntegerFail1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
    );
    var record1 = "     ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryIntegerFail2() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryInteger", "Integer", "", 1, 5, new ArrayList<>(), "", true)
    );
    var record1 = "FAIL!";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}


@Test
public void testMandatoryDateOk1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
    );
    var record1 = "20200101";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testMandatoryDateFailIncorrectDate() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
    );
    var record1 = "20202020";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryDateFailValueMissing() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
    );
    var record1 = "        ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryDateFailDatePatternMissing() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "", true)
    );
    var record1 = "20200101";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testMandatoryDateFailIncorrectValue() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", true)
    );
    var record1 = "FAIL!   ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testOptionalDateFailInvalidDate() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "MandatoryDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
    );
    var record1 = "20200000";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testOptionalDateOk1() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
    );
    var record1 = "20200101";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalDateFailIncorrectDate() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
    );
    var record1 = "20202020";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testOptionalDateFailValueMissing() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
    );
    var record1 = "        ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertFalse(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
}

@Test
public void testOptionalDateFailDatePatternMissing() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "", false)
    );
    var record1 = "20200101";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}

@Test
public void testOptionalDateFailIncorrectValue() {
    var fieldDefinitions = List.of(
        new FieldDefinition(1, "OptionalDate", "Date", "", 1, 8, new ArrayList<>(), "yyyyMMdd", false)
    );
    var record1 = "FAIL!   ";
    kostraRecord = Utils.addLineNumbering(new KostraRecord(record1, fieldDefinitions));
    assertTrue(ControlFilbeskrivelse.doControl(List.of(kostraRecord), errorReport));
    assertEquals(Constants.CRITICAL_ERROR, errorReport.getErrorType());
}
*/