package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control30HarVarighetMenManglerKvalifiseringssumTest : BehaviorSpec({
    val sut = Control30HarVarighetMenManglerKvalifiseringssum()

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
                "with months, without amount",
                validKostraRecordInTest.copy(
                    valuesByName = mapOf(
                        KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                        KVP_STONAD_COL_NAME to " ",
                        *((1..12).map {
                            "${MONTH_PREFIX}$it" to it.toString().padStart(2, '0')
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
                    "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                            "kvalifiseringsstønad ( ) i løpet av året, eller feltet inneholder andre tegn enn " +
                            "tall. Feltet er obligatorisk å fylle ut."
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
                    "${MONTH_PREFIX}$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
