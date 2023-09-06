package no.ssb.kostra.validation.rule.sosial.kvalifisering.extensions

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.rule.sosial.extensions.ageInYears

fun Collection<KostraRecord>.deltakereByAlderAsStatsEntries(arguments: KotlinArguments) = this
    .map { it.ageInYears(arguments) }
    .groupBy {
        when {
            it in 0..19 -> "Under 20"
            it in 20..24 -> "20 - 24"
            it in 25..29 -> "25 - 29"
            it in 30..39 -> "30 - 39"
            it in 40..49 -> "40 - 49"
            it in 50..66 -> "50 - 66"
            it >= 67 -> "67 og over"
            else -> "Ugyldig fnr"
        }
    }.map { StatsEntry(it.key, it.value.size.toString()) }

fun Collection<KostraRecord>.stonadAsStatsEntries() = this
    .map { it.fieldAsIntOrDefault(KvalifiseringColumnNames.KVP_STONAD_COL_NAME) }
    .groupBy {
        when {
            it in 1..7_999 -> "1 - 7999"
            it in 8_000..49_999 -> "8000 - 49999"
            it in 50_000..99_999 -> "50000 - 99999"
            it in 100_000..149_999 -> "100000 - 149999"
            it >= 150_000 -> "150000 og over"
            else -> "Uoppgitt"
        }
    }.map { StatsEntry(it.key, it.value.size.toString()) }
