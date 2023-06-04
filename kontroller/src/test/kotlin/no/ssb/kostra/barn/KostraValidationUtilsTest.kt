package no.ssb.kostra.barn

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.validate
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraBarnevernType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.validation.rule.barnevern.RandomUtils.generateRandomSSN
import java.time.LocalDate
import java.time.Year

class KostraValidationUtilsTest : BehaviorSpec({

    Given("validate") {

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

        private val kostraAvgiverTypeInTest = KostraAvgiverType(
            organisasjonsnummer = "123456789",
            versjon = Year.now().value - 1,
            kommunenummer = "1234",
            kommunenavn = "~kommunenavn~"
        )

        private val kostraIndividInTest = KostraIndividType(
            id = "C1",
            journalnummer = "~journalnummer~",
            startDato = dateInTest,
            saksbehandler = "~saksbehandler~",
            avslutta3112 = KOSTRA_IS_CLOSED_FALSE,
            sluttDato = dateInTest,
            fodselsnummer = generateRandomSSN(
                LocalDate.now().minusYears(1),
                LocalDate.of(Year.now().value - 1, 12, 31)
            ),
            duFnummer = null,
            bydelsnummer = "11",
            bydelsnavn = "~bydelsnavn~",
            distriktsnummer = "12"
        )

        private val barnevernTypeInTest = KostraBarnevernType(
            avgiver = kostraAvgiverTypeInTest,
            individ = mutableListOf(kostraIndividInTest)
        )
    }
}
