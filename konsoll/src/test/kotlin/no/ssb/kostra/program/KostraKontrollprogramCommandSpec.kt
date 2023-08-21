package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import picocli.CommandLine
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.time.Year

class KostraKontrollprogramCommandSpec : BehaviorSpec({

    Given("kostra-kontrollprogram-konsoll") {
        forAll(
            row(
                "invocation with no parameters",
                arrayOf(" "),
                " ",
                2,
                ""
            ),
            row(
                "invocation with non-existing schema",
                arrayOf("--schema", "SS", "--year", "${Year.now().toString().toInt() - 1}", "--region", "1234"),
                " ",
                2,
                "Ukjent skjema"
            ),
            row(
                "invocation with blank schema",
                arrayOf(
                    "--schema", "  ",
                    "--year", "${Year.now().toString().toInt() - 1}",
                    "--region", "1234"
                ),
                "",
                1,
                ""
            ),
            row(
                "invocation with valid schema, but hasAttachment = 0",
                arrayOf(
                    "--schema", "0G",
                    "--year", "${Year.now().toString().toInt() - 1}",
                    "--region", "1234",
                    "--attachment", "0"
                ),
                "",
                2,
                "Recordlengde har funnet 1 feil som hindrer innsending"
            ),
            row(
                "invocation with valid schema, hasAttachment = 1",
                arrayOf(
                    "--schema", "0G",
                    "--year", "${Year.now().toString().toInt() - 1}",
                    "--region", "1234",
                    "--attachment", "1"
                ),
                PLAIN_TEXT_0G,
                2,
                "Oppsummering pr. kontroll"
            )
        ) { description, args, input, expectedExitCode, expectedOutput ->
            When(description) {
                val originalSystemOut = System.out
                val originalSystemIn = System.`in`

                System.setIn(input.byteInputStream(StandardCharsets.ISO_8859_1))

                val outputStream = ByteArrayOutputStream()
                System.setOut(PrintStream(outputStream))

                val exitCode = CommandLine(KostraKontrollprogramCommand()).execute(*args)
                val output = outputStream.toString()

                System.setIn(originalSystemIn)
                System.setOut(originalSystemOut)

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
        private val PLAIN_TEXT_0G = """
            0G2020 300500976989732         510  123      263
            0G2020 300500976989732         510           263
        """.trimIndent()
    }
}