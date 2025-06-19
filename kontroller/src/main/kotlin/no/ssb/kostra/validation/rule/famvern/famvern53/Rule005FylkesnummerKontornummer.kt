package no.ssb.kostra.validation.rule.famvern.famvern53

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FYLKE_NR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.KONTORNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule005FylkesnummerKontornummer(
    private val mappingList: List<FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping>,
) : AbstractNoArgsRule<List<KostraRecord>>(
        Familievern53RuleId.FAMILIEVERN53_RULE005.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot {
                mappingList.any { mapping ->
                    it[FYLKE_NR_COL_NAME] == mapping.fylke &&
                        it[KONTORNR_COL_NAME] == mapping.kontor
                }
            }.map {
                createValidationReportEntry(
                    messageText =
                        "Fylkesnummer '${it[FYLKE_NR_COL_NAME]}' og " +
                            "kontornummer '${it[KONTORNR_COL_NAME]}' stemmer ikke overens.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[FYLKE_NR_COL_NAME],
                    journalId = it[KONTORNR_COL_NAME],
                )
            }.ifEmpty { null }
}
