package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_OSLO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Control20AFraKvalifiseringsprogramIAnnenBydelIOsloTest : BehaviorSpec({
    val sut = Control20AFraKvalifiseringsprogramIAnnenBydelIOslo()

    Given("valid context") {
        forAll(
            row(
                "valid kommunenummer, not Oslo",
                kostraRecordInTest("1106", null)
            ),
            row(
                "Oslo with kvpOslo",
                kostraRecordInTest("0301", "1")
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
                "Oslo without kvpOslo",
                "0301", "null"
            ),
            row(
                "Oslo with invalid kvpOslo",
                "0301", "42"
            )
        ) { description, kommunnummer, kvpOslo ->

            When(description) {
                val reportEntryList = sut.validate(
                    kostraRecordInTest(kommunnummer, kvpOslo), argumentsInTest
                )

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.WARNING
                        it.messageText shouldBe "Manglende/ugyldig utfylling for om deltakeren kommer fra " +
                                "kvalifiseringsprogram i annen bydel (). Feltet er obligatorisk å fylle ut for Oslo."
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            kommunenummer: String,
            kvpOslo: String?
        ): KostraRecord = KostraRecord(
            1,
            when (kvpOslo) {
                null -> mapOf(
                    MUNICIPALITY_ID_COL_NAME to kommunenummer
                )

                else -> mapOf(
                    MUNICIPALITY_ID_COL_NAME to kommunenummer,
                    KVP_OSLO_COL_NAME to kvpOslo
                )
            },
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
