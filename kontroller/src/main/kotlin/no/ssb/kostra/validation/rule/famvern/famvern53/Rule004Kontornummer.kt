package no.ssb.kostra.validation.rule.famvern.famvern53

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FYLKE_NR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.KONTORNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule004Kontornummer(
    private val mappingList: List<FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping>,
) : AbstractNoArgsRule<List<KostraRecord>>(
        Familievern53RuleId.FAMILIEVERN53_RULE004.title,
        Severity.WARNING,
    ) {
    private val kontorList =
        mappingList.map { item -> item.kontor }.distinct().sorted()

    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot { mappingList.any { mapping -> it[KONTORNR_COL_NAME] == mapping.kontor } }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Kontornummeret som er oppgitt i recorden fins ikke i listen med gyldige kontornumre. " +
                            "Fant '${it[KONTORNR_COL_NAME]}', forventet én av : $kontorList. " +
                            "Feltet er obligatorisk og må fylles ut.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[FYLKE_NR_COL_NAME],
                    journalId = it[KONTORNR_COL_NAME],
                )
            }.ifEmpty { null }
}
