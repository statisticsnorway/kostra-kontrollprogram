package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Control26MottattStotteTest : BehaviorSpec({
    val sut = Control26MottattStotte()

    Given("context") {
        forAll(
            row(
                "valid kvpMedAStonad, 1",
                1, false
            ),
            row(
                "valid kvpMedAStonad, 2",
                2, false
            ),
            row(
                "invalid kvpMedAStonad",
                42, true
            )
        ) { description, kvpMedAStonad, expectError ->
            val context = kostraRecordInTest(kvpMedAStonad)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Feltet for 'Har deltakeren i 2022 i løpet av perioden med " +
                            "kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, kommunal bostøtte eller " +
                            "Husbankens bostøtte?', er ikke utfylt eller feil kode (42) er benyttet. Feltet er " +
                            "obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(kvpMedAStonad: Int) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString()
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
