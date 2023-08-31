package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.HENV_DATO_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

class Rule007Henvendelsesdato : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE007.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context.filterNot {
        it.fieldAsLocalDate(HENV_DATO_A_COL_NAME) != null
    }.map {
        createValidationReportEntry(
            messageText = "Dette er ikke oppgitt dato (${it[HENV_DATO_A_COL_NAME]}) for når " +
                    "primærklienten henvendte seg til familievernkontoret eller feltet har ugyldig format " +
                    "(DDMMÅÅÅÅ). Feltet er obligatorisk å fylle ut.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}