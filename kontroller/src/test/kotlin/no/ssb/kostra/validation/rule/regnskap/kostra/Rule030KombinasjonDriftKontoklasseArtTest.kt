package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule030KombinasjonDriftKontoklasseArtTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule030KombinasjonDriftKontoklasseArt(listOf("285")),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBevilgningRegnskap = true, art matching, belop matching",
                    kostraRecordsInTest("1", "285", "1"),
                    expectedErrorMessage = "Kun advarsel, hindrer ikke innsending: ('285') regnes å være " +
                            "ulogisk art i driftsregnskapet. Vennligst vurder å postere på annen art eller om " +
                            "posteringen hører til i investeringsregnskapet."
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = false, art matching, belop matching",
                    kostraRecordsInTest("0", "285", "1")
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = true, art not matching, belop matching",
                    kostraRecordsInTest("1", "284", "1")
                ),
                ForAllRowItem(
                    "isBevilgningRegnskap = true, art matching, belop not matching",
                    kostraRecordsInTest("1", "285", "0")
                ),
            ),
            expectedSeverity = Severity.INFO,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: String,
            art: String,
            belop: String
        ): List<KostraRecord> = mapOf(
            RegnskapConstants.FIELD_SKJEMA to "0A",
            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
            RegnskapConstants.FIELD_ART to art,
            RegnskapConstants.FIELD_BELOP to belop,
        ).toKostraRecord().asList()
    }
}