package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isFylkeRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule180Funksjon465Investering : AbstractRecordRule(
    "Kontroll 180 : Funksjon 465, investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            !it.isOsloBydel()
                    && it.isFylkeRegnskap()
                    && it.isBevilgningInvesteringRegnskap()
                    && it.getFieldAsString(FIELD_FUNKSJON) == "465 "
        }
        .takeIf { it.any() }
        ?.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }
        ?.takeUnless { funksjon465Investering -> funksjon465Investering in -30..30 }
        ?.let { funksjon465Investering ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differanse ($funksjon465Investering) på funksjon 465 " +
                        "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                        "(margin på +/- 30')"
            )
        }
}