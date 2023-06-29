package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule

class Rule015Duplicates(
    private val fieldNameTitlePairList: Pair<List<String>, List<String>>
) : AbstractRule<List<KostraRecord>>("Kontroll 015 : Dubletter", Severity.WARNING) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .groupBy { kostraRecord ->
            fieldNameTitlePairList.first.joinToString(" * ") { fieldName ->
                kostraRecord.getFieldAsTrimmedString(fieldName)
            }
        }
        .filter { (_, group) -> group.size > 1 }
        .takeIf { it.any() }
        ?.flatMap { (key, group) ->
            createSingleReportEntryList(
                messageText = """Det er oppgitt flere beløp på samme kombinasjon av 
                    (${fieldNameTitlePairList.second.joinToString(" * ")} - $key).<br/>
                 Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB. 
                 Dersom dette er feil må recordene korrigeres før innsending til SSB. 
                """.trimIndent(),
                lineNumbers = group.map { kostraRecord -> kostraRecord.lineNumber }
            )
        }
}