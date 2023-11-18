package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.HENV_DATO_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import java.time.LocalDate

class Rule021DatoForsteBehandlingssamtaleEtterHenvendelse : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE021.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            it[HENV_DATO_A_COL_NAME].isNotBlank()
                    && it[FORSTE_SAMT_A_COL_NAME].isNotBlank()
        }
        .filterNot {
            it.fieldAs<LocalDate?>(HENV_DATO_A_COL_NAME) != null
                    && it.fieldAs<LocalDate?>(FORSTE_SAMT_A_COL_NAME) != null
                    && it.fieldAs<LocalDate>(HENV_DATO_A_COL_NAME) <= it.fieldAs<LocalDate>(FORSTE_SAMT_A_COL_NAME)
        }
        .map {
            createValidationReportEntry(
                messageText = "Dato for første behandlingssamtale '${it[FORSTE_SAMT_A_COL_NAME]}' er før dato for " +
                        "primærklientens henvendelse '${it[HENV_DATO_A_COL_NAME]}' til familievernkontoret.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KONTOR_NR_A_COL_NAME],
                journalId = it[JOURNAL_NR_A_COL_NAME]
            )
        }
        .ifEmpty { null }
}