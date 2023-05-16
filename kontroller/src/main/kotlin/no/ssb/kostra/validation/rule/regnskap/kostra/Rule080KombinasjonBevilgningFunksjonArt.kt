package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.isBevilgningRegnskap

class Rule080KombinasjonBevilgningFunksjonArt : AbstractRecordRule(
    "Kontroll 080 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningRegnskap()
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_ART)  == "800"
                    && kostraRecord.getFieldAsString(RegnskapConstants.FIELD_FUNKSJON) != "840 "
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Art 800 er kun tillat brukt i kombinasjon med funksjon 840.",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}