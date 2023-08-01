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

class Rule050KombinasjonInvesteringKontoklasseArtTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule050KombinasjonInvesteringKontoklasseArt(listOf("990")),
            forAllRows = listOf(
                ForAllRowItem(
                    "all conditions match",
                    kostraRecordsInTest(0, 990, 1),
                    expectedErrorMessage = "Korrigér ugyldig art '990' i investeringsregnskapet til en " +
                            "gyldig art i investeringsregnskapet eller overfør posteringen til driftsregnskapet."
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = false",
                    kostraRecordsInTest(1, 990, 1)
                ),
                ForAllRowItem(
                    "art != 990",
                    kostraRecordsInTest(0, 991, 1)
                ),
                ForAllRowItem(
                    "belop == 0",
                    kostraRecordsInTest(0, 990, 0)
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        fun kostraRecordsInTest(
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