package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule006Region : AbstractRule<List<KostraRecord>>("Kontroll 006 : Region", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter { kostraRecord -> kostraRecord[FIELD_REGION] != arguments.region }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText = "Fant ugyldig region '${kostraRecord[FIELD_REGION]}'. " +
                        "Korrigér region til '${arguments.region}'",
                lineNumbers = listOf(kostraRecord.lineNumber)
            )
        }.ifEmpty { null }
}