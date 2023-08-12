package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils

class Rule540EiendelerErLikEgenkaptialPlussGjeldTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule540EiendelerErLikEgenkaptialPlussGjeld(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "feil skjema",
                listOf(
                    kostraRecordInTest("0X", "100", 0),
                )
            ),
            *setOf(Pair(-551, -51), Pair(-449, 51)).map { (sumGjeld, differanse) ->
                ForAllRowItem(
                    "riktig skjema, sektor, men beløp utenfor interval, differanse = $differanse",
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
                    "riktig skjema, sektor og beløp innenfor interval, differanse = $differanse",
                    listOf(
                        kostraRecordInTest("0Y", "100", 1100),
                        kostraRecordInTest("0Y", "200", -600),
                        kostraRecordInTest("0Y", "210", sumGjeld),
                    )
                )
            }.toTypedArray(),
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
