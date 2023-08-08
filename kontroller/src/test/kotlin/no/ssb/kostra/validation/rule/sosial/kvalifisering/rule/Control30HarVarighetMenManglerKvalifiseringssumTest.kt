package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.PERMISJON
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule028MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX


class Control30HarVarighetMenManglerKvalifiseringssumTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control30HarVarighetMenManglerKvalifiseringssum(),
            forAllRows = listOf(
                ForAllRowItem(
                    "with status = on leave, months and amount",
                    kostraRecordInTest(PERMISJON, true, "2"),
                ),
                ForAllRowItem(
                    "with status = on leave, no months and no amount",
                    kostraRecordInTest(PERMISJON, false, " "),
                ),
                ForAllRowItem(
                    "with status = active, set months and set amount",
                    kostraRecordInTest("1", true, "2"),
                ),
                ForAllRowItem(
                    "with status = active, no months, without amount",
                    kostraRecordInTest("1", false, "0"),
                ),
                ForAllRowItem(
                    "with status = active, months, without amount",
                    kostraRecordInTest("1", true, " "),
                    "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                            "kvalifiseringsstønad ( ) i løpet av året, eller feltet inneholder andre tegn enn " +
                            "tall. Feltet er obligatorisk å fylle ut.",
                )
            ),
            expectedSeverity = Severity.WARNING
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, setMonths: Boolean, stonad: String) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    STATUS_COL_NAME to status,
                    KVP_STONAD_COL_NAME to stonad,
                    *((1..12).map {
                        "$MONTH_PREFIX$it" to if (setMonths) it.toString().padStart(2, '0') else "  "
                    }).toTypedArray()
                )
            )
        )
    }
}
