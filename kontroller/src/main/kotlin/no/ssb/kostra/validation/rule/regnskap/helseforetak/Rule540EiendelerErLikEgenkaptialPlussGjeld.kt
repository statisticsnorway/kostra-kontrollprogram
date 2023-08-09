package no.ssb.kostra.validation.rule.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule540EiendelerErLikEgenkaptialPlussGjeld : AbstractRule<List<KostraRecord>>(
    "Kontroll 540 : Eiendeler = egenkapital + gjeld",
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter { it.isBalanseRegnskap() }
        .takeIf { it.any() }
        ?.filter {
            it.fieldAsIntOrDefault(FIELD_ART) in 100..195
                    || it.fieldAsIntOrDefault(FIELD_ART) in 200..299
        }
        ?.partition {
            it.fieldAsIntOrDefault(FIELD_ART) in 100..195
        }
        ?.let { (eiendelerPosteringer, egenkapitalOgGjeldPosteringer) ->
            val egenkapitalOgGjeldPartionedPosteringer =
                egenkapitalOgGjeldPosteringer.partition { it.fieldAsIntOrDefault(FIELD_ART) in 200..209 }

            return@let Triple(
                eiendelerPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                egenkapitalOgGjeldPartionedPosteringer.first.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                egenkapitalOgGjeldPartionedPosteringer.second.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }?.takeUnless { (sumEiendeler, sumEgenkapital, sumGjeld) ->
            (sumEiendeler + sumEgenkapital + sumGjeld) in -50..50
        }?.let { (sumEiendeler, sumEgenkapital, sumGjeld) ->
            val sumBalanse = sumEiendeler + (sumEgenkapital + sumGjeld)
            createSingleReportEntryList(
                messageText = "Balansen ($sumBalanse) skal balansere ved at sum eiendeler ($sumEiendeler)  = sum " +
                        "egenkapital ($sumEgenkapital) + sum gjeld ($sumGjeld) . Differanser +/- 50' kroner godtas"
            )
        }
}