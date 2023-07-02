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
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.fourDigitReportingYear

class Control27MottattOkonomiskSosialhjelpTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control27MottattOkonomiskSosialhjelp(),
            forAllRows = listOf(
                ForAllRowItem(
                    "valid kvpMedAStonad, 1",
                    kostraRecordInTest(1, 4, false)
                ),
                ForAllRowItem(
                    "valid kvpMedAStonad, 2",
                    kostraRecordInTest(2, 0, true)
                ),
                ForAllRowItem(
                    "kvpMedAStonad = 3",
                    kostraRecordInTest(3, 0, true)
                ),

                ForAllRowItem(
                    "invalid kvpMedAStonad, kvpMedAStonad = 1",
                    kostraRecordInTest(1, 1, true),
                    "Svaralternativer for feltet \"Har deltakeren i $fourDigitReportingYear i løpet av perioden " +
                            "med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens " +
                            "bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er mottatt støtte."
                ),
                ForAllRowItem(
                    "invalid kvpMedAStonad, kvpMedAStonad = 2",
                    kostraRecordInTest(2, 1, false),
                    "Svaralternativer for feltet \"Har deltakeren i $fourDigitReportingYear i løpet av perioden " +
                            "med kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens " +
                            "bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            kvpMedAStonad: Int,
            kvpMedKommBos: Int = 4,
            useEmptyValues: Boolean
        ) = KvalifiseringTestUtils.kvalifiseringKostraRecordInTest(
            mapOf(
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString(),
                KVP_MED_KOMMBOS_COL_NAME to kvpMedKommBos.toString(),
                KVP_MED_HUSBANK_COL_NAME to if (useEmptyValues) "0" else "5",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to if (useEmptyValues) " " else "9",
                KVP_MED_SOSHJ_PGM_COL_NAME to if (useEmptyValues) "0" else "8",
                KVP_MED_SOSHJ_SUP_COL_NAME to if (useEmptyValues) " " else "7"
            )
        )
    }
}
