package no.ssb.kostra.validation.rule.sosial.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils

class KostraRecordSosialExtensionsTest : BehaviorSpec({
    Given("KostraRecordSosialExtensions#hasVarighet") {
        forAll(
            row("Has varighet", true, true),
            row("Has not varighet", false, false),
        ) { description, setStmndRangeValues, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(setStmndRangeValues = setStmndRangeValues)

                Then("result should be as expected") {
                    sut.hasVarighet() shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraRecordSosialExtensions#hasNotVarighet") {
        forAll(
            row("Has varighet", true, false),
            row("Has not varighet", false, true),
        ) { description, setStmndRangeValues, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(setStmndRangeValues = setStmndRangeValues)

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

    Given("KostraRecordSosialExtensions#varighetAsStatsEntries") {
        forAll(
            row("Has varighet, 1 month", 1..1, true, "1 måned"),
            row("Has varighet, 2 months", 1..2, true, "2 - 3 måneder"),
            row("Has varighet, 4 months", 1..4, true, "4 - 6 måneder"),
            row("Has varighet, 7 months", 1..7, true, "7 - 9 måneder"),
            row("Has varighet, 10 months", 1..10, true, "10 - 11 måneder"),
            row("Has varighet, 12 months", 1..12, true, "12 måneder"),
            row("Has no varighet", 1..12, false, "Uoppgitt"),

            ) { description, stmndRange, setStmndRangeValues, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(
                    stmndRange = stmndRange,
                    setStmndRangeValues = setStmndRangeValues
                ).asList()
                val result = sut.varighetAsStatsEntries()

                Then("result should be as expected") {
                    result.shouldNotBeNull()
                    result.first().id shouldBe expectedResult
                }
            }
        }
    }
}) {
    companion object {
        fun kostraRecordInTest(
            fnr: String = RandomUtils.generateRandomSsn(30, RuleTestData.argumentsInTest.aargang.toInt()),
            stmndRange: IntRange = 1..1,
            setStmndRangeValues: Boolean = false
        ) = KvalifiseringTestUtils.kvalifiseringKostraRecordInTest(
            mapOf(
                SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to fnr
            ).plus(
                stmndRange.map {
                    with(fieldDefinitions.byColumnName("STMND_$it")) {
                        "STMND_$it" to if (setStmndRangeValues) codeList.first().code else " ".repeat(length)
                    }
                }
            )
        )
    }
}