package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule010Funksjon(
    val funksjonList: List<String>
) : AbstractRule<List<KostraRecord>>("Kontroll 010 : Funksjon", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) =
        if (funksjonList.isEmpty()) null
        else context
            .filter { kostraRecord -> funksjonList.none { it.trim() == kostraRecord[FIELD_FUNKSJON].trim() } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = """Fant ugyldig funksjon '${kostraRecord[FIELD_FUNKSJON]}'. 
                                Korrigér funksjon til en av '${funksjonList.joinToString(", ")}'""".trimMargin(),
                    lineNumbers = listOf(kostraRecord.lineNumber)
                )
            }.ifEmpty { null }
}