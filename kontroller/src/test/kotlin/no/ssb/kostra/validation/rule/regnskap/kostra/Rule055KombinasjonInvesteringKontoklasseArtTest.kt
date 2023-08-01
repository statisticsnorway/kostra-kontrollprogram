package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest

class Rule055KombinasjonInvesteringKontoklasseArtTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule055KombinasjonInvesteringKontoklasseArt(listOf("620")),
            forAllRows = listOf(
                ForAllRowItem(
                    "all conditions match",
                    kostraRecordsInTest(0, 620, 1),
                    expectedErrorMessage = "Kun advarsel, hindrer ikke innsending: (620) regnes å være ulogisk art i " +
                            "investeringsregnskapet. Vennligst vurder å postere på annen art eller om " +
                            "posteringen hører til i driftsregnskapet."
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = false",
                    kostraRecordsInTest(1, 620, 1)
                ),
                ForAllRowItem(
                    "art != 620",
                    kostraRecordsInTest(0, 621, 1)
                ),
                ForAllRowItem(
                    "belop = 0",
                    kostraRecordsInTest(0, 620, 0)
                )
            ),
            expectedSeverity = Severity.INFO,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}