package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AGE_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class AlderUnder18Aar06Test : BehaviorSpec({
    val sut = AlderUnder18Aar06()

    Given("valid context") {
        forAll(
            row(
                "record with valid age",
                kostraRecordInTest(18)
            )
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
                "record with invalid age",
                kostraRecordInTest(17)
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.messageText shouldBe "Deltakeren (17 år) er under 18 år."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(age: Int) = KostraRecord(
            1,
            mapOf(
                MUNICIPALITY_ID_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                AGE_COL_NAME to age.toString()
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
