package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Control36StatusForDeltakelseIKvalifiseringsprogramTest : BehaviorSpec({
    val sut = Control36StatusForDeltakelseIKvalifiseringsprogram()

    Given("valid context") {
        forAll(
            *(1..6).map {
                row(
                    "valid status, $it",
                    kostraRecordInTest(it.toString())
                )
            }.toTypedArray()
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect null") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "invalid status",
                kostraRecordInTest("7")
            )
        ) { description, kostraRecord ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecord, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith  "Korrigér status. Fant '7', forventet én av"
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(status: String) = KostraRecord(
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                STATUS_COL_NAME to status,
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
