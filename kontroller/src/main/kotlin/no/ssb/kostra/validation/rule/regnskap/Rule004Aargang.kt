package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_AARGANG
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule004Aargang : AbstractRule<List<KostraRecord>>("Kontroll 004 : Årgang", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filter { kostraRecord ->
        kostraRecord.fieldAsString(FIELD_AARGANG) != arguments.aargang
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Fant ugyldig aargang '${kostraRecord.fieldAsString(FIELD_AARGANG)}'. " +
                    "Korrigér aargang til '${arguments.aargang}'",
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}