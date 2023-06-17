package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class Kjonn08Test : BehaviorSpec({
    val sut = Kjonn08()

    Given("valid context") {
        forAll(
            row(
                "record with kjonn = MANN",
                kostraRecordInTest("1")
            ),
            row(
                "record with kjonn = MANN #2",
                kostraRecordInTest(" 1 ")
            ),
            row(
                "record with kjonn = KVINNE",
                kostraRecordInTest("2")
            ),
            row(
                "record with kjonn = KVINNE #2",
                kostraRecordInTest(" 2 ")
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
                "record with empty kjonn",
                ""
            ),
            row(
                "record with invalid kjonn",
                "42"
            )
        ) { description, gender ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecordInTest(gender), argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith  "Korrigér kjønn. Fant '$gender', forventet én av"
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(genderId: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KJONN_COL_NAME to genderId
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
