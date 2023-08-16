package no.ssb.kostra.validation.report

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.Code

class StatsReportEntryTest : BehaviorSpec({

    Given("StatsReportEntry instance with empty codeList and statsEntryList") {
        val sut = StatsReportEntry(
            content = "~content~",
            codeList = emptyList(),
            statsEntryList = emptyList()
        )

        When("toString is called") {
            val result = sut.toString()

            Then("result should be as expected") {
                result shouldBe
                        """
                        <table style='border: 1px solid black'>
                        <tr><td>&nbsp;</td><td>~content~</td></tr>
                        </table>
                        
                        """.trimIndent()
            }
        }
    }

    Given("StatsReportEntry instance with populated codeList and statsEntryList") {
        val sut = StatsReportEntry(
            content = "~content~",
            codeList = listOf(Code("~id~", "~value~")),
            statsEntryList = listOf(StatsEntry("~id~", "~value~"))
        )

        When("toString is called") {
            val result = sut.toString()

            Then("result should be as expected") {
                result shouldBe
                        """
                        <table style='border: 1px solid black'>
                        <tr><td>&nbsp;</td><td>~content~</td></tr>
                        <tr><td>&nbsp;</td><td style='text-align:right;'>~value~</td></tr>
                        <tr><td>~value~</td><td style='text-align:right;'>~value~</td></tr>
                        </table>
                        
                        """.trimIndent()
            }
        }
    }

    Given("StatsReportEntry instance with no match for statsEntry") {
        val sut = StatsReportEntry(
            content = "~content~",
            codeList = listOf(Code("~code~", "~value~")),
            statsEntryList = listOf(StatsEntry("~id~", "~value~"))
        )

        When("toString is called") {
            val result = sut.toString()

            Then("result should be as expected") {
                result shouldBe
                        """
                        <table style='border: 1px solid black'>
                        <tr><td>&nbsp;</td><td>~content~</td></tr>
                        <tr><td>&nbsp;</td><td style='text-align:right;'>~value~</td></tr>
                        <tr><td>~value~</td><td style='text-align:right;'>..</td></tr>
                        </table>
                        
                        """.trimIndent()
            }
        }
    }
})
