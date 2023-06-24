package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control32KvalifiseringssumOverMaksimum.Companion.STONAD_SUM_MAX

class Control32KvalifiseringssumOverMaksimumTest : BehaviorSpec({
    val sut = Control32KvalifiseringssumOverMaksimum()

    Given("valid context") {
        forAll(
            row(
                "empty amount",
                kostraRecordInTest(" ")
            ),
            row(
                "valid amount",
                kostraRecordInTest("42")
            ),
            row(
                "max amount",
                kostraRecordInTest(STONAD_SUM_MAX.toString())
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
                "amount too high",
                kostraRecordInTest((STONAD_SUM_MAX + 1).toString())
            )
        ) { description, kostraRecord ->

            When(description) {
                val reportEntryList = sut.validate(kostraRecord, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.messageText shouldBe "Kvalifiseringsstønaden (600001) som deltakeren har fått i løpet av " +
                                "rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på NOK 600000,-."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(amount: String) = KostraRecord(
            valuesByName = mapOf(
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_STONAD_COL_NAME to amount,
            ),
            fieldDefinitionByName = fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
