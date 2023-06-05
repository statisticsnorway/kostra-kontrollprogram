package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barn.xsd.*
import no.ssb.kostra.validation.rule.barnevern.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KAPITTEL_4
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KOSTRA_IS_CLOSED_FALSE
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.PARAGRAF_12
import java.time.LocalDate

object IndividRuleTestData {

    val dateInTest: LocalDate = LocalDate.now()
    private const val individKostraId = "C1"

    const val KOSTRA_TILTAK_ID = "~tiltak~"
    const val KOSTRA_MELDING_ID = "~melding~"
    const val KOSTRA_UNDERSOKELSE_ID = "~undersokelse~"
    const val KOSTRA_PLAN_ID = "~plan~"

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

    val kostraVedtaksgrunnlagTypeInTest = KostraVedtaksgrunnlagType(
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
        fodselsnummer = generateRandomSSN(
            LocalDate.now().minusYears(1),
            LocalDate.now()
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

    val kostraUndersokelseTypeInTest = KostraUndersokelseType(
        id = KOSTRA_UNDERSOKELSE_ID,
        startDato = dateInTest,
        sluttDato =  null,
        konklusjon =  null,
        presisering = null,
        vedtaksgrunnlag = mutableListOf()
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