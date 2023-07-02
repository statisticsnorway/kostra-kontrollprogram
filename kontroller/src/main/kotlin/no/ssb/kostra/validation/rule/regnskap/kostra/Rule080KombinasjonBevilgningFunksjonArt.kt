package no.ssb.kostra.validation.rule.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBevilgningRegnskap

class Rule080KombinasjonBevilgningFunksjonArt : AbstractRule<List<KostraRecord>>(
    "Kontroll 080 : Ugyldig kombinasjon i bevilgningsregnskapet, funksjon og art",
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kostraRecord.isBevilgningRegnskap()
                && kostraRecord.getFieldAsString(FIELD_ART) == "800"
                && kostraRecord.getFieldAsTrimmedString(FIELD_FUNKSJON) != "840"
                && kostraRecord.getFieldAsIntegerOrDefault(FIELD_BELOP) != 0
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Art 800 er kun tillat brukt i kombinasjon med funksjon 840.",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}