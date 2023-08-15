package no.ssb.kostra.validation.rule.famvern.famvern53

import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.FYLKE_NR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames.KONTORNR_COL_NAME
import no.ssb.kostra.area.famvern.famvern53.Familievern53Constants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule010Tiltak(
    private val mappingList: List<Familievern53Constants.Rule010Mapping>
) : AbstractRule<List<KostraRecord>>(
    Familievern53RuleId.FAMILIEVERN53_RULE010_TILTAK.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.map {
        mappingList.mapNotNull { mapping ->
            if (it.fieldAsIntOrDefault(mapping.tiltakField) == 0) {
                createValidationReportEntry(
                    ruleName = "$ruleName ${mapping.kategori}, tiltak",
                    messageText = "Det er ikke fylt hvor mange tiltak (${it[mapping.tiltakField]}) " +
                            "kontoret har gjennomført når det gjelder '${mapping.kategori}, tiltak'. " +
                            "Sjekk om det er glemt å rapportere '${mapping.kategori}'.",
                    lineNumbers = listOf(it.lineNumber)
                ).copy(
                    caseworker = it[FYLKE_NR_COL_NAME],
                    journalId = it[KONTORNR_COL_NAME]
                )
            } else null
        }
    }.flatten().ifEmpty { null }
}