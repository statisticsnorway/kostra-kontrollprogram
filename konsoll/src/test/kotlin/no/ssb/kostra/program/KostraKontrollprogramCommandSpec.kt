package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import picocli.CommandLine
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.time.Year

class KostraKontrollprogramCommandSpec : BehaviorSpec({

    Given("kostra-kontrollprogram-konsoll") {
        listOf(
            Case(
                "invocation with no parameters",
                arrayOf(" "),
                " ",
                2,
                ""
            ),
            Case(
                "invocation with non-existing schema",
                arrayOf(
                    "--schema", "SS",
                    "--year", "$previousYear",
                    "--region", "1234"
                ),
                " ",
                2,
                "Ukjent skjema"
            ),
            Case(
                "invocation with blank schema",
                arrayOf(
                    "--schema", "  ",
                    "--year", "$previousYear",
                    "--region", "1234"
                ),
                "",
                1,
                ""
            ),
            Case(
                "invocation with valid schema, but hasAttachment = 0",
                arrayOf(
                    "--schema", "0G",
                    "--year", "$previousYear",
                    "--region", "1234",
                    "--attachment", "0"
                ),
                "",
                2,
                "Recordlengde har funnet 1 kritiske feil som hindrer innsending"
            ),
            Case(
                "invocation with valid schema, hasAttachment = 1",
                arrayOf(
                    "--schema", "0G",
                    "--year", "$previousYear",
                    "--region", "1234",
                    "--attachment", "1"
                ),
                PLAIN_TEXT_0G,
                2,
                "Oppsummering pr. kontroll"
            ),
            Case(
                "Invocation with output format as JSON",
                arrayOf(
                    "--schema", "0G",
                    "--year", "$previousYear",
                    "--region", "1234",
                    "--output-format", "JSON",
                    "--output-charset", "UTF-8"
                ),
                PLAIN_TEXT_0G,
                2,
                "Fant ugyldig aargang '2020'"
            ),
            Case(
                "Invocation of output format with UTF-8 charset",
                arrayOf(
                    "--schema", "0G",
                    "--year", "$previousYear",
                    "--region", "1234",
                    "--output-format", "HTML",
                    "--output-charset", "UTF-8"
                ),
                PLAIN_TEXT_0G,
                2,
                String("Årgang".toByteArray(), StandardCharsets.UTF_8)
            ),

            ).forEach { (description, args, input, expectedExitCode, expectedOutput) ->
            When(description) {
                val originalSystemOut = System.out
                val originalSystemIn = System.`in`

                System.setIn(input.byteInputStream(StandardCharsets.ISO_8859_1))

                val outputStream = ByteArrayOutputStream()
                System.setOut(PrintStream(outputStream))

                val exitCode = CommandLine(KostraKontrollprogramCommand()).execute(*(args.plus("-v")))
                val output = outputStream.toString()

                System.setIn(originalSystemIn)
                System.setOut(originalSystemOut)

                println(output)

                Then("exit code '$exitCode' should be '$expectedExitCode'") {
                    exitCode shouldBe expectedExitCode
                }

                Then("output should contain '$expectedOutput'") {
                    output shouldContain expectedOutput
                }
            }
        }
    }
}) {
    companion object {
        private val previousYear = Year.now().value - 1

        private val PLAIN_TEXT_0G = """
            0G2020 300500976989732         510  123      263
            0G2020 300500976989732         510           263
        """.trimIndent()

        data class Case(
            val description: String,
            val args: Array<String>,
            val input: String,
            val expectedExitCode: Int,
            val expectedOutput: String
        )
    }
}
