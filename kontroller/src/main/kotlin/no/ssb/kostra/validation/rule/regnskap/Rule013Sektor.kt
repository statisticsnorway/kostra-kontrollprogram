package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule013Sektor(
    val sektorList: List<String>
) : AbstractRule<List<KostraRecord>>("Kontroll 013 : Sektor", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) =
        if (sektorList.isEmpty()) null
        else context
            .filter { kostraRecord -> sektorList.none { it == kostraRecord.getFieldAsString(FIELD_SEKTOR) } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = """Fant ugyldig sektor '${kostraRecord.getFieldAsString(FIELD_SEKTOR)}'. 
                                Korrigér sektor til en av '${sektorList.joinToString(", ")}'""".trimMargin(),
                    lineNumbers = listOf(kostraRecord.lineNumber)
                )
            }.ifEmpty { null }
}