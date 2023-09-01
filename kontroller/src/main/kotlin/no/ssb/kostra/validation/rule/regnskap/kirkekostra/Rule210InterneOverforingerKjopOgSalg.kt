package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule210InterneOverforingerKjopOgSalg : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 210 : Interne overføringer, kjøp og salg",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBevilgningRegnskap() }
        .takeIf { it.any() }
        ?.filter { it[FIELD_ART] in listOf("380", "780") }
        ?.partition { it[FIELD_ART] == "380" }
        ?.let { (internKjopPosteringer, internSalgPosteringer) ->
            Pair(
                internKjopPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                internSalgPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }
        ?.takeUnless { (internKjop, internSalg) -> (internKjop + internSalg) in -30..30 }
        ?.let { (internKjop, internSalg) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen (${internKjop.plus(internSalg)}) mellom " +
                        "internkjøp ($internKjop) og internsalg ($internSalg) stemmer overens " +
                        "(margin på +/- 30')"
            )
        }
}