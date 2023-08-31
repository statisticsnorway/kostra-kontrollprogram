package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule004Kontornummer(
    private val mappingList: List<FamilievernConstants.KontorFylkeRegionMapping>
) : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE004.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context.filterNot {
        mappingList.any { mapping -> it[KONTOR_NR_A_COL_NAME] == mapping.kontor }
    }.map {
        val kontorList = mappingList.map { item -> item.kontor }.distinct().sorted()
        createValidationReportEntry(
            messageText = "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. " +
                    "Fant '${it[KONTOR_NR_A_COL_NAME]}', forventet én av : ${kontorList}.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}