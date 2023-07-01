package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule040KombinasjonInvesteringKontoklasseFunksjonTest : BehaviorSpec({

    Given("context") {
        val sut = Rule040KombinasjonInvesteringKontoklasseFunksjon(
            listOf("800 ", "840 ", "850 ", "860 ")
        )
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA),
            FieldDefinition(from = 3, to = 3, name = RegnskapConstants.FIELD_KONTOKLASSE),
            FieldDefinition(from = 4, to = 7, name = RegnskapConstants.FIELD_FUNKSJON),
        ).associateBy { it.name }

        forAll(
            row("0A", "0", "100 ", "1", false),
            row("0A", "0", "800 ", "1", true),
            row("0A", "0", "850 ", "1", true),
            row("0A", "0", "840 ", "1", true),
            row("0A", "0", "860 ", "1", true),
            row("0C", "0", "100 ", "1", false),
            row("0C", "0", "800 ", "1", true),
            row("0C", "0", "850 ", "1", true),
            row("0C", "0", "840 ", "1", true),
            row("0C", "0", "860 ", "1", true),
            row("0I", "4", "100 ", "1", false),
            row("0I", "4", "800 ", "1", true),
            row("0I", "4", "850 ", "1", true),
            row("0I", "4", "840 ", "1", true),
            row("0I", "4", "860 ", "1", true),
            row("0K", "4", "100 ", "1", false),
            row("0K", "4", "800 ", "1", true),
            row("0K", "4", "850 ", "1", true),
            row("0K", "4", "840 ", "1", true),
            row("0K", "4", "860 ", "1", true),
            row("0M", "4", "100 ", "1", false),
            row("0M", "4", "800 ", "1", true),
            row("0M", "4", "850 ", "1", true),
            row("0M", "4", "840 ", "1", true),
            row("0M", "4", "860 ", "1", true),
            row("0P", "4", "100 ", "1", false),
            row("0P", "4", "800 ", "1", true),
            row("0P", "4", "850 ", "1", true),
            row("0P", "4", "840 ", "1", true),
            row("0P", "4", "860 ", "1", true),
        ) { skjema, kontoklasse, funksjon, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                        RegnskapConstants.FIELD_FUNKSJON to funksjon,
                        RegnskapConstants.FIELD_BELOP to belop,
                    )
                )
            )

            When("For $skjema, $kontoklasse, $funksjon -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér ugyldig funksjon '${funksjon}' i " +
                            "investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller " +
                            "overfør posteringen til driftsregnskapet."
                )
            }
        }
    }
})