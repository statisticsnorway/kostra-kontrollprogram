package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule143Avskrivninger : AbstractRecordRule(
    "Kontroll 143 : Avskrivninger",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            it.isBevilgningDriftRegnskap()
        }
        .takeIf {
            it.any()
        }
        ?.filter {
            it.getFieldAsIntegerOrDefault(FIELD_FUNKSJON) in 41..45
        }
        ?.let { driftPosteringer ->
            driftPosteringer
                .filter { it.getFieldAsString(FIELD_ART) == "590" }
                .sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) } to
                    driftPosteringer
                        .filter { it.getFieldAsString(FIELD_ART) == "990" }
                        .sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }

        }
        ?.takeUnless { (avskrivninger, motpostAvskrivninger) ->
            (avskrivninger + motpostAvskrivninger) in -30..30
        }
        ?.let { (avskrivninger, motpostAvskrivninger) ->
            val differanse = avskrivninger + motpostAvskrivninger
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen ($differanse) " +
                        "mellom art 590 ($avskrivninger) stemmer overens med art " +
                        "990 ($motpostAvskrivninger) (margin på +/- 30')"
            )
        }
}