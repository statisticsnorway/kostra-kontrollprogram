package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.extension.municipalityIdFromRegion

class Control10Bu18Test : BehaviorSpec({
    val sut = Control10Bu18()

    Given("valid context") {
        forAll(
            row(
                "code = 1",
                kostraRecordInTest(1)
            ),
            row(
                "code = 2",
                kostraRecordInTest(1)
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
                "code = 3",
                kostraRecordInTest(3)
            ),
        ) { description, currentContext ->

            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                        it.messageText shouldBe "Korrigér forsørgerplikt. Fant '3', forventet én av [1=Ja, 2=Nei]'. " +
                                "Det er ikke krysset av for om deltakeren har barn under 18 år, som deltakeren " +
                                "(eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen " +
                                "ved siste kontakt. Feltet er obligatorisk å fylle ut."
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
                BU18_COL_NAME to code.toString()
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
