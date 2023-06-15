package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class Control19KvalifiseringsprogramIAnnenKommuneTest : BehaviorSpec({
    val sut = Control19KvalifiseringsprogramIAnnenKommune()

    Given("valid context") {
        forAll(
            row(
                "code = 1",
                kostraRecordInTest(1)
            ),
            row(
                "code = 2",
                kostraRecordInTest(2)
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
                "invalid code",
                42,
            ),
        ) { description, code ->

            When(description) {
                val reportEntryList = sut.validate(
                    kostraRecordInTest(code), argumentsInTest
                )

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Feltet for 'Kommer deltakeren fra kvalifiseringsprogram i " +
                                "annen kommune?' er ikke fylt ut, eller feil kode er benyttet ($code). " +
                                "Feltet er obligatorisk å fylle ut."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(code: Int) = KostraRecord(
            1,
            mapOf(
                MUNICIPALITY_ID_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                KVP_KOMM_COL_NAME to code.toString()

            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
