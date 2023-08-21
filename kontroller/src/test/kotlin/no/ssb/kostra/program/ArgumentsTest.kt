package no.ssb.kostra.program

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ArgumentsTest : BehaviorSpec({
    Given("valid context") {
        When("all parameters are set") {
            val sut = KotlinArguments(
                skjema = "S",
                aargang = "YYYY",
                kvartal = "K",
                region = "RRRR",
                navn = "NNNNN",
                orgnr = "987654321",
                foretaknr = "876543210",
                harVedlegg = true,
                isRunAsExternalProcess = false,
                inputFileContent = "record1",
                startTime = LocalDateTime.of(2022,1,1, 1,1,1),
            )

            val result = sut.toString()

            Then("result should formatted as expected") {
                result shouldBe """KotlinArguments(skjema=S, aargang=YYYY, kvartal=K, region=RRRR, navn=NNNNN, orgnr=987654321, foretaknr=876543210, harVedlegg=true, isRunAsExternalProcess=false, inputFileContent=record1, startTime=2022-01-01T01:01:01)"""
            }
        }

        When("required parameters are set") {
            val sut = KotlinArguments(
                skjema = "S",
                aargang = "YYYY",
                region = "RRRR",
                startTime = LocalDateTime.of(2022,1,1, 1,1,1),
            )

            val result = sut.toString()

            Then("result should formatted as expected") {
                result shouldBe """KotlinArguments(skjema=S, aargang=YYYY, kvartal= , region=RRRR, navn=Uoppgitt, orgnr=         , foretaknr=         , harVedlegg=true, isRunAsExternalProcess=false, inputFileContent= , startTime=2022-01-01T01:01:01)"""
            }
        }
    }

    Given("valid context and inputFileContent is set") {
        val sut = KotlinArguments(
            skjema = "S",
            aargang = "YYYY",
            region = "RRRR",
            inputFileContent = "record1\nrecord2",
            startTime = LocalDateTime.of(2022,1,1, 1,1,1),
        )

        When("getting inputFileContent as a list of String") {
            val result = sut.getInputContentAsStringList()

            Then("result should be expected recordStrings") {
                result shouldBe listOf("record1", "record2")
            }
        }

        When("getting inputFileContent as an inputStream") {
            val result = sut.getInputContentAsInputStream()

            Then("result should be expected recordStrings") {
                result.readBytes() shouldBe "record1\nrecord2".toByteArray()
            }
        }

    }

    Given("invalid context") {
        When("no parameters are set") {
            Then("result should throw IllegalArgumentsException") {
                shouldThrow<IllegalArgumentException> { KotlinArguments(skjema = "", aargang = "", region = "") }
            }
        }

        When("only parameter -s is set") {
            Then("result should throw IllegalArgumentsException") {
                shouldThrow<IllegalArgumentException> { KotlinArguments(skjema = "S", aargang = "", region = "") }
            }
        }

        When("parameters skjema and aargang are set") {
            Then("result should throw IllegalArgumentsException") {
                shouldThrow<IllegalArgumentException> { KotlinArguments(skjema = "S", aargang = "YYYY", region = "") }
            }
        }
    }
})