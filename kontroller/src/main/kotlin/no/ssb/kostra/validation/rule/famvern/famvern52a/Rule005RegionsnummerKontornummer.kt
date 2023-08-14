package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aMain
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames as Columns
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule005RegionsnummerKontornummer(
    private val mappingList: List<Familievern52aMain.KontorFylkeRegionMapping>
) : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE005.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        mappingList.any { mapping ->
            it[Columns.REGION_NR_A_COL_NAME] == mapping.region
                    && it[Columns.KONTOR_NR_A_COL_NAME] == mapping.kontor
        }
    }.map {
        createValidationReportEntry(
            messageText = "Regionsnummer '${it[Columns.REGION_NR_A_COL_NAME]}' og "
                    + "kontornummer '${it[Columns.KONTOR_NR_A_COL_NAME]}' stemmer ikke overens.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[Columns.KONTOR_NR_A_COL_NAME],
            journalId = it[Columns.JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}