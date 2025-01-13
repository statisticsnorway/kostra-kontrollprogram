package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.REGION_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule005RegionsnummerKontornummer(
    private val mappingList: List<FamilievernConstants.KontorFylkeRegionMapping>,
) : AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE005.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot {
                mappingList.any { mapping ->
                    it[REGION_NR_A_COL_NAME] == mapping.region &&
                        it[KONTOR_NR_A_COL_NAME] == mapping.kontor
                }
            }.map {
                createValidationReportEntry(
                    messageText =
                        "Regionsnummer '${it[REGION_NR_A_COL_NAME]}' og " +
                            "kontornummer '${it[KONTOR_NR_A_COL_NAME]}' stemmer ikke overens.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }
}
