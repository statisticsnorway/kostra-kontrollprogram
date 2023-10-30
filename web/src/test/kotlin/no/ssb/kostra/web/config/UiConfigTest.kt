package no.ssb.kostra.web.config

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class UiConfigTest(
    sut: UiConfig
) : BehaviorSpec({

    Given("UiConfig") {
        Then("should be as expected") {
            sut.aarganger.size.shouldBeGreaterThan(1)

            assertSoftly(sut.skjematyper.first { it.id == "0BK3" }) {
                tittel shouldBe "0BK3. Balanseregnskap - 3. kvartal for kommuner"
                kvartal shouldBe "3"
            }

            assertSoftly(sut.skjematyper.first { it.id == "0X" }) {
                tittel shouldBe "0X. Resultatregnskap for helseforetak"
                labelOrgnr shouldBe "Organisasjonsnummer for foretaket"
                labelOrgnrVirksomhetene shouldBe "Organisasjonsnummer for virksomhetene"
            }
        }
    }
})
