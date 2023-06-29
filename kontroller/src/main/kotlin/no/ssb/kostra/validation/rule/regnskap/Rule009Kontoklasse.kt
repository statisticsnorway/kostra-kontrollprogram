package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule009Kontoklasse(
    val kontoklasseList: List<String>
) : AbstractRule<List<KostraRecord>>("Kontroll 009 : Kontoklasse", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) = context.filter { kostraRecord ->
        kontoklasseList.none { it == kostraRecord.getFieldAsString(FIELD_KONTOKLASSE) }
    }.map { kostraRecord ->
        createValidationReportEntry(
            messageText = "Fant ugyldig kontoklasse '${kostraRecord.getFieldAsString(FIELD_KONTOKLASSE)}'. " +
                    "Korrigér kontoklasse til en av '${kontoklasseList.joinToString(", ")}'".trimMargin(),
            lineNumbers = listOf(kostraRecord.lineNumber)
        )
    }.ifEmpty { null }
}