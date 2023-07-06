package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule020KombinasjonDriftKontoklasseFunksjonTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule020KombinasjonDriftKontoklasseFunksjon(listOf("841")),
            forAllRows = listOf(
                ForAllRowItem(
                    "all conditions match",
                    kostraRecordsInTest(1, 841, 1),
                    expectedErrorMessage = "Korrigér ugyldig funksjon '841' i driftsregnskapet " +
                            "til en gyldig funksjon i driftsregnskapet eller overfør posteringen til " +
                            "investeringsregnskapet."
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = false",
                    kostraRecordsInTest(0, 841, 1)
                ),
                ForAllRowItem(
                    "funksjon != 841",
                    kostraRecordsInTest(1, 842, 1)
                ),
                ForAllRowItem(
                    "belop == 0",
                    kostraRecordsInTest(1, 841, 0)
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: Int,
            funksjon: Int,
            belop: Int
        ) = mapOf(
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "$funksjon",
            FIELD_BELOP to "$belop"
        ).toKostraRecord().asList()
    }
}