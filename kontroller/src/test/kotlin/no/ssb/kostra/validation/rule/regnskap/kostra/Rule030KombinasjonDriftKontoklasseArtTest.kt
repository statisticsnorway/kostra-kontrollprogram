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

class Rule030KombinasjonDriftKontoklasseArtTest : BehaviorSpec({
    Given("context") {
        val illogicalDriftArtList = listOf("285", "660")
        val sut = Rule030KombinasjonDriftKontoklasseArt(illogicalDriftArtList)

        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0A", "1", "100", "1", false),
            row("0A", "1", "285", "1", true),
            row("0A", "1", "660", "1", true),
            row("0C", "1", "100", "1", false),
            row("0C", "1", "285", "1", true),
            row("0C", "1", "660", "1", true),
            row("0I", "3", "100", "1", false),
            row("0I", "3", "285", "1", true),
            row("0I", "3", "660", "1", true),
            row("0K", "3", "100", "1", false),
            row("0K", "3", "285", "1", true),
            row("0K", "3", "660", "1", true),
            row("0M", "3", "100", "1", false),
            row("0M", "3", "285", "1", true),
            row("0M", "3", "660", "1", true),
            row("0P", "3", "100", "1", false),
            row("0P", "3", "285", "1", true),
            row("0P", "3", "660", "1", true),
        ) { skjema, kontoklasse, art, belop, expectedResult ->
            When("For $skjema, $kontoklasse, $art, $belop -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
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
                            "Kun advarsel, hindrer ikke innsending: (" +
                                    "'${art}') regnes å være " +
                                    "ulogisk art i driftsregnskapet. Vennligst vurder å postere på annen art eller om " +
                                    "posteringen hører til i investeringsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})