package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ORGNR
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule007Organisasjonsnummer : AbstractRule<List<KostraRecord>>("Kontroll 007 : Organisasjonsnummer", Severity.ERROR) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter { kostraRecord -> kostraRecord[FIELD_ORGNR] != arguments.orgnr }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText =
                    "Fant ugyldig orgnr '${kostraRecord[FIELD_ORGNR]}'. " +
                        "Korrigér orgnr til en av '${arguments.orgnr.split(",")}'",
                lineNumbers = listOf(kostraRecord.lineNumber),
            )
        }.ifEmpty { null }
}
