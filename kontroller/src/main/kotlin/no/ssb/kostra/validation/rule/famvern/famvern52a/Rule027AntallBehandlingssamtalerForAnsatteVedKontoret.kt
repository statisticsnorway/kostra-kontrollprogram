package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.ANTSAMT_ANDREANS_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.ANTSAMT_HOVEDT_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule027AntallBehandlingssamtalerForAnsatteVedKontoret :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE027.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot { fields.any { field -> 0 < it.fieldAsIntOrDefault(field) } }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Det er ikke oppgitt hvor mange behandlingssamtaler hovedterapeut eller andre " +
                            "ansatte har deltatt i gjennom året. Feltet er obligatorisk å fylle ut.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }

    companion object {
        private val fields =
            listOf(
                ANTSAMT_HOVEDT_A_COL_NAME,
                ANTSAMT_ANDREANS_A_COL_NAME,
            )
    }
}
