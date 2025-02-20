package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FORETAKSNR
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule008Foretaksnummer : AbstractRule<List<KostraRecord>>("Kontroll 008 : Foretaksnummer", Severity.ERROR) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter { kostraRecord -> kostraRecord[FIELD_FORETAKSNR] != arguments.orgnr && kostraRecord[FIELD_FORETAKSNR] != " ".repeat(9) }
        .map { kostraRecord ->
            createValidationReportEntry(
                messageText =
                    "Fant ugyldig foretaksnummer '${kostraRecord[FIELD_FORETAKSNR]}'. " +
                        "Korrigér foretaksnummer til '${arguments.orgnr}'",
                lineNumbers = listOf(kostraRecord.lineNumber),
            )
        }.ifEmpty { null }
}
