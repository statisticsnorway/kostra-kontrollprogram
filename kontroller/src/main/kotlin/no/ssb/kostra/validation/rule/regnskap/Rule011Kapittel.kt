package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule011Kapittel(
    val kapittelList: List<String>
) : AbstractNoArgsRule<List<KostraRecord>>("Kontroll 011 : Kapittel", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) =
        if (kapittelList.isEmpty()) null
        else context
            .filter { kostraRecord -> kapittelList.none { it == kostraRecord[FIELD_KAPITTEL] } }
            .map { kostraRecord ->
                createValidationReportEntry(
                    messageText = """Fant ugyldig kapittel '${kostraRecord[FIELD_KAPITTEL]}'. 
                                Korrigér kapittel til en av '${kapittelList.joinToString(", ")}'""".trimMargin(),
                    lineNumbers = listOf(kostraRecord.lineNumber)
                )
            }
            .ifEmpty { null }
}