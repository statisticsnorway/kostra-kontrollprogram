package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningInvesteringRegnskap
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isOsloBydel
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isRegional
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isUtgift

class Rule085SummeringInvesteringUtgiftsposteringer : AbstractRecordRule(
    "Kontroll 085 : Summeringskontroller investeringsregnskapet, utgiftsposteringer i investeringsregnskapet",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { !it.isOsloBydel() && it.isRegional() && it.isBevilgningInvesteringRegnskap() }
        .takeIf { it.any() }
        ?.filter { it.isUtgift() }
        ?.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }
        ?.takeUnless { 0 < it }
        ?.let { sumInvesteringsUtgifter ->
            createSingleReportEntryList(
                messageText = "Korrigér slik at fila inneholder utgiftsposteringene " +
                        "($sumInvesteringsUtgifter) i investeringsregnskapet"
            )
        }
}