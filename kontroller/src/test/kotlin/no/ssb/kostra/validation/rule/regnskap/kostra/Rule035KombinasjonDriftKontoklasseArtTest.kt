package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule035KombinasjonDriftKontoklasseArtTest : BehaviorSpec({
    Given("context") {
        val illogicalDriftArtList = listOf("520", "920")
        val sut = Rule035KombinasjonDriftKontoklasseArt(illogicalDriftArtList)

        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("0A", "1", "100", "1", false),
            row("0A", "1", "520", "1", true),
            row("0A", "1", "920", "1", true),
            row("0C", "1", "100", "1", false),
            row("0C", "1", "520", "1", true),
            row("0C", "1", "920", "1", true),
            row("0I", "3", "100", "1", false),
            row("0I", "3", "520", "1", true),
            row("0I", "3", "920", "1", true),
            row("0K", "3", "100", "1", false),
            row("0K", "3", "520", "1", true),
            row("0K", "3", "920", "1", true),
            row("0M", "3", "100", "1", false),
            row("0M", "3", "520", "1", true),
            row("0M", "3", "920", "1", true),
            row("0P", "3", "100", "1", false),
            row("0P", "3", "520", "1", true),
            row("0P", "3", "920", "1", true),
        ) { skjema, kontoklasse, art, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                        RegnskapConstants.FIELD_ART to art,
                        RegnskapConstants.FIELD_BELOP to belop,
                    )
                )
            )

            When("For $skjema, $kontoklasse, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.INFO,
                    "Kun advarsel, hindrer ikke innsending: (${art}) regnes å være ulogisk i " +
                            "driftsregnskapet, med mindre posteringen gjelder sosiale utlån og næringsutlån " +
                            "eller mottatte avdrag på sosiale utlån og næringsutlån, som finansieres av " +
                            "driftsinntekter."
                )
            }
        }
    }
})