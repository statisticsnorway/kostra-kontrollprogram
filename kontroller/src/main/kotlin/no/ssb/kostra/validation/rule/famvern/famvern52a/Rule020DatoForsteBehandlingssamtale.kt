package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule020DatoForsteBehandlingssamtale :
    AbstractNoArgsRule<List<KostraRecord>>(
        Familievern52aRuleId.FAMILIEVERN52A_RULE020.title,
        Severity.WARNING,
    ) {
    override fun validate(context: List<KostraRecord>) =
        context
            .filterNot { it.fieldAsLocalDate(FORSTE_SAMT_A_COL_NAME) != null }
            .map {
                createValidationReportEntry(
                    messageText =
                        "Det er ikke oppgitt dato for første behandlingssamtale eller feltet har ugyldig format. " +
                            "Fant '${it[FORSTE_SAMT_A_COL_NAME]}'. Feltet er obligatorisk å fylle ut.",
                    lineNumbers = listOf(it.lineNumber),
                ).copy(
                    caseworker = it[KONTOR_NR_A_COL_NAME],
                    journalId = it[JOURNAL_NR_A_COL_NAME],
                )
            }.ifEmpty { null }
}
