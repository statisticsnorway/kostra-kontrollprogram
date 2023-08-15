package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_ANDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_BARNO18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_BARNU18_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_EKSPART_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_FORELDRE_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_OVRFAM_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_PARTNER_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_PRIMK_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.SAMT_VENN_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule025Behandlingssamtaler : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE025.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filterNot {
        fields.any { field ->
            0 < it.fieldAsIntOrDefault(field)
        }
    }.map {
        createValidationReportEntry(
            messageText = "Det er ikke fylt ut ut hvor mange behandlingssamtaler de ulike deltakerne i saken har " +
                    "deltatt i gjennom året. Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }

    companion object {
        val fields = listOf(
            SAMT_PRIMK_A_COL_NAME,
            SAMT_PARTNER_A_COL_NAME,
            SAMT_EKSPART_A_COL_NAME,
            SAMT_BARNU18_A_COL_NAME,
            SAMT_BARNO18_A_COL_NAME,
            SAMT_FORELDRE_A_COL_NAME,
            SAMT_OVRFAM_A_COL_NAME,
            SAMT_VENN_A_COL_NAME,
            SAMT_ANDRE_A_COL_NAME,
        )
    }
}