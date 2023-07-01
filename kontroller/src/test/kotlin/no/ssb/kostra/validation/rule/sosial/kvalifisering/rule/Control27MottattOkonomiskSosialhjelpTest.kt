package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Control27MottattOkonomiskSosialhjelpTest : BehaviorSpec({
    val sut = Control27MottattOkonomiskSosialhjelp()

    Given("context") {
        forAll(
            row(
                "valid kvpMedAStonad, 1",
                1,4,false,"N/A", false
            ),
            row(
                "valid kvpMedAStonad, 2",
                2,0,true,"N/A", false
            ),
            row(
                "kvpMedAStonad = 3",
                3,0,true,"N/A", false
            ),

            row(
                "invalid kvpMedAStonad, kvpMedAStonad = 1", 1, 1, true,
                "Svaralternativer for feltet \"Har deltakeren i 2022 i løpet av perioden med kvalifiseringsstønad " +
                        "mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige " +
                        "koder. Feltet er obligatorisk å fylle ut. Det er mottatt støtte.",
                true
            ),
            row(
                "invalid kvpMedAStonad, kvpMedAStonad = 2", 2, 1, false,
                "Svaralternativer for feltet \"Har deltakeren i 2022 i løpet av perioden med kvalifiseringsstønad " +
                        "mottatt økonomisk sosialhjelp, kommunal bostøtte eller Husbankens bostøtte?\" har ugyldige " +
                        "koder. Feltet er obligatorisk å fylle ut. Det er IKKE mottatt støtte.",
                true
            )
        ) { description, kvpMedAStonad, kvpMedKommBos, useEmptyValues, expectedErrorMsg, expectError ->
            val context = kostraRecordInTest(
                kvpMedAStonad,
                kvpMedKommBos,
                useEmptyValues
            )

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    expectedErrorMsg
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            kvpMedAStonad: Int,
            kvpMedKommBos: Int = 4,
            useEmptyValues: Boolean
        ) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString(),

                KVP_MED_KOMMBOS_COL_NAME to kvpMedKommBos.toString(),
                KVP_MED_HUSBANK_COL_NAME to if (useEmptyValues) "0" else "5",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to if (useEmptyValues) " " else "9",
                KVP_MED_SOSHJ_PGM_COL_NAME to if (useEmptyValues) "0" else "8",
                KVP_MED_SOSHJ_SUP_COL_NAME to if (useEmptyValues) " " else "7"
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
