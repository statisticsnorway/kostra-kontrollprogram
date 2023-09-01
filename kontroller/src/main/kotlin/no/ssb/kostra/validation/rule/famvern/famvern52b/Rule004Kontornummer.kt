package no.ssb.kostra.validation.rule.famvern.famvern52b

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames.KONTOR_NR_B_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule004Kontornummer(
    private val mappingList: List<FamilievernConstants.KontorFylkeRegionMapping>
) : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52bRuleId.FAMILIEVERN52B_RULE004.title,
    Severity.WARNING
) {
    private val kontorList = mappingList.map { item -> item.kontor }.distinct().sorted()

    override fun validate(context: List<KostraRecord>) = context
        .filterNot { mappingList.any { mapping -> it[KONTOR_NR_B_COL_NAME] == mapping.kontor } }
        .map {
            createValidationReportEntry(
                messageText = "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. " +
                        "Fant '${it[KONTOR_NR_B_COL_NAME]}', forventet én av : ${kontorList}.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_B_COL_NAME],
                journalId = it[GRUPPE_NR_B_COL_NAME]
            )
        }.ifEmpty { null }
}