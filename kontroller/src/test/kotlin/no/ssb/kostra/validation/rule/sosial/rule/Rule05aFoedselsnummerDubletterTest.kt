package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import java.time.LocalDate

class Rule05aFoedselsnummerDubletterTest : BehaviorSpec({
    val sut = Rule05aFoedselsnummerDubletter()

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
                "two records, different fødselsnummer",
                listOf(
                    kostraRecordInTest(),
                    kostraRecordInTest(
                        RandomUtils.generateRandomSSN(
                            LocalDate.now().minusYears(1),
                            LocalDate.now()
                        )
                    )
                )
            )
        ) { description, currentContext ->
            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect empty list") {
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
                    kostraRecordInTest(journalId = "~journalId2~")
                )
            )
        ) { description, context ->
            When(description) {
                val reportEntryList = sut.validate(context, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 2

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldStartWith "Fødselsnummeret i journalnummer"
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

        private fun kostraRecordInTest(
            foedselsnummer: String = fodselsnummerInTest,
            journalId: String = "~journalId~"
        ) = KostraRecord(
            1,
            mapOf(
                SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                PERSON_JOURNALNR_COL_NAME to journalId,
                PERSON_FODSELSNR_COL_NAME to foedselsnummer
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
