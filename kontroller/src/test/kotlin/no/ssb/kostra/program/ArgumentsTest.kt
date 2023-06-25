package no.ssb.kostra.program

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ArgumentsTest : BehaviorSpec({
    Given("valid context") {
        When("all parameters are set") {
            val sut = KotlinArguments(
                skjema = "S",
                aargang = "YYYY",
                region = "RRRR",
                kvartal = "K",
                navn = "NNNNN",
                orgnr = "987654321",
                foretaknr = "876543210",
                harVedlegg = true,
                isRunAsExternalProcess = false,
                inputFileContent = "record1"
            )

            val result = sut.toString()

            Then("result should formatted as expected") {
                result shouldBe """KotlinArguments(skjema=S, aargang=YYYY, region=RRRR, kvartal=K, navn=NNNNN, orgnr=987654321, foretaknr=876543210, harVedlegg=true, isRunAsExternalProcess=false, inputFileContent=record1, inputFileStream=null)"""
            }
        }

        When("required parameters are set") {
            val sut = KotlinArguments(
                skjema = "S",
                aargang = "YYYY",
                region = "RRRR",
            )

            val result = sut.toString()

            Then("result should formatted as expected") {
                result shouldBe """KotlinArguments(skjema=S, aargang=YYYY, region=RRRR, kvartal= , navn=Uoppgitt, orgnr=         , foretaknr=         , harVedlegg=true, isRunAsExternalProcess=false, inputFileContent= , inputFileStream=null)"""
            }
        }
    }

    Given("valid context and inputFileContent is set") {
        val sut = KotlinArguments(
            skjema = "S",
            aargang = "YYYY",
            region = "RRRR",
            inputFileContent = "record1\nrecord2"
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