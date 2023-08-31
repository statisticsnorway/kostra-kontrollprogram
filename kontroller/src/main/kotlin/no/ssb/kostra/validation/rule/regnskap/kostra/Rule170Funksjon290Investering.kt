package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isKommuneRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel

class Rule170Funksjon290Investering : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 170 : Funksjon 290, investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            !it.isOsloBydel()
                    && it.isKommuneRegnskap()
                    && it.isBevilgningInvesteringRegnskap()
                    && it[FIELD_FUNKSJON].trim() == "290"
        }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { funksjon290Investering -> funksjon290Investering in -30..30 }
        ?.let { funksjon290Investering ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differanse ($funksjon290Investering) på funksjon " +
                        "290 interkommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')"
            )
        }
}