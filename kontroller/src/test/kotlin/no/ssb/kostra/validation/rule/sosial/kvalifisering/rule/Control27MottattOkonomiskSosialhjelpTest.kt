package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control27MottattOkonomiskSosialhjelpTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control27MottattOkonomiskSosialhjelp(),
            forAllRows = listOf(
                ForAllRowItem(
                    "answer Ja, all fields blank",
                    recordInTestAllValuesBlank(1)
                ),
                ForAllRowItem(
                    "answer Ja, all fields '0'",
                    recordInTestAllValuesZero(1)
                ),
                ForAllRowItem(
                    "answer Ja, all fields populated with valid values",
                    recordInTestAllValuesPopulated(1)
                ),

                ForAllRowItem(
                    "answer Nei, all fields blank",
                    recordInTestAllValuesBlank(2)
                ),
                ForAllRowItem(
                    "answer Nei, all fields '0'",
                    recordInTestAllValuesZero(2)
                ),

                ForAllRowItem(
                    "KVP_MED_ASTONAD out of range",
                    recordInTestAllValuesBlank(3)
                ),

                ForAllRowItem(
                    "invalid kvpMedKommBos",
                    recordInTestAllValuesPopulated(1, 1),
                    "Svaralternativer for feltet \"Har deltakeren i ${argumentsInTest.aargang} " +
                            "i løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal " +
                            "bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å " +
                            "fylle ut. Det er mottatt støtte."
                ),
                ForAllRowItem(
                    "invalid kvpMedAStonad, kvpMedAStonad = 2",
                    recordInTestAllValuesPopulated(2),
                    "Svaralternativer for feltet \"Har deltakeren i ${argumentsInTest.aargang} i " +
                            "løpet av perioden med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal " +
                            "bostøtte eller Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å " +
                            "fylle ut. Det er IKKE mottatt støtte."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun recordInTestAllValuesPopulated(
            kvpMedAStonad: Int,
            kvpMedKommBos: Int = 4
        ) = kvalifiseringKostraRecordInTest(
            mapOf(
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString(),
                KVP_MED_KOMMBOS_COL_NAME to "$kvpMedKommBos",
                KVP_MED_HUSBANK_COL_NAME to "5",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to "9",
                KVP_MED_SOSHJ_PGM_COL_NAME to "8",
                KVP_MED_SOSHJ_SUP_COL_NAME to "7"
            )
        )

        private fun recordInTestAllValuesBlank(kvpMedAStonad: Int) = kvalifiseringKostraRecordInTest(
            mapOf(
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString(),
                KVP_MED_KOMMBOS_COL_NAME to " ",
                KVP_MED_HUSBANK_COL_NAME to " ",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to " ",
                KVP_MED_SOSHJ_PGM_COL_NAME to " ",
                KVP_MED_SOSHJ_SUP_COL_NAME to " "
            )
        )

        private fun recordInTestAllValuesZero(kvpMedAStonad: Int) = kvalifiseringKostraRecordInTest(
            mapOf(
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString(),
                KVP_MED_KOMMBOS_COL_NAME to "0",
                KVP_MED_HUSBANK_COL_NAME to "0",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to "0",
                KVP_MED_SOSHJ_PGM_COL_NAME to "0",
                KVP_MED_SOSHJ_SUP_COL_NAME to "0"
            )
        )
    }
}
