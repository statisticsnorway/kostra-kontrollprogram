package no.ssb.kostra.program

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.Year

class KotlinArgumentsTest : BehaviorSpec({

    Given("KotlinArguments") {

        listOf(
            Case("empty skjema", ""),
            Case("blank skjema", "  "),
        ).forEach { (description, skjema) ->
            When("skjema is $description") {
                val thrown = shouldThrow<IllegalArgumentException> {
                    KotlinArguments(
                        skjema = skjema,
                        aargang = Year.now().value.toString(),
                        region = "030100"
                    )
                }

                Then("requirement is thrown") {
                    thrown.message shouldBe "parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A"
                }
            }
        }

        listOf(
            Case("empty aargang", ""),
            Case("blank aargang", "  "),
        ).forEach { (description, aargang) ->
            When("aargang is $description") {
                val thrown = shouldThrow<IllegalArgumentException> {
                    KotlinArguments(
                        skjema = "15",
                        aargang = aargang,
                        region = "030100"
                    )
                }

                Then("requirement is thrown") {
                    thrown.message shouldBe "parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2023"
                }
            }
        }

        listOf(
            Case("empty region", ""),
            Case("blank region", "  "),
        ).forEach { (description, region) ->
            When("region is $description") {
                val thrown = shouldThrow<IllegalArgumentException> {
                    KotlinArguments(
                        skjema = "15",
                        aargang = Year.now().value.toString(),
                        region = region
                    )
                }

                Then("requirement is thrown") {
                    thrown.message shouldBe "parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100"
                }
            }
        }
    }

    Given("KotlinArguments#getInputContentAsStringList()") {

        listOf(
            CaseWithExpected("empty content", "", listOf()),
            CaseWithExpected("content with new line", "a\nb", listOf("a", "b")),
            CaseWithExpected("content with carriage return and new line", "a\r\nb", listOf("a", "b")),
            CaseWithExpected("content with carriage return and new line plus blank line at end", "a\r\nb\n", listOf("a", "b")),
        ).forEach { (description, content, expectedList) ->
            When(description) {
                val kotlinArguments = KotlinArguments(
                    skjema = "15",
                    aargang = Year.now().value.toString(),
                    region = "030100",
                    inputFileContent = content
                )

                val contentAsStringList = kotlinArguments.getInputContentAsStringList()

                Then("getInputContentAsStringList() should produce the expectedList") {
                    contentAsStringList shouldBe expectedList
                }
            }
        }
    }
}){
    companion object{
        data class Case(
            val description: String,
            val argument: String,
        )

        data class CaseWithExpected(
            val description: String,
            val argument: String,
            val expected: List<String>,
        )

    }
}
