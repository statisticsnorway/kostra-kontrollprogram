package no.ssb.kostra.validation.rule.sosial.kvalifisering.extensions

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.rule.sosial.extensions.ageInYears

fun Collection<KostraRecord>.deltakereByAlderAsStatsEntries(arguments: KotlinArguments) = this
    .map { it.ageInYears(arguments) }
    .groupBy {
        when (it) {
            in 0..19 -> "Under 20"
            in 20..24 -> "20 - 24"
            in 25..29 -> "25 - 29"
            in 30..39 -> "30 - 39"
            in 40..49 -> "40 - 49"
            in 50..66 -> "50 - 66"
            in 67..999 -> "67 og over"
            else -> "Ugyldig fnr"
        }
    }.map { StatsEntry(it.key, it.value.size.toString()) }

fun Collection<KostraRecord>.stonadAsStatsEntries() = this
    .map { it.fieldAsIntOrDefault(KvalifiseringColumnNames.KVP_STONAD_COL_NAME) }
    .groupBy {
        when (it) {
            in 1..7_999 -> "1 - 7999"
            in 8_000..49_999 -> "8000 - 49999"
            in 50_000..99_999 -> "50000 - 99999"
            in 100_000..149_999 -> "100000 - 149999"
            in 150_000..9_999_999 -> "150000 og over"
            else -> "Uoppgitt"
        }
    }.map { StatsEntry(it.key, it.value.size.toString()) }
