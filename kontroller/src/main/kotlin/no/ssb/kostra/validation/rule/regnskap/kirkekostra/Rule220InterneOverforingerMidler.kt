package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule220InterneOverforingerMidler : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 220 : Interne overføringer, midler",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBevilgningRegnskap() }
        .takeIf { it.any() }
        ?.filter { kostraRecord -> kostraRecord[FIELD_ART] in setOf("465", "865") }
        ?.partition { it[FIELD_ART] == "465" }
        ?.let { (overforingerPosteringer, innsamledeMidlerPosteringer) ->
            Pair(
                overforingerPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                innsamledeMidlerPosteringer.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }?.takeUnless { (overforinger, innsamledeMidler) ->
            (overforinger + innsamledeMidler) in -30..30
        }?.let { (overforinger, innsamledeMidler) ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen (${overforinger.plus(innsamledeMidler)}) " +
                        "mellom overføringer av midler ($overforinger) og innsamlede midler ($innsamledeMidler) " +
                        "stemmer overens (margin på +/- 30')"
            )
        }
}