package no.ssb.kostra.area.barnevern.extension

import no.ssb.kostra.area.barnevern.SharedValidationConstants
import no.ssb.kostra.area.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.area.barnevern.SharedValidationConstants.KAPITTEL_4
import no.ssb.kostra.area.barnevern.SharedValidationConstants.PARAGRAF_12
import no.ssb.kostra.barn.xsd.KostraLovhjemmelType
import no.ssb.kostra.barn.xsd.KostraTiltakType

fun KostraTiltakType.erOmsorgsTiltak(): Boolean {
    return lovhjemmelOmsorgstiltak.any { this.lovhjemmel.matches(it) }
}

private val lovhjemmelOmsorgstiltak = setOf(
    KostraLovhjemmelType(
        lov = BVL1992,
        kapittel = KAPITTEL_4,
        paragraf = PARAGRAF_12
    )
) + SharedValidationConstants.leddOmsorg.map {
    KostraLovhjemmelType(
        lov = BVL1992,
        kapittel = KAPITTEL_4,
        paragraf = SharedValidationConstants.PARAGRAF_8,
        ledd = it
    )
}.toSet() + ('a'..'g').map { bokstav ->
    KostraLovhjemmelType(
        lov = SharedValidationConstants.BVL2021,
        kapittel = SharedValidationConstants.KAPITTEL_5,
        paragraf = SharedValidationConstants.PARAGRAF_1,
        bokstav = "$bokstav"
    )
}.toSet()

