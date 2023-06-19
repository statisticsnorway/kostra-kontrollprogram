package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningDriftRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule100SummeringDriftUtgiftsposteringer : AbstractRecordRule(
    "Kontroll 100 : Summeringskontroller driftsregnskapet, utgiftsposteringer i driftsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isBevilgningDriftRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isUtgift() }
        ?.sumOf { it.getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { sumDriftsUtgifter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder utgiftsposteringene " +
                        "($sumDriftsUtgifter) i driftsregnskapet"
            )
        }
}