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
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord


class Rule110SummeringDriftDifferanseTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule110SummeringDriftDifferanse(),
            forAllRows = listOf(
                ForAllRowItem(
                    "all conditions match, negative amount",
                    listOf(
                        kostraRecordInTest("420400", 1, 590, 1),
                        kostraRecordInTest("420400", 1, 600, -32)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-31) mellom inntekter " +
                            "(-32) og utgifter (1) i driftsregnskapet"
                ),
                ForAllRowItem(
                    "all conditions match, positive amount",
                    listOf(
                        kostraRecordInTest("420400", 1, 590, 32),
                        kostraRecordInTest("420400", 1, 600, -1)
                    ),
                    expectedErrorMessage = "Korrigér differansen (31) mellom inntekter " +
                            "(-1) og utgifter (32) i driftsregnskapet"
                ),
                ForAllRowItem(
                    "isOsloBydel = true",
                    listOf(
                        kostraRecordInTest("030102", 1, 590, 1),
                        kostraRecordInTest("030102", 1, 600, -32)
                    )
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = false",
                    listOf(
                        kostraRecordInTest("420400", 0, 590, 1),
                        kostraRecordInTest("420400", 0, 600, -32)
                    )
                ),
                ForAllRowItem(
                    "driftUtgifter < 0",
                    listOf(
                        kostraRecordInTest("420400", 1, 590, -1),
                        kostraRecordInTest("420400", 1, 600, -1)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-2) mellom inntekter " +
                            "(-1) og utgifter (-1) i driftsregnskapet"
                ),
                ForAllRowItem(
                    "driftInntekter > 0",
                    listOf(
                        kostraRecordInTest("420400", 1, 590, 1),
                        kostraRecordInTest("420400", 1, 600, 1)
                    ),
                    expectedErrorMessage = "Korrigér differansen (2) mellom inntekter " +
                            "(1) og utgifter (1) i driftsregnskapet"
                ),
                ForAllRowItem(
                    "driftUtgifter + driftInntekter within range, lower bound",
                    listOf(
                        kostraRecordInTest("420400", 0, 590, 1),
                        kostraRecordInTest("420400", 0, 600, -31)
                    )
                ),
                ForAllRowItem(
                    "driftUtgifter + driftInntekter within range, upper bound",
                    listOf(
                        kostraRecordInTest("420400", 0, 590, 31),
                        kostraRecordInTest("420400", 0, 600, 1)
                    )
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            region: String,
            kontoklasse: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            FIELD_REGION to region,
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "100",
            FIELD_ART to "$art",
            FIELD_BELOP to "$belop"
        ).toKostraRecord()
    }
}