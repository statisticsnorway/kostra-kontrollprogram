package no.ssb.kostra.program

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.time.Year

class KotlinArgumentsTest : BehaviorSpec({


    Given("KotlinArguments") {

        forAll(
            row("empty skjema", ""),
            row("blank skjema", "  "),
        ) { description, skjema ->
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

        forAll(
            row("empty aargang", ""),
            row("blank aargang", "  "),
        ) { description, aargang ->
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

        forAll(
            row("empty region", ""),
            row("blank region", "  "),
        ) { description, region ->
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
})
