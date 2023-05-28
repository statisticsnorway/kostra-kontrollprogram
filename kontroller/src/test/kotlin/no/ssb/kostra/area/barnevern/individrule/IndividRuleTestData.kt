package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.KOSTRA_IS_CLOSED_FALSE
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import java.time.LocalDate
import java.time.Year

object IndividRuleTestData {

    const val SOCIAL_SECURITY_ID_IN_TEST = "02011088123"
    const val DUF_NUMBER_IN_TEST = "201017238203"

    val dateInTest: LocalDate = LocalDate.now()
    const val caseKostraId = "C1"

    val argumentsInTest = Arguments(
        skjema = "15F",
        aargang = (Year.now().value - 1).toString(),
        region = "030100"
    )

    val kostraIndividInTest = KostraIndividType(
        id = caseKostraId,
        journalnummer = "~journalnummer~",
        startDato = dateInTest,
        sluttDato = null,
        avslutta3112 = KOSTRA_IS_CLOSED_FALSE,
        fodselsnummer = SOCIAL_SECURITY_ID_IN_TEST,
        duFnummer = null,
        saksbehandler = "~saksbehandler~",
        bydelsnummer = "11",
        bydelsnavn = "~bydelsnavn~",
        distriktsnummer = "12"
    )
}