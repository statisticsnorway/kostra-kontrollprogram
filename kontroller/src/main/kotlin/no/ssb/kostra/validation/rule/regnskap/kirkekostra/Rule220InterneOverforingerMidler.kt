package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule220InterneOverforingerMidler : AbstractRule<List<KostraRecord>>(
    "Kontroll 220 : Interne overføringer, midler",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            it.isBevilgningRegnskap()
        }
        .takeIf {
            it.any()
        }
        ?.filter { kostraRecord ->
            kostraRecord.getFieldAsString(FIELD_ART) in listOf("465", "865")
        }
        ?.partition {
            it.getFieldAsString(FIELD_ART) == "465"
        }
        ?.let { (overforingerPosteringer, innsamledeMidlerPosteringer) ->
            overforingerPosteringer.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) } to
                    innsamledeMidlerPosteringer.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }
        }
        ?.takeUnless { (overforinger, innsamledeMidler) ->
            (overforinger + innsamledeMidler) in -30..30
        }
        ?.let { (overforinger, innsamledeMidler) ->
            val midlerDifferanse = overforinger + innsamledeMidler
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen ($midlerDifferanse) mellom overføringer " +
                        "av midler ($overforinger) og innsamlede midler ($innsamledeMidler) " +
                        "stemmer overens (margin på +/- 30')"
            )
        }
}