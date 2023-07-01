package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control28MaanederMedKvalifiseringsstonadTest : BehaviorSpec({
    val sut = Control28MaanederMedKvalifiseringsstonad()

    Given("context") {
        forAll(
            row(
                "permisjon",
                validKostraRecordInTest,
                false
            ),
            row(
                "status != permisjon, all months present",
                validKostraRecordInTest.copy(
                    valuesByName = mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        STATUS_COL_NAME to "1",
                        *((1..12).map {
                            "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                        }).toTypedArray()
                    )
                ),
                false
            ),
            row(
                "all months missing",
                validKostraRecordInTest.copy(
                    valuesByName = mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        STATUS_COL_NAME to "1",
                        *((1..12).map {
                            "$MONTH_PREFIX$it" to " "
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
                    "Det er ikke krysset av for hvilke måneder deltakeren har " +
                            "fått utbetalt kvalifiseringsstønad"
                )
            }
        }
    }
}) {
    companion object {
        private val validKostraRecordInTest = KostraRecord(
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                STATUS_COL_NAME to "2",
                *((1..12).map {
                    "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
