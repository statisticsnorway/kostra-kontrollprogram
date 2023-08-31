package no.ssb.kostra.validation.rule.famvern.famvern53

import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames
import no.ssb.kostra.area.famvern.famvern53.Familievern53Constants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule010Timer(
    private val mappingList: List<Familievern53Constants.Rule010Mapping>
) : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern53RuleId.FAMILIEVERN53_RULE010_TIMER.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context.map {
        mappingList.mapNotNull { mapping ->
            if (0 < it.fieldAsIntOrDefault(mapping.tiltakField) && it.fieldAsIntOrDefault(mapping.timerField) == 0) {
                createValidationReportEntry(
                    ruleName = "$ruleName ${mapping.kategori}, timer",
                    messageText = "Det er ikke fylt hvor mange timer '${it[mapping.timerField]}' " +
                            "kontoret har gjennomført når det gjelder '${mapping.kategori}, timer'. " +
                            "Sjekk om det er glemt å rapportere '${mapping.kategori}, timer'.",
                    lineNumbers = listOf(it.lineNumber)
                ).copy(
                    caseworker = it[Familievern53ColumnNames.FYLKE_NR_COL_NAME],
                    journalId = it[Familievern53ColumnNames.KONTORNR_COL_NAME]
                )
            } else null
        }
    }.flatten().ifEmpty { null }
}