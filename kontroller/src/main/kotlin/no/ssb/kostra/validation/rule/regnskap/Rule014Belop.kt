package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule014Belop : AbstractRule<List<KostraRecord>>("Kontroll 014 : Beløp", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        with(kostraRecord[FIELD_BELOP]) {
            contains("\t") || !matches("^\\s*?-?\\d+$".toRegex())
        }
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Fant ugyldig beløp '${kostraRecord[FIELD_BELOP]}'. Korrigér beløp",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}