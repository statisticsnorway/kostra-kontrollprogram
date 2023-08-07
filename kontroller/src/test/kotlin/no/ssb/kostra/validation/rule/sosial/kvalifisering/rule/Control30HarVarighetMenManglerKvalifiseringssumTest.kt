package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule028MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX
import org.junit.Ignore


class Control30HarVarighetMenManglerKvalifiseringssumTest : BehaviorSpec({
    // TODO()
//    include(
//        validationRuleTest(
//            sut = Control30HarVarighetMenManglerKvalifiseringssum(),
//            forAllRows = listOf(
//                ForAllRowItem(
//                    "with months and amount",
//                    kostraRecordInTest("2", true),
//                ),
//                ForAllRowItem(
//                    "without months, without amount",
//                    kostraRecordInTest("0", false),
//                ),
//                ForAllRowItem(
//                    "with months, without amount",
//                    kostraRecordInTest(" ", true),
//                    "Det er ikke oppgitt hvor mye deltakeren har fått i " +
//                            "kvalifiseringsstønad ( ) i løpet av året, eller feltet inneholder andre tegn enn " +
//                            "tall. Feltet er obligatorisk å fylle ut.",
//                )
//            ),
//            expectedSeverity = Severity.WARNING
//        )
//    )
}) {
    companion object {
        private fun kostraRecordInTest(stonad: String, setValues: Boolean) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    KVP_STONAD_COL_NAME to stonad,
                    *((1..12).map {
                        "$MONTH_PREFIX$it" to if (setValues) it.toString().padStart(2, '0') else " "
                    }).toTypedArray()
                )
            )
        )
    }
}
