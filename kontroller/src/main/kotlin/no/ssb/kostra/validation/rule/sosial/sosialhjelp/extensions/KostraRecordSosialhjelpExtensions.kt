package no.ssb.kostra.validation.rule.sosial.sosialhjelp.extensions

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.rule.sosial.extensions.ageInYears

fun Collection<KostraRecord>.deltakereByAlderAsStatsEntries(arguments: KotlinArguments) = this
    .map { it.ageInYears(arguments) }
    .groupBy {
        when {
            it in 0..17 -> "Under 18"
            it in 18..24 -> "18 - 24"
            it in 25..44 -> "25 - 44"
            it in 45..66 -> "45 - 66"
            it >= 67 -> "67 og over"
            else -> "Ugyldig fnr"
        }
    }.map { StatsEntry(it.key, it.value.size.toString()) }

fun Collection<KostraRecord>.stonadAsStatsEntries() = this
    .map { it.fieldAsIntOrDefault(BIDRAG_COL_NAME) + it.fieldAsIntOrDefault(LAAN_COL_NAME) }
    .groupBy {
        when {
            it in 1..9_999 -> "1 - 9999"
            it in 10_000..49_999 -> "10000 - 49999"
            it in 50_000..99_999 -> "50000 - 99999"
            it in 100_000..149_999 -> "100000 - 149999"
            it >= 150_000 -> "150000 og over"
            else -> "Uoppgitt"
        }
    }.map { StatsEntry(it.key, it.value.size.toString()) }
