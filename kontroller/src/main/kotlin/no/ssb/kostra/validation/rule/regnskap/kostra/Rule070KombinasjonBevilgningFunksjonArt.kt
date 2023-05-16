package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningRegnskap

class Rule070KombinasjonBevilgningFunksjonArt : AbstractRecordRule(
    "Kontroll 070 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART) in listOf("530")
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON) != "880 "
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Art 530  er kun tillat brukt i kombinasjon med funksjon 880",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}