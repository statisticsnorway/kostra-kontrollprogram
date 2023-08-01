package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule03BydelsnummerTest : BehaviorSpec({
    val sut = Rule03Bydelsnummer()

    Given("valid context") {
        forAll(
            row(
                "record with Oslo municipality and valid bydel",
                kostraRecordInTest(municipalityId = OSLO_MUNICIPALITY_ID, districtId = "01")
            ),
            row(
                "record not with Oslo municipality and blank bydel",
                kostraRecordInTest(municipalityId = "1234", districtId = "  ")
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
                "record with Oslo municipality and blank bydel",
                kostraRecordInTest(municipalityId = OSLO_MUNICIPALITY_ID, districtId = "  ")
            ),
            row(
                "record with Oslo municipality and invalid bydel",
                kostraRecordInTest(municipalityId = OSLO_MUNICIPALITY_ID, districtId = "42")
            ),
            row(
                "record not with Oslo municipality and with bydel",
                kostraRecordInTest(municipalityId = "1234", districtId = "42")
            )
        ) { description, currentContext ->
            When(description) {
                val reportEntryList = sut.validate(currentContext, argumentsInTest)

                Then("expect non-null result") {
                    reportEntryList.shouldNotBeNull()
                    reportEntryList.size shouldBe 1

                    assertSoftly(reportEntryList.first()) {
                        it.severity shouldBe Severity.ERROR
                    }
                }
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(municipalityId: String, districtId: String) =
            mapOf(
                KOMMUNE_NR_COL_NAME to municipalityId,
                BYDELSNR_COL_NAME to districtId
            ).toKostraRecord(1, fieldDefinitions)
    }
}
