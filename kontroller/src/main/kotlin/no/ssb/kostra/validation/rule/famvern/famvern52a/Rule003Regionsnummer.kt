package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.REGION_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule003Regionsnummer(
    private val mappingList: List<FamilievernConstants.KontorFylkeRegionMapping>
) : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE003.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { mappingList.any { mapping -> it[REGION_NR_A_COL_NAME] == mapping.region } }
        .map {
            val regionList = mappingList.map { item -> item.region }.distinct().sorted()
            createValidationReportEntry(
                messageText = "Regionsnummeret som er oppgitt i recorden fins ikke i listen med gyldige regionsnumre. "
                        + "Fant '${it[REGION_NR_A_COL_NAME]}', forventet én av : $regionList.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_A_COL_NAME],
                journalId = it[JOURNAL_NR_A_COL_NAME]
            )
        }.ifEmpty { null }
}