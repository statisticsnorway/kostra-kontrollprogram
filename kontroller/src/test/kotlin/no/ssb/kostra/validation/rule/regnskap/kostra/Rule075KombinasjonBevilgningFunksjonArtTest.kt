package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule075KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule075KombinasjonBevilgningFunksjonArt()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row("0A", "800 ", "010", "1", false),
            row("0A", "800 ", "870", "1", false),
            row("0A", "800 ", "874", "1", false),
            row("0A", "800 ", "875", "1", false),
            row("0A", "800 ", "877", "1", false),
            row("0A", "100 ", "870", "1", true),
            row("0A", "100 ", "874", "1", false), /** TODO Jon Ole: Denne gir ikek feil */
            row("0A", "100 ", "875", "1", true),
            row("0A", "100 ", "877", "1", false),
            row("0C", "800 ", "010", "1", false),
            row("0C", "800 ", "870", "1", false),
            row("0C", "800 ", "877", "1", false),
            row("0C", "100 ", "870", "1", true),
            row("0C", "100 ", "874", "1", false), /** TODO Jon Ole: Denne gir ikek feil */
            row("0C", "100 ", "875", "1", true),
            row("0C", "100 ", "877", "1", false),
            row("0I", "800 ", "010", "1", false),
            row("0I", "800 ", "870", "1", false),
            row("0I", "800 ", "874", "1", false),
            row("0I", "800 ", "875", "1", false),
            row("0I", "800 ", "877", "1", false),
            row("0I", "100 ", "870", "1", true),
            row("0I", "100 ", "874", "1", false), /** TODO Jon Ole: Denne gir ikek feil */
            row("0I", "100 ", "875", "1", true),
            row("0I", "100 ", "877", "1", false),
            row("0K", "800 ", "010", "1", false),
            row("0K", "800 ", "870", "1", false),
            row("0K", "800 ", "874", "1", false),
            row("0K", "800 ", "875", "1", false),
            row("0K", "800 ", "877", "1", false),
            row("0K", "100 ", "870", "1", true),
            row("0K", "100 ", "874", "1", false), /** TODO Jon Ole: Denne gir ikek feil */
            row("0K", "100 ", "875", "1", true),
            row("0K", "100 ", "877", "1", false),
            row("0M", "800 ", "010", "1", false),
            row("0M", "800 ", "870", "1", false),
            row("0M", "800 ", "874", "1", false),
            row("0M", "800 ", "875", "1", false),
            row("0M", "800 ", "877", "1", false),
            row("0M", "100 ", "870", "1", true),
            row("0M", "100 ", "874", "1", false), /** TODO Jon Ole: Denne gir ikek feil */
            row("0M", "100 ", "875", "1", true),
            row("0M", "100 ", "877", "1", false),
            row("0P", "800 ", "010", "1", false),
            row("0P", "800 ", "870", "1", false),
            row("0P", "800 ", "874", "1", false),
            row("0P", "100 ", "870", "1", true),
            row("0P", "100 ", "874", "1", false), /** TODO Jon Ole: Denne gir ikek feil */
            row("0P", "100 ", "875", "1", true),
            row("0P", "100 ", "877", "1", false)
        ) { skjema, funksjon, art, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_FUNKSJON to funksjon,
                        RegnskapConstants.FIELD_ART to art,
                        RegnskapConstants.FIELD_BELOP to belop,
                    )
                )
            )

            When("For $skjema, $funksjon, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Artene 870, 871, 872, 873, 875 og 876 er kun tillat brukt i kombinasjon med funksjon 800."
                )
            }
        }
    }
})