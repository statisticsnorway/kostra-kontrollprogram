package no.ssb.kostra.validation.rule.barnevern.extension

import no.ssb.kostra.barnevern.xsd.KostraLovhjemmelType

fun KostraLovhjemmelType.matches(requirement: KostraLovhjemmelType): Boolean = when {
    !this.isEqualTo(requirement) -> false

    requirement.ledd != null && this.ledd != requirement.ledd -> false

    requirement.bokstav != null && this.bokstav != requirement.bokstav -> false

    requirement.punktum != null && this.punktum != requirement.punktum -> false

    else -> true
}

internal fun KostraLovhjemmelType.isEqualTo(other: KostraLovhjemmelType): Boolean =
    this.lov == other.lov
            && this.kapittel == other.kapittel
            && this.paragraf == other.paragraf
