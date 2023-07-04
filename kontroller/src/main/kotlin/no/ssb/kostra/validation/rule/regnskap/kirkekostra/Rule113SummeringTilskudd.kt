package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule113SummeringTilskudd : AbstractRule<List<KostraRecord>>(
    "Kontroll 113 : Summeringskontroller driftsregnskapet, tilskudd",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.fieldAsString(FIELD_ART) == "830" }
        ?.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { tilskudd ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder tilskudd ($tilskudd) fra kommunen"
            )
        }
}