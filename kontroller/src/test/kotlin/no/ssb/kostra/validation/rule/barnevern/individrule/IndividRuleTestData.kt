package no.ssb.kostra.validation.rule.barnevern.individrule

import no.ssb.kostra.barnevern.xsd.*
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KAPITTEL_4
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.PARAGRAF_12
import java.time.LocalDate

object IndividRuleTestData {

    private const val KOSTRA_IS_CLOSED_FALSE = "2"
    val dateInTest: LocalDate = LocalDate.now()
    private const val INDIVID_KOSTRA_ID = "C1"

    private const val KOSTRA_TILTAK_ID = "~tiltak~"
    private const val KOSTRA_MELDING_ID = "~melding~"
    private const val KOSTRA_UNDERSOKELSE_ID = "~undersokelse~"
    private const val KOSTRA_PLAN_ID = "~plan~"
    private const val KOSTRA_FLYTTING_ID = "~flytting~"

    /** START subtypes */

    val saksinnholdTypeInTest = KostraSaksinnholdType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    val melderTypeInTest = KostraMelderType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    val kategoriTypeInTest = KostraKategoriType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    val lovhjemmelTypeInTest = KostraLovhjemmelType(
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

    val opphevelseTypeInTest = KostraOpphevelseType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    val vedtaksgrunnlagTypeInTest = KostraVedtaksgrunnlagType(
        kode = "~kode~",
        presisering = "~presisering~"
    )

    /** START Kostra types */

    val individInTest = KostraIndividType(
        id = INDIVID_KOSTRA_ID,
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

    val meldingTypeInTest = KostraMeldingType(
        id = KOSTRA_MELDING_ID,
        startDato = dateInTest,
        sluttDato = null,
        konklusjon = null,
        undersokelse = null,
        melder = mutableListOf(),
        saksinnhold = mutableListOf()
    )

    val undersokelseTypeInTest = KostraUndersokelseType(
        id = KOSTRA_UNDERSOKELSE_ID,
        startDato = dateInTest,
        sluttDato = null,
        konklusjon = null,
        presisering = null,
        vedtaksgrunnlag = mutableListOf()
    )

    val tiltakTypeInTest = KostraTiltakType(
        id = KOSTRA_TILTAK_ID,
        startDato = dateInTest,
        kategori = kategoriTypeInTest,
        lovhjemmel = omsorgLovhjemmelTypeInTest,
        sluttDato = null,
        opphevelse = null,
        jmfrLovhjemmel = mutableListOf()
    )

    val planTypeInTest = KostraPlanType(
        id = KOSTRA_PLAN_ID,
        startDato = dateInTest,
        plantype = "~plantype~",
        sluttDato = null,
        evaluertDato = null
    )

    val flyttingTypeInTest = KostraFlyttingType(
        id = KOSTRA_FLYTTING_ID,
        sluttDato = dateInTest,
        arsakFra = KostraArsakFraType(
            kode = "~kode~",
            presisering = "~presisering~"
        ),
        flyttingTil = KostraFlyttingTilType(
            kode = "~kode~",
            presisering = "~presisering~"
        )
    )
}