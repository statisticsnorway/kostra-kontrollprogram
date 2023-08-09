package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils

class Rule540EiendelerErLikEgenkaptialPlussGjeldTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule540EiendelerErLikEgenkaptialPlussGjeld(),
            forAllRows = listOf(
                ForAllRowItem(
                    "feil skjema",
                    listOf(
                        kostraRecordInTest("0X", "100", 0),
                    )
                ),
                *setOf(99, 196, 199, 300).map {
                    ForAllRowItem(
                        "riktig skjema, men feil sektor ($it)",
                        listOf(
                            kostraRecordInTest("0Y", "$it", 0),
                        )
                    )
                }.toTypedArray(),
                *setOf(Pair(-551, -51), Pair(-449, 51)).map { (sumGjeld, differanse) ->
                    ForAllRowItem(
                        "riktig skjema, sektor, men feil beløp, differanse = $differanse",
                        listOf(
                            kostraRecordInTest("0Y", "100", 1000),
                            kostraRecordInTest("0Y", "200", -500),
                            kostraRecordInTest("0Y", "210", sumGjeld),
                        ),
                        expectedErrorMessage = "Balansen ($differanse) skal balansere ved at sum eiendeler (1000)  = sum " +
                                "egenkapital (-500) + sum gjeld ($sumGjeld) . Differanser +/- 50' kroner godtas"
                    )
                }.toTypedArray(),
                *setOf(Pair(-550, -50), Pair(-450, 50)).map { (sumGjeld, differanse) ->
                    ForAllRowItem(
                        "riktig skjema, sektor og beløp, differanse = $differanse",
                        listOf(
                            kostraRecordInTest("0Y", "100", 1000),
                            kostraRecordInTest("0Y", "200", -500),
                            kostraRecordInTest("0Y", "210", sumGjeld),
                        )
                    )
                }.toTypedArray(),
                ForAllRowItem(
                    "riktig skjema og sektor, men feil beløp",
                    listOf(
                        kostraRecordInTest("0Y", "100", 1200),
                        kostraRecordInTest("0Y", "200", -100),
                        kostraRecordInTest("0Y", "210", -100),
                    ),
                    expectedErrorMessage = "Balansen (1000) skal balansere ved at sum eiendeler (1200)  = sum " +
                            "egenkapital (-100) + sum gjeld (-100) . Differanser +/- 50' kroner godtas"
                ),
                ForAllRowItem(
                    "riktig skjema, sektor og beløp, differanse = -50",
                    listOf(
                        kostraRecordInTest("0Y", "100", 1000),
                        kostraRecordInTest("0Y", "200", -500),
                        kostraRecordInTest("0Y", "210", -550),
                    ),
                ),
                ForAllRowItem(
                    "riktig skjema, sektor og beløp, differanse = +50",
                    listOf(
                        kostraRecordInTest("0Y", "100", 1000),
                        kostraRecordInTest("0Y", "200", -500),
                        kostraRecordInTest("0Y", "210", -450),
                    ),
                )
            ),
            expectedSeverity = Severity.WARNING
        )
    )

}) {
    companion object {
        private fun kostraRecordInTest(
            skjema: String,
            sektor: String,
            belop: Int
        ) = RegnskapTestUtils.regnskapRecordInTest(
            mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_SEKTOR to sektor,
                FIELD_BELOP to belop.toString()
            )
        )
    }
}
