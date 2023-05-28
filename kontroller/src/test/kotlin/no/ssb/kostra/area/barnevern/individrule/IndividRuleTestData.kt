package no.ssb.kostra.area.barnevern.individrule

import no.ssb.kostra.area.barnevern.KOSTRA_IS_CLOSED_FALSE
import no.ssb.kostra.area.barnevern.RandomUtils
import no.ssb.kostra.barn.xsd.*
import no.ssb.kostra.program.Arguments
import java.time.LocalDate
import java.time.Year

object IndividRuleTestData {
    
    val dateInTest: LocalDate = LocalDate.now()
    private const val individKostraId = "C1"

    val argumentsInTest = Arguments(
        skjema = "15F",
        aargang = (Year.now().value - 1).toString(),
        region = "030100"
    )

    val kostraIndividInTest = KostraIndividType(
        id = individKostraId,
        journalnummer = "~journalnummer~",
        startDato = dateInTest,
        sluttDato = null,
        avslutta3112 = KOSTRA_IS_CLOSED_FALSE,
        fodselsnummer = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(1),
            LocalDate.of(Year.now().value - 1, 12, 31)
        ),
        duFnummer = null,
        saksbehandler = "~saksbehandler~",
        bydelsnummer = "11",
        bydelsnavn = "~bydelsnavn~",
        distriktsnummer = "12"
    )

    const val measureKostraId = "~tiltak~"
    const val messageKostraId = "~melding~"
    const val planKostraId = "~plan~"

    val kostraMeldingTypeInTes = KostraMeldingType(
        id = messageKostraId,
        startDato = dateInTest,
        sluttDato = null,
        konklusjon = null,
        undersokelse = null,
        melder = mutableListOf(),
        saksinnhold = mutableListOf()
    )

    val kostraKategoriTypeInTest = KostraKategoriType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    val kostraLovhjemmelTypeInTest = KostraLovhjemmelType(
        lov = "BVL",
        kapittel = "2",
        paragraf = "3",
        ledd = "4",
        bokstav = "a",
        punktum = "5"
    )

    val kostraTiltakTypeInTest = KostraTiltakType(
        id = measureKostraId,
        startDato = dateInTest,
        kategori = kostraKategoriTypeInTest,
        lovhjemmel = kostraLovhjemmelTypeInTest,
        sluttDato = null,
        opphevelse = null,
        jmfrLovhjemmel = mutableListOf()
    )

    val kostraPlanTypeInTest = KostraPlanType(
        id = planKostraId,
        startDato = dateInTest,
        plantype = "~plantype~",
        sluttDato = null,
        evaluertDato = null
    )
}