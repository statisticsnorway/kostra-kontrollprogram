package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule003Skjema : AbstractRule<List<KostraRecord>>("Kontroll 003 : Skjema", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filter { kostraRecord ->
        kostraRecord.fieldAsString(FIELD_SKJEMA) != arguments.skjema
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Fant ugyldig skjema '${kostraRecord.fieldAsString(FIELD_SKJEMA)}'. Korrigér skjema til '${arguments.skjema}'",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}