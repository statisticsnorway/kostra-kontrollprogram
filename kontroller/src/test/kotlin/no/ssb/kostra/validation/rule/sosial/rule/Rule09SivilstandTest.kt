package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule09SivilstandTest : BehaviorSpec({
    val sut = Rule09Sivilstand()

    Given("valid context") {

        forAll(
            *(1..5).map {
                row(
                    "record with sivilstand = $it",
                    kostraRecordInTest("$it")
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
                "record with empty sivilstand",
                ""
            ),
            row(
                "record with invalid sivilstand",
                "42"
            )
        ) { description, maritalStatus ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecordInTest(maritalStatus), argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith "Korrigér sivilstand. Fant '$maritalStatus, forventet én av"
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(maritalStatus: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                EKTSTAT_COL_NAME to maritalStatus
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
