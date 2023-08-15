package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule003Skjema : AbstractRule<List<KostraRecord>>("Kontroll 003 : Skjema", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filter { kostraRecord ->
        kostraRecord[FIELD_SKJEMA] != arguments.skjema.substring(0, 2)
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Fant ugyldig skjema '${kostraRecord[FIELD_SKJEMA]}'. Korrigér skjema til '${arguments.skjema}'",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}