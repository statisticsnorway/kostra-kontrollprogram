package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule143Avskrivninger : AbstractRule<List<KostraRecord>>(
    "Kontroll 143 : Avskrivninger",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.fieldAsIntOrDefault(FIELD_FUNKSJON) in 41..45 }
        ?.let { driftPosteringer ->
            Pair(
                driftPosteringer
                    .filter { it[FIELD_ART] == "590" }
                    .sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) },
                driftPosteringer
                    .filter { it[FIELD_ART] == "990" }
                    .sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
            ).takeUnless { (avskrivninger, motpostAvskrivninger) ->
                (avskrivninger + motpostAvskrivninger) in -30..30
            }?.let { (avskrivninger, motpostAvskrivninger) ->
                createSingleReportEntryList(
                    messageText = "Korrigér i fila slik at differansen (${avskrivninger.plus(motpostAvskrivninger)}) " +
                            "mellom art 590 ($avskrivninger) stemmer overens med art " +
                            "990 ($motpostAvskrivninger) (margin på +/- 30')"
                )
            }
        }
}