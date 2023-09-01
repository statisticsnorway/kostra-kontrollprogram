package no.ssb.kostra.validation.rule.sosial.sosialhjelp.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class KostraRecordSosialhjelpExtensionsTest : BehaviorSpec({

    Given("KostraRecordSosialhjelpExtensions#deltakereByAlderAsAsStatsEntries") {
        forAll(
            row("Has FNR where age is 0", generateRandomSsn(0), "Under 18"),
            row("Has FNR where age is 17", generateRandomSsn(17), "Under 18"),
            row("Has FNR where age is 18", generateRandomSsn(18), "18 - 24"),
            row("Has FNR where age is 24", generateRandomSsn(24), "18 - 24"),
            row("Has FNR where age is 25", generateRandomSsn(25), "25 - 44"),
            row("Has FNR where age is 44", generateRandomSsn(44), "25 - 44"),
            row("Has FNR where age is 45", generateRandomSsn(45), "45 - 66"),
            row("Has FNR where age is 66", generateRandomSsn(66), "45 - 66"),
            row("Has FNR where age is 67", generateRandomSsn(67), "67 og over"),
            row(
                "Has invalid FNR, invalid character string",
                "DDMMYYAAGKK",
                "Ugyldig fnr"
            ),
        ) { description, fnr, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(fnr = fnr)
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
            row("Has stonad, 1 kr", "1", "1 - 9999"),
            row("Has stonad, 9 999 kr", "9999", "1 - 9999"),
            row("Has stonad, 10 000 kr", "10000", "10000 - 49999"),
            row("Has stonad, 49 999 kr", "49999", "10000 - 49999"),
            row("Has stonad, 50 000 kr", "50000", "50000 - 99999"),
            row("Has stonad, 99 999 kr", "99999", "50000 - 99999"),
            row("Has stonad, 100 000 kr", "100000", "100000 - 149999"),
            row("Has stonad, 149 999 kr", "149999", "100000 - 149999"),
            row("Has stonad, 150 000 kr", "150000", "150000 og over"),
            row("Has stonad, 9 999 999 kr", "9999999", "150000 og over"),
            row("Missing stonad, 0 kr", "0", "Uoppgitt"),

            ) { description, bidrag, expectedResult ->
            When(description) {
                val sut = kostraRecordInTest(bidrag = bidrag)
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
            bidrag: String = "0",
            laan: String = "0"
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to fnr,
                SosialhjelpColumnNames.BIDRAG_COL_NAME to bidrag,
                SosialhjelpColumnNames.LAAN_COL_NAME to laan
            )
        )
    }
}