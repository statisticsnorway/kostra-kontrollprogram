package no.ssb.kostra.program

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import picocli.CommandLine
import java.io.ByteArrayInputStream
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
            )
        ) { description, args, input, expectedExitCode, expectedOutput ->
            When(description) {
                val originalSystemOut = System.out
                val originalSystemIn = System.`in`

                val bais = ByteArrayInputStream(input.toByteArray(StandardCharsets.ISO_8859_1))
                System.setIn(bais)

                val baos = ByteArrayOutputStream()
                System.setOut(PrintStream(baos))

                val exitCode = CommandLine(KostraKontrollprogramCommand()).execute(*args)
                val output = baos.toString()

                System.setIn(originalSystemIn)
                System.setOut(originalSystemOut)

                Then("exit code should be $exitCode") {
                    exitCode shouldBe expectedExitCode
                }

                Then("output should contain '$expectedOutput'") {
                    output shouldContain expectedOutput
                }
            }
        }
    }
})