package no.ssb.kostra.web.config

import io.kotest.assertions.asClue
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class UiConfigTest(
    sut: UiConfig
) : BehaviorSpec({

    Given("UiConfig") {
        Then("should be as expected") {

            "aarganger should contain more than one element".asClue {
                sut.aarganger.size.shouldBeGreaterThan(1)
            }

            "kvartal should not be null/empty/blank and should match title".asClue {
                val itemsWithKvartal = sut.skjematyper.filter { it.tittel.contains("kvartal") }
                itemsWithKvartal.shouldNotBeEmpty()

                itemsWithKvartal.forEach {
                    it.kvartal.shouldNotBeBlank()
                    it.tittel.shouldContain("${it.kvartal}. kvartal")
                }
            }

            "tittel, labelOrgnr and labelOrgnrVirksomhetene should be mapped".asClue {
                assertSoftly(sut.skjematyper.first { it.id == "0X" }) {
                    tittel shouldBe "0X. Resultatregnskap for helseforetak"
                    labelOrgnr shouldBe "Organisasjonsnummer for foretaket"
                }
            }
        }
    }
})
