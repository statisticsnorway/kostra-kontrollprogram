package no.ssb.kostra.validation.rule.sosial.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils

class KostraRecordSosialExtensionsTest : BehaviorSpec({
    Given("KostraRecordSosialExtensions#hasVarighet") {
        forAll(
            row(
                "Has varighet",
                "01",
                true
            ),
            row(
                "Has not varighet",
                "  ",
                false
            ),
        ) { description, code, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(stmnd1 = code)

                Then("result should be as expected") {
                    sut.hasVarighet() shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraRecordSosialExtensions#hasNotVarighet") {
        forAll(
            row(
                "Has varighet",
                "01",
                false
            ),
            row(
                "Has not varighet",
                "  ",
                true
            ),
        ) { description, code, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(stmnd1 = code)

                Then("result should be as expected") {
                    sut.hasNotVarighet() shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraRecordSosialExtensions#ageInYears") {
        forAll(
            row(
                "Has FNR where age is 30",
                RandomUtils.generateRandomSsn(30, RuleTestData.argumentsInTest.aargang.toInt()),
                30
            ),
            row(
                "Has invalid FNR, returned age is set to -1",
                "DDMMYYAAGKK",
                -1
            ),
        ) { description, fnr, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(fnr = fnr)

                Then("result should be as expected") {
                    sut.ageInYears(RuleTestData.argumentsInTest) shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraRecordSosialExtensions#hasFnr") {
        forAll(
            row(
                "Has FNR where age is 30",
                RandomUtils.generateRandomSsn(30, RuleTestData.argumentsInTest.aargang.toInt()),
                true
            ),
            row(
                "Has invalid FNR, invalid character string",
                "DDMMYYAAGKK",
                false
            ),
            row(
                "Has invalid FNR, datepart = OK, controlpart = invalid",
                "01012312345",
                false
            ),
        ) { description, fnr, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(fnr = fnr)

                Then("result should be as expected") {
                    sut.hasFnr() shouldBe expectedResult
                }
            }
        }
    }
}) {
    companion object {
        fun kostraRecordInTest(
            fnr: String = RandomUtils.generateRandomSsn(30, RuleTestData.argumentsInTest.aargang.toInt()),
            stmnd1: String = SosialhjelpFieldDefinitions.fieldDefinitions
                .byColumnName(SosialhjelpColumnNames.STMND_1_COL_NAME)
                .codeList.first().code
        ) = KvalifiseringTestUtils.kvalifiseringKostraRecordInTest(
            mapOf(
                SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to fnr,
                SosialhjelpColumnNames.STMND_1_COL_NAME to stmnd1
            )
        )
    }
}