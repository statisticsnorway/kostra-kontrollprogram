package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRecordRule

class Rule015Duplicates(
    private val fieldNameTitlePairList: Pair<List<String>, List<String>>
) : AbstractRecordRule("Kontroll 015 : Dubletter", Severity.WARNING) {
    override fun validate(context: List<KostraRecord>): List<ValidationReportEntry>? = context
        .map { kostraRecord ->
            fieldNameTitlePairList.first.joinToString(" * ") { fieldName ->
                kostraRecord.getFieldAsTrimmedString(fieldName)
            }
        }
        .groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }
        .map { it.key }
        .sorted()
        .takeIf { it.any() }
        ?.let {
            createSingleReportEntryList(
                messageText = """Det er oppgitt flere beløp på samme kombinasjon av 
                    (${fieldNameTitlePairList.second.joinToString(" * ")}).<br/>
                 Hvis dette er riktig, kan du sende inn filen, og beløpene summeres hos SSB. 
                 Dersom dette er feil må recordene korrigeres før innsending til SSB.   
                """.trimIndent()
            )
        }
}