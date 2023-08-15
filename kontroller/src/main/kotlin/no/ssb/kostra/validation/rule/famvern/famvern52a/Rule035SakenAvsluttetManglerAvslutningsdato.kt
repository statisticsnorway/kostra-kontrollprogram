package no.ssb.kostra.validation.rule.famvern.famvern52a

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.DATO_AVSL_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.KONTOR_NR_A_COL_NAME
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule

class Rule035SakenAvsluttetManglerAvslutningsdato : AbstractRule<List<KostraRecord>>(
    Familievern52aRuleId.FAMILIEVERN52A_RULE035.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context.filter {
        it[STATUS_ARETSSL_A_COL_NAME] in listOf("1", "2")
    }.filterNot {
        it.fieldAsLocalDate(DATO_AVSL_A_COL_NAME) != null
    }.map {
        createValidationReportEntry(
            messageText = "Det er krysset av for at saken er avsluttet i rapporteringsåret, " +
                    "men ikke fylt ut dato '${it[DATO_AVSL_A_COL_NAME]}' for avslutning av saken.",
            lineNumbers = listOf(it.lineNumber)
        ).copy(
            caseworker = it[KONTOR_NR_A_COL_NAME],
            journalId = it[JOURNAL_NR_A_COL_NAME]
        )
    }.ifEmpty { null }
}