package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.regnskap.kostra.extensions.isBalanseRegnskap

class Rule011Kapittel(
    val kapittelList: List<String>
) : AbstractNoArgsRule<List<KostraRecord>>("Kontroll 011 : Kapittel", Severity.ERROR) {
    override fun validate(context: List<KostraRecord>) =
        if (kapittelList.isEmpty()) null
        else context
            .filter { it.isBalanseRegnskap() }
            .filter { kostraRecord -> kapittelList.none { it == kostraRecord[FIELD_KAPITTEL] } }
            .map { kostraRecord ->
                val kapittelListAsString = kapittelList.joinToString(", ")
                createValidationReportEntry(
                    messageText = "Fant ugyldig kapittel '${kostraRecord[FIELD_KAPITTEL]}'. " +
                                "Korrigér kapittel til en av '$kapittelListAsString'",
                    lineNumbers = listOf(kostraRecord.lineNumber),
                    severity = if (
                        kostraRecord[FIELD_KVARTAL].first()
                        in setOf('1', '2', '3')
                    )
                        Severity.WARNING
                    else
                        Severity.ERROR
                )
            }
            .ifEmpty { null }
}