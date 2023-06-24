package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap

class Rule113SummeringTilskudd : AbstractRecordRule(
    "Kontroll 113 : Summeringskontroller driftsregnskapet, tilskudd",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.getFieldAsString(FIELD_ART) == "830" }
        ?.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { tilskudd ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder tilskudd ($tilskudd) fra kommunen"
            )
        }
}