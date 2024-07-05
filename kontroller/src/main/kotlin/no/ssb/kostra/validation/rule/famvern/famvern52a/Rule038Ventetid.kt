package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.HENV_DATO_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import java.time.LocalDate
import java.time.Period

class Rule038Ventetid : AbstractNoArgsRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE038.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            val henvendelsesDato = it.fieldAs<LocalDate?>(HENV_DATO_A_COL_NAME)
            val samtaleDato = it.fieldAs<LocalDate?>(FORSTE_SAMT_A_COL_NAME)
            it[HENV_DATO_A_COL_NAME].isNotBlank()
                    && it[FORSTE_SAMT_A_COL_NAME].isNotBlank()
                    && henvendelsesDato != null
                    && samtaleDato != null
                    && Period.between(henvendelsesDato, samtaleDato).years >= 1
                    && (
                    Period.between(henvendelsesDato, samtaleDato).months > 0
                            || Period.between(henvendelsesDato, samtaleDato).days > 0
                    )
        }
        .map {
            createValidationReportEntry(
                messageText = "Dato for primærklientens henvendelse '${it[HENV_DATO_A_COL_NAME]}' " +
                        "til familievernkontoret er mer enn 1 år før første behandlingssamtale " +
                        "'${it[FORSTE_SAMT_A_COL_NAME]}'.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[Familievern52aColumnNames.KONTOR_NR_A_COL_NAME],
                journalId = it[Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME]
            )
        }
        .ifEmpty { null }
}