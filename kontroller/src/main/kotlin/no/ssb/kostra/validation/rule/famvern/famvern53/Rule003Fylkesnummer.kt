package no.ssb.kostra.validation.rule.famvern.famvern53

import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FYLKE_NR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.KONTORNR_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule003Fylkesnummer(
    private val mappingList: List<FamilievernConstants.KontorFylkeRegionMapping>
) : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern53RuleId.FAMILIEVERN53_RULE003.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context.filterNot {
        mappingList.any { mapping -> it[FYLKE_NR_COL_NAME] == mapping.fylke }
    }.map {
        val fylkeList = mappingList.map { item -> item.fylke }.distinct().sorted()
        createValidationReportEntry(
            messageText = "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med gyldige " +
                    "fylkesnumre. Fant '${it[FYLKE_NR_COL_NAME]}', forventet én av : $fylkeList.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[FYLKE_NR_COL_NAME],
            journalId = it[KONTORNR_COL_NAME]
        )
    }.ifEmpty { null }
}