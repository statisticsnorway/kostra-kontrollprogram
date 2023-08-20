package no.ssb.kostra.barnevern

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barnevern.KostraValidationUtils.validate
import no.ssb.kostra.barnevern.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barnevern.xsd.KostraAvgiverType
import no.ssb.kostra.barnevern.xsd.KostraBarnevernType
import no.ssb.kostra.barnevern.xsd.KostraIndividType
import java.time.LocalDate
import java.time.Year

class KostraValidationUtilsTest : BehaviorSpec({

    Given("valid XML") {
        val xmlToValidate = marshallInstance(barnevernTypeInTest)

        When("validate") {
            val isValid = validate(xmlToValidate)

            Then("isValid should be true") {
                isValid shouldBe true
            }
        }
    }
}) {
    companion object {
        private val dateInTest: LocalDate = LocalDate.now()
        private const val KOSTRA_IS_CLOSED_FALSE = "2"

        val barnevernTypeInTest = KostraBarnevernType(
            avgiver = KostraAvgiverType(
                organisasjonsnummer = "123456789",
                versjon = Year.now().value - 1,
                kommunenummer = "1234",
                kommunenavn = "~kommunenavn~"
            ),
            individ = mutableListOf(
                KostraIndividType(
                    id = "C1",
                    journalnummer = "~journalnummer~",
                    startDato = dateInTest,
                    saksbehandler = "~saksbehandler~",
                    avslutta3112 = KOSTRA_IS_CLOSED_FALSE,
                    sluttDato = dateInTest,
                    fodselsnummer = null,
                    duFnummer = null,
                    bydelsnummer = "11",
                    bydelsnavn = "~bydelsnavn~",
                    distriktsnummer = "12"
                )
            )
        )
    }
}