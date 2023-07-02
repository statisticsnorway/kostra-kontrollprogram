package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule055KombinasjonInvesteringKontoklasseArtTest : BehaviorSpec({
    Given("context") {
        val illogicalInvesteringArtList = listOf("620", "650", "900")
        val sut = Rule055KombinasjonInvesteringKontoklasseArt(illogicalInvesteringArtList)

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
            row("0P", "4", "900", "1", true)
        ) { skjema, kontoklasse, art, belop, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_BELOP to belop,
            ).toKostraRecord().asList()

            When("For $skjema, $kontoklasse, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.INFO,
                    "Kun advarsel, hindrer ikke innsending: (${art}) regnes å være ulogisk art i " +
                            "investeringsregnskapet. Vennligst vurder å postere på annen art eller om " +
                            "posteringen hører til i driftsregnskapet."
                )
            }
        }
    }
})