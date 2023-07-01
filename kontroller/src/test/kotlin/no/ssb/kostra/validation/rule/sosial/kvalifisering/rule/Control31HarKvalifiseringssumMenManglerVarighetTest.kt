package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control31HarKvalifiseringssumMenManglerVarighetTest : BehaviorSpec({
    val sut = Control31HarKvalifiseringssumMenManglerVarighet()

    Given("context") {
        forAll(
            row(
                "with months and amount",
                validKostraRecordInTest,
                false
            ),
            row(
                "without months, without amount",
                validKostraRecordInTest.copy(
                    valuesByName = mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        KVP_STONAD_COL_NAME to " ",
                        *((1..12).map {
                            "${MONTH_PREFIX}$it" to "  "
                        }).toTypedArray()
                    )
                ), false
            ),
            row(
                "without months, with amount",
                validKostraRecordInTest.copy(
                    valuesByName = mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        KVP_STONAD_COL_NAME to "1",
                        *((1..12).map {
                            "$MONTH_PREFIX$it" to "  "
                        }).toTypedArray()
                    )
                ), true
            )
        ) { description, context, expectError ->
            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(context, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Deltakeren har fått kvalifiseringsstønad (1) i løpet av året, " +
                            "men mangler utfylling for hvilke måneder stønaden gjelder. " +
                            "Feltet er obligatorisk å fylle ut."
                )
            }
        }
    }
}) {
    companion object {
        private val validKostraRecordInTest = KostraRecord(
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_STONAD_COL_NAME to "2",
                *((1..12).map {
                    "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            ),
            fieldDefinitionByName = KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
