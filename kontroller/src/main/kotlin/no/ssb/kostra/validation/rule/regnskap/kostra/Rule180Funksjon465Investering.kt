package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isFylkeRegnskap

class Rule180Funksjon465Investering : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 180 : Funksjon 465, investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            it.isFylkeRegnskap()
                    && it.isBevilgningInvesteringRegnskap()
                    && it[FIELD_FUNKSJON].trim() == "465"
        }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { funksjon465Investering -> funksjon465Investering in -30..30 }
        ?.let { funksjon465Investering ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differanse ($funksjon465Investering) på funksjon 465 " +
                        "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i investeringsregnskapet. " +
                        "(margin på +/- 30')"
            )
        }
}