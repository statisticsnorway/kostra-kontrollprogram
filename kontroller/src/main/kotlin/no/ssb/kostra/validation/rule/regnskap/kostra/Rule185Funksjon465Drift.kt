package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isFylkeRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule185Funksjon465Drift : AbstractRule<List<KostraRecord>>(
    "Kontroll 185 : Funksjon 465, driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            !it.isOsloBydel()
                    && it.isFylkeRegnskap()
                    && it.isBevilgningDriftRegnskap()
                    && it[FIELD_FUNKSJON].trim() == "465"
        }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { funksjon465Drift -> funksjon465Drift in -30..30 }
        ?.let { funksjon465Drift ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differanse ($funksjon465Drift) på funksjon 465 " +
                        "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                        "(margin på +/- 30')"
            )
        }
}