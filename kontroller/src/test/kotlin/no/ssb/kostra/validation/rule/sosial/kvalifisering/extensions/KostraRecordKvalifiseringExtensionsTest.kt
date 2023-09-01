package no.ssb.kostra.validation.rule.sosial.kvalifisering.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils

class KostraRecordKvalifiseringExtensionsTest : BehaviorSpec({

    Given("KostraRecordSosialhjelpExtensions#deltakereByAlderAsAsStatsEntries") {
        forAll(
            row("Has FNR where age is 0", generateRandomSsn(0), "Under 20"),
            row("Has FNR where age is 19", generateRandomSsn(19), "Under 20"),
            row("Has FNR where age is 20", generateRandomSsn(20), "20 - 24"),
            row("Has FNR where age is 24", generateRandomSsn(24), "20 - 24"),
            row("Has FNR where age is 25", generateRandomSsn(25), "25 - 29"),
            row("Has FNR where age is 29", generateRandomSsn(29), "25 - 29"),
            row("Has FNR where age is 30", generateRandomSsn(30), "30 - 39"),
            row("Has FNR where age is 39", generateRandomSsn(39), "30 - 39"),
            row("Has FNR where age is 40", generateRandomSsn(40), "40 - 49"),
            row("Has FNR where age is 49", generateRandomSsn(49), "40 - 49"),
            row("Has FNR where age is 50", generateRandomSsn(50), "50 - 66"),
            row("Has FNR where age is 66", generateRandomSsn(66), "50 - 66"),
            row("Has FNR where age is 67", generateRandomSsn(67), "67 og over"),
            row(
                "Has invalid FNR, invalid character string",
                "DDMMYYAAGKK",
                "Ugyldig fnr"
            ),
        ) { description, fnr, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(fnr = fnr).asList()
                val result = sut.deltakereByAlderAsStatsEntries(
                    arguments = RuleTestData.argumentsInTest
                )

                Then("result should be as expected") {
                    result.first().id shouldBe expectedResult
                }
            }
        }
    }

    Given("KostraRecordSosialhjelpExtensions#stonadAsStatsEntries") {
        forAll(
            row("Has stonad, 1 kr", "1", "1 - 7999"),
            row("Has stonad, 7 999 kr", "7999", "1 - 7999"),
            row("Has stonad, 8 000 kr", "8000", "8000 - 49999"),
            row("Has stonad, 49 999 kr", "49999", "8000 - 49999"),
            row("Has stonad, 50 000 kr", "50000", "50000 - 99999"),
            row("Has stonad, 99 999 kr", "99999", "50000 - 99999"),
            row("Has stonad, 100 000 kr", "100000", "100000 - 149999"),
            row("Has stonad, 149 999 kr", "149999", "100000 - 149999"),
            row("Has stonad, 150 000 kr", "150000", "150000 og over"),
            row("Has stonad, 9 999 999 kr", "9999999", "150000 og over"),
            row("Missing stonad, 0 kr", "0", "Uoppgitt"),

            ) { description, stonad, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(stonad = stonad).asList()
                val result = sut.stonadAsStatsEntries()

                Then("result should be as expected") {
                    result.shouldNotBeNull()
                    result.first().id shouldBe expectedResult
                }
            }
        }
    }
}) {
    companion object {
        fun generateRandomSsn(age: Int = 30) = RandomUtils.generateRandomSsn(
            age,
            RuleTestData.argumentsInTest.aargang.toInt()
        )

        fun kostraRecordInTest(
            fnr: String = RandomUtils.generateRandomSsn(30, RuleTestData.argumentsInTest.aargang.toInt()),
            stonad: String = "0"
        ) = KvalifiseringTestUtils.kvalifiseringKostraRecordInTest(
            mapOf(
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to fnr,
                KvalifiseringColumnNames.KVP_STONAD_COL_NAME to stonad
            )
        )
    }
}