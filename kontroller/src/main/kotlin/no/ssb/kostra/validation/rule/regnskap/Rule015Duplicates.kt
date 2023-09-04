package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule015Duplicates(
    private val fieldNameTitlePairList: Pair<List<String>, List<String>>
) : AbstractNoArgsRule<List<KostraRecord>>("Kontroll 015 : Dubletter", Severity.WARNING) {
    override fun validate(context: List<KostraRecord>) = context
        .groupBy { kostraRecord ->
            fieldNameTitlePairList.first.joinToString(" * ") { fieldName -> kostraRecord[fieldName].trim() }
        }.filter { (_, group) -> group.size > 1 }
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