package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isKommuneRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isNotOsloBydel

class Rule175Funksjon290Drift : AbstractNoArgsRule<List<KostraRecord>>(
    "Kontroll 175 : Funksjon 290, driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            it.isNotOsloBydel()
                    && it.isKommuneRegnskap()
                    && it.isBevilgningDriftRegnskap()
                    && it[FIELD_FUNKSJON].trim() == "290"
        }
        .takeIf { it.any() }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { funksjon290Drift -> funksjon290Drift in -30..30 }
        ?.let { funksjon290Drift ->
            createSingleReportEntryList(
                messageText = "Korrigér i fila slik at differanse ($funksjon290Drift) på funksjon " +
                        "290 interkommunale samarbeid går i 0 i driftsregnskapet. (margin på +/- 30')"
            )
        }
}