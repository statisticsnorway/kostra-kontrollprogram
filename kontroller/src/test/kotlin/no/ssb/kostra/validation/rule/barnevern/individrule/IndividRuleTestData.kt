package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.*
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.rule.barnevern.RandomUtils
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KAPITTEL_4
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_FALSE
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.PARAGRAF_12
import java.time.LocalDate
import java.time.Year

object IndividRuleTestData {

    val dateInTest: LocalDate = LocalDate.now()
    private const val individKostraId = "C1"

    const val KOSTRA_TILTAK_ID = "~tiltak~"
    const val KOSTRA_MELDING_ID = "~melding~"
    const val KOSTRA_PLAN_ID = "~plan~"

    val argumentsInTest = Arguments(
        skjema = "15F",
        aargang = (Year.now().value - 1).toString(),
        region = "030100"
    )

    /** START sub-types */

    val kostraSaksinnholdTypeInTest = KostraSaksinnholdType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    val kostraMelderTypeInTest = KostraMelderType(
        kode = "~kode~",
        presisering = "~presisering~"
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

    val omsorgLovhjemmelTypeInTest = KostraLovhjemmelType(
        lov = BVL1992,
        kapittel = KAPITTEL_4,
        paragraf = PARAGRAF_12
    )

    val kostraOpphevelseTypeInTest = KostraOpphevelseType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    /** START Kostra types */

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

    val kostraMeldingTypeInTest = KostraMeldingType(
        id = KOSTRA_MELDING_ID,
        startDato = dateInTest,
        sluttDato = null,
        konklusjon = null,
        undersokelse = null,
        melder = mutableListOf(),
        saksinnhold = mutableListOf()
    )

    val kostraTiltakTypeInTest = KostraTiltakType(
        id = KOSTRA_TILTAK_ID,
        startDato = dateInTest,
        kategori = kostraKategoriTypeInTest,
        lovhjemmel = omsorgLovhjemmelTypeInTest,
        sluttDato = null,
        opphevelse = null,
        jmfrLovhjemmel = mutableListOf()
    )

    val kostraPlanTypeInTest = KostraPlanType(
        id = KOSTRA_PLAN_ID,
        startDato = dateInTest,
        plantype = "~plantype~",
        sluttDato = null,
        evaluertDato = null
    )
}