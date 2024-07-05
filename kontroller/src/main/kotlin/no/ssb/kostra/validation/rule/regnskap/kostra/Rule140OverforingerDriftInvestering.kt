package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isNotOsloBydel

class Rule140OverforingerDriftInvestering : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 140 : Overføring mellom drifts- og investeringsregnskap",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isNotOsloBydel() && it.isBevilgningRegnskap() }
        .takeIf { it.any() }
        ?.partition { it.isBevilgningDriftRegnskap() }
        ?.let { (driftPosteringer, investeringPosteringer) ->
            Pair(
                driftPosteringer.filter { it[FIELD_ART] == "570" }
                    .sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                investeringPosteringer.filter { it[FIELD_ART] == "970" }
                    .sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            )
        }?.takeUnless { (driftOverforinger, investeringOverforinger) ->
            driftOverforinger + investeringOverforinger in -30..30
        }?.let { (driftOverforinger, investeringOverforinger) ->
            val overforingDifferanse = driftOverforinger + investeringOverforinger
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differansen ($overforingDifferanse) i " +
                        "overføringer mellom drifts- ($driftOverforinger) og investeringsregnskapet " +
                        "($investeringOverforinger) stemmer overens (margin på +/- 30')"
            )
        }
}