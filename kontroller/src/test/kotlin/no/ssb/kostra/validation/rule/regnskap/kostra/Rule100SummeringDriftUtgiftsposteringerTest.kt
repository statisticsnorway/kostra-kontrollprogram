package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule100SummeringDriftUtgiftsposteringerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule100SummeringDriftUtgiftsposteringer(),
            forAllRows = listOf(
                ForAllRowItem(
                    "all conditions match",
                    kostraRecordsInTest("420400", 1, 100, 590, 0),
                    expectedErrorMessage = "Korrigér slik at fila inneholder utgiftsposteringene " +
                            "(0) i driftsregnskapet"
                ),
                ForAllRowItem(
                    "isOsloBydel() = true",
                    kostraRecordsInTest("030114", 1, 100, 590, 0)
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = false",
                    kostraRecordsInTest("420400", 0, 100, 590, 0)
                ),
                ForAllRowItem(
                    "isUtgift = false",
                    kostraRecordsInTest("420400", 1, 100, 600, 0),
                    expectedErrorMessage = "Korrigér slik at fila inneholder utgiftsposteringene " +
                            "(0) i driftsregnskapet"
                ),
                ForAllRowItem(
                    "belop > 0",
                    kostraRecordsInTest("420400", 1, 100, 590, 1)
                ),
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        fun kostraRecordsInTest(
            region: String,
            kontoklasse: Int,
            funksjon: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_REGION to region,
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "$funksjon",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord().asList()
    }
}