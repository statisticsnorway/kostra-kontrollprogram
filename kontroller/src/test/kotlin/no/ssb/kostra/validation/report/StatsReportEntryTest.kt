package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.Code

class StatsReportEntryTest : BehaviorSpec({

    Given("StatsReportEntry instance with empty codeList and statsEntryList") {
        val sut = StatsReportEntry(
            heading = StatsEntryHeading("~id~","~measure~"),
            entries = emptyList()
        )

        When("toString is called") {
            val result = sut.toString()

            Then("result should be as expected") {
                result shouldBe
                        """
                        <table style='border: 1px solid black'>
                        <tr><td>~id~</td><td>~measure~</td></tr>
                        </table>
                        
                        """.trimIndent()
            }
        }
    }

    Given("StatsReportEntry instance with populated codeList and statsEntryList") {
        val sut = StatsReportEntry(
            heading = StatsEntryHeading("~id~","~measure~"),
            entries = listOf(StatsEntry("~id-string~", "~value-string~"))
        )

        When("toString is called") {
            val result = sut.toString()

            Then("result should be as expected") {
                result shouldBe
                        """
                        <table style='border: 1px solid black'>
                        <tr><td>~id~</td><td>~measure~</td></tr>
                        <tr><td>~id-string~</td><td style='text-align:right;'>~value-string~</td></tr>
                        </table>
                        
                        """.trimIndent()
            }
        }
    }
})
