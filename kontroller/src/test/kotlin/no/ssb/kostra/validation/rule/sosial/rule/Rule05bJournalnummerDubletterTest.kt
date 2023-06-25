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
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.LocalDate

class Rule05bJournalnummerDubletterTest : BehaviorSpec({
    val sut = Rule05bJournalnummerDubletter()

    Given("valid context") {
        forAll(
            row(
                "no records",
                emptyList()
            ),
            row(
                "single record",
                listOf(kostraRecordInTest())
            ),
            row(
                "two records, different journalId",
                listOf(
                    kostraRecordInTest(),
                    kostraRecordInTest("~journalId2~")
                )
            )
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, RuleTestData.argumentsInTest)

                Then("expect empty lsit") {
                    reportEntryList.shouldBeNull()
                }
            }
        }
    }

    Given("invalid context") {
        forAll(
            row(
                "two records with same fødselsnummer",
                listOf(
                    kostraRecordInTest(),
                    kostraRecordInTest()
                )
            )
        ) { description, context ->

            When(description) {
                val reportEntryList = sut.validate(context, RuleTestData.argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 2

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith "Journalnummer"
                    }
                }
            }
        }
    }
}) {
    companion object {
        private val fodselsnummerInTest = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(1),
            LocalDate.now()
        )

        private fun kostraRecordInTest(journalId: String = "~journalId~") = KostraRecord(
            1,
            mapOf(
                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to journalId,
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to fodselsnummerInTest
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
