package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule070KombinasjonBevilgningFunksjonArt : AbstractRecordRule(
    "Kontroll 070 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .filter { kostraRecord ->
            kostraRecord.isBevilgningRegnskap()
                    && kostraRecord.getFieldAsString(FIELD_ART) == "530"
                    && kostraRecord.getFieldAsString(FIELD_FUNKSJON) != "880 "
                    && kostraRecord.getFieldAsIntegerDefaultEquals0(FIELD_BELOP) != 0
        }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Art 530 er kun tillat brukt i kombinasjon med funksjon 880",
                lineNumbers = listOf(kostraRecord.index)
            )
        }
        .ifEmpty { null }
}