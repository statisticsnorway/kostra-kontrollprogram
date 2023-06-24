package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import java.time.LocalDate

class Rule05FodselsnummerTest : BehaviorSpec({
    val sut = Rule05Fodselsnummer()

    Given("valid context") {
        forAll(
            row(
                "record with valid fodselsnummer",
                kostraRecordInTest(generateRandomSSN(LocalDate.now().minusYears(1), LocalDate.now()))
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
                "record with invalid fodselsnummer",
                kostraRecordInTest("42")
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.messageText shouldStartWith  "Det er ikke oppgitt fødselsnummer/d-nummer på deltakeren"
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(individId: String) = KostraRecord(
            1,
            mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                PERSON_FODSELSNR_COL_NAME to individId
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
