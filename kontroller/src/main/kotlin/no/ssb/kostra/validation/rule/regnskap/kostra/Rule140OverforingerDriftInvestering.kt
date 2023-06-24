package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule140OverforingerDriftInvestering : AbstractRecordRule(
    "Kontroll 140 : Overføring mellom drifts- og investeringsregnskap",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isBevilgningRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isBevilgningDriftRegnskap() }
        ?.let { (driftPosteringer, investeringPosteringer) ->
            driftPosteringer
                .filter { it.getFieldAsString(FIELD_ART) == "570" }
                .sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) } to
                    investeringPosteringer
                        .filter { it.getFieldAsString(FIELD_ART) == "970" }
                        .sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }
        }
        ?.takeUnless { (driftOverforinger, investeringOverforinger) ->
            driftOverforinger + investeringOverforinger in -30..30
        }
        ?.let { (driftOverforinger, investeringOverforinger) ->
            val overforingDifferanse = driftOverforinger + investeringOverforinger
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen ($overforingDifferanse) i " +
                        "overføringer mellom drifts- ($driftOverforinger) og investeringsregnskapet " +
                        "($investeringOverforinger) stemmer overens."
            )
        }
}