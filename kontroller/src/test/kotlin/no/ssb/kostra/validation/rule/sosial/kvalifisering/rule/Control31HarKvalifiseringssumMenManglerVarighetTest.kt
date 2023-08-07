package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule028MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control31HarKvalifiseringssumMenManglerVarighetTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control31HarKvalifiseringssumMenManglerVarighet(),
            forAllRows = listOf(
                ForAllRowItem(
                    "with months and amount",
                    validKostraRecordInTest,
                ),
                ForAllRowItem(
                    "without months, without amount",
                    validKostraRecordInTest.copy(
                        valuesByName = mapOf(
                            KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                            KVP_STONAD_COL_NAME to " ",
                            *((1..12).map {
                                "${MONTH_PREFIX}$it" to "  "
                            }).toTypedArray()
                        )
                    ),
                ),
                ForAllRowItem(
                    "without months, with amount",
                    validKostraRecordInTest.copy(
                        valuesByName = mapOf(
                            KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                            KVP_STONAD_COL_NAME to "1",
                            *((1..12).map {
                                "$MONTH_PREFIX$it" to "  "
                            }).toTypedArray()
                        )
                    ),
                    "Deltakeren har fått kvalifiseringsstønad (1) i løpet av året, " +
                            "men mangler utfylling for hvilke måneder stønaden gjelder. " +
                            "Feltet er obligatorisk å fylle ut.",
                )
            ),
            expectedSeverity = Severity.WARNING
        )
    )
}) {
    companion object {
        private val validKostraRecordInTest = kvalifiseringKostraRecordInTest(
            mapOf(
                KVP_STONAD_COL_NAME to "2",
                *((1..12).map {
                    "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            )
        )
    }
}
