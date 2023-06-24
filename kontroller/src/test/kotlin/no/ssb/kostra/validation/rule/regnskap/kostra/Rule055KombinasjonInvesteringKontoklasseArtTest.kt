package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule055KombinasjonInvesteringKontoklasseArtTest : BehaviorSpec({
    Given("context") {
        val illogicalInvesteringArtList = listOf("620", "650", "900")
        val sut = Rule055KombinasjonInvesteringKontoklasseArt(illogicalInvesteringArtList)

        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("0A", "0", "010", "1", false),
            row("0A", "0", "620", "1", true),
            row("0A", "0", "650", "1", true),
            row("0A", "0", "900", "1", true),
            row("0C", "0", "010", "1", false),
            row("0C", "0", "620", "1", true),
            row("0C", "0", "650", "1", true),
            row("0C", "0", "900", "1", true),
            row("0I", "4", "010", "1", false),
            row("0I", "4", "620", "1", true),
            row("0I", "4", "650", "1", true),
            row("0I", "4", "900", "1", true),
            row("0K", "4", "010", "1", false),
            row("0K", "4", "620", "1", true),
            row("0K", "4", "650", "1", true),
            row("0K", "4", "900", "1", true),
            row("0M", "4", "010", "1", false),
            row("0M", "4", "620", "1", true),
            row("0M", "4", "650", "1", true),
            row("0M", "4", "900", "1", true),
            row("0P", "4", "010", "1", false),
            row("0P", "4", "620", "1", true),
            row("0P", "4", "650", "1", true),
            row("0P", "4", "900", "1", true),
        ) { skjema, kontoklasse, art, belop, expectedResult ->
            When("For $skjema, $kontoklasse, $art -> $expectedResult") {
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

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.INFO)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Kun advarsel, hindrer ikke innsending: (${art}) regnes å være ulogisk art i " +
                                    "investeringsregnskapet. Vennligst vurder å postere på annen art eller om " +
                                    "posteringen hører til i driftsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})