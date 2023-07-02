package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control28MaanederMedKvalifiseringsstonadTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control28MaanederMedKvalifiseringsstonad(),
            forAllRows = listOf(
                ForAllRowItem(
                    "permisjon",
                    kostraRecordInTest
                ),
                ForAllRowItem(
                    "status != permisjon, all months present",
                    kostraRecordInTest.copy(
                        valuesByName = mapOf(
                            KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                            STATUS_COL_NAME to "1",
                            *((1..12).map {
                                "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                            }).toTypedArray()
                        )
                    )
                ),
                ForAllRowItem(
                    "all months missing",
                    kostraRecordInTest.copy(
                        valuesByName = mapOf(
                            KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                            STATUS_COL_NAME to "1",
                            *((1..12).map {
                                "$MONTH_PREFIX$it" to " "
                            }).toTypedArray()
                        )
                    ),
                    "Det er ikke krysset av for hvilke måneder deltakeren har " +
                            "fått utbetalt kvalifiseringsstønad"
                )
            ),
            expectedSeverity = Severity.WARNING
        )
    )
}) {
    companion object {
        private val kostraRecordInTest = kvalifiseringKostraRecordInTest(
            mapOf(
                STATUS_COL_NAME to "2",
                *((1..12).map {
                    "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            )
        )
    }
}
