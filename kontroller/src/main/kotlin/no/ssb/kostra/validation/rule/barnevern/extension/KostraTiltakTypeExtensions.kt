package no.ssb.kostra.validation.rule.barnevern.extension

import no.ssb.kostra.barn.xsd.KostraLovhjemmelType
import no.ssb.kostra.barn.xsd.KostraTiltakType
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.ALLOCATION_CODE_1_PREFIX
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.ALLOCATION_CODE_2_PREFIX
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL1992
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.BVL2021
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KAPITTEL_4
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.KAPITTEL_5
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.MEASURE_CATEGORY_CODE_8_2
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.PARAGRAF_1
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.PARAGRAF_12
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.PARAGRAF_8
import no.ssb.kostra.validation.rule.barnevern.SharedValidationConstants.leddOmsorg
import java.time.LocalDate

const val MIN_OVERLAP_IN_DAYS = 90L

fun KostraTiltakType.erPlasseringsTiltak() =
    this.kategori.kode.startsWith(ALLOCATION_CODE_1_PREFIX)
            || this.kategori.kode.startsWith(ALLOCATION_CODE_2_PREFIX)
            || this.kategori.kode == MEASURE_CATEGORY_CODE_8_2

fun KostraTiltakType.erOmsorgsTiltak(): Boolean = lovhjemmelOmsorgstiltak.any { this.lovhjemmel.matches(it) }

fun KostraTiltakType.isOverlapWithAtLeastThreeMonthsOf(
    other: KostraTiltakType,
    fallbackEndDate: LocalDate
): Boolean {

    val firstRange = this.toStartDateEndDateRange(fallbackEndDate)
    val secondRange = other.toStartDateEndDateRange(fallbackEndDate)

    return firstRange.areOverlapping(secondRange)
            && firstRange.start.maxDate(secondRange.start)
        .plusDays(MIN_OVERLAP_IN_DAYS)
        .isBefore(firstRange.endInclusive.minDate(secondRange.endInclusive))
}

internal fun ClosedRange<LocalDate>.areOverlapping(other: ClosedRange<LocalDate>) =
    this.start.isBefore(other.endInclusive) && other.start.isBefore(this.endInclusive)

internal fun LocalDate.maxDate(other: LocalDate) = if (this.isAfter(other)) this else other

internal fun LocalDate.minDate(other: LocalDate) = if (this.isBefore(other)) this else other

internal fun KostraTiltakType.toStartDateEndDateRange(fallbackEndDate: LocalDate) =
    this.startDato.rangeTo(this.sluttDato ?: fallbackEndDate)

private val lovhjemmelOmsorgstiltak = setOf(
    KostraLovhjemmelType(
        lov = BVL1992,
        kapittel = KAPITTEL_4,
        paragraf = PARAGRAF_12
    )
) + leddOmsorg.map {
    KostraLovhjemmelType(
        lov = BVL1992,
        kapittel = KAPITTEL_4,
        paragraf = PARAGRAF_8,
        ledd = it
    )
}.toSet() + ('a'..'g').map { bokstav ->
    KostraLovhjemmelType(
        lov = BVL2021,
        kapittel = KAPITTEL_5,
        paragraf = PARAGRAF_1,
        bokstav = "$bokstav"
    )
}.toSet()

