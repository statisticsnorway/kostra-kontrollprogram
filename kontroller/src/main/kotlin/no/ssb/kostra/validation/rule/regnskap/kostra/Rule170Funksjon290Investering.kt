package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isKommuneRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule170Funksjon290Investering : AbstractRecordRule(
    "Kontroll 170 : Funksjon 290, investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter {
            !it.isOsloBydel()
                    && it.isKommuneRegnskap()
                    && it.isBevilgningInvesteringRegnskap()
                    && it.getFieldAsString(FIELD_FUNKSJON) == "290 "
        }
        .takeIf { it.any() }
        ?.sumOf { it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) }
        ?.takeUnless { funksjon290Investering -> funksjon290Investering in -30..30 }
        ?.let { funksjon290Investering ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differanse ($funksjon290Investering) på funksjon " +
                        "290 interkommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')"
            )
        }
}