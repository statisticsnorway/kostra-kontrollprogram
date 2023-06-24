package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule210InterneOverforingerKjopOgSalg : AbstractRecordRule(
    "Kontroll 210 : Interne overføringer, kjøp og salg",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            it.isBevilgningRegnskap()
        }
        .takeIf {
            it.any()
        }
        ?.filter {
            it.getFieldAsString(FIELD_ART) in listOf("380", "780")
        }
        ?.partition {
            it.getFieldAsString(FIELD_ART) == "380"
        }
        ?.let { (internKjopPosteringer, internSalgPosteringer) ->
            internKjopPosteringer.sumOf { it.getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP) } to
                    internSalgPosteringer.sumOf { it.getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP) }
        }
        ?.takeUnless { (internKjop, internSalg) ->
            (internKjop + internSalg) in -30..30
        }
        ?.let { (internKjop, internSalg) ->
            val internDifferanse = internKjop + internSalg
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen ($internDifferanse) mellom " +
                        "internkjøp ($internKjop) og internsalg ($internSalg) stemmer overens " +
                        "(margin på +/- 30')"
            )
        }
}