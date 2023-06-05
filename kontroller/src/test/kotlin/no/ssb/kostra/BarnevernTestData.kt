package no.ssb.kostra

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraBarnevernType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.validation.rule.barnevern.RandomUtils
import java.time.LocalDate
import java.time.Year

object BarnevernTestData {

    val dateInTest: LocalDate = LocalDate.now()
    const val KOSTRA_IS_CLOSED_FALSE = "2"

    val kostraAvgiverTypeInTest = KostraAvgiverType(
        organisasjonsnummer = "123456789",
        versjon = Year.now().value - 1,
        kommunenummer = "1234",
        kommunenavn = "~kommunenavn~"
    )

    val kostraIndividInTest = KostraIndividType(
        id = "C1",
        journalnummer = "~journalnummer~",
        startDato = dateInTest,
        saksbehandler = "~saksbehandler~",
        avslutta3112 = KOSTRA_IS_CLOSED_FALSE,
        sluttDato = dateInTest,
        fodselsnummer = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(2),
            LocalDate.now().minusYears(1)
        ),
        duFnummer = null,
        bydelsnummer = "11",
        bydelsnavn = "~bydelsnavn~",
        distriktsnummer = "12"
    )

    val barnevernTypeInTest = KostraBarnevernType(
        avgiver = kostraAvgiverTypeInTest,
        individ = mutableListOf(kostraIndividInTest)
    )
}