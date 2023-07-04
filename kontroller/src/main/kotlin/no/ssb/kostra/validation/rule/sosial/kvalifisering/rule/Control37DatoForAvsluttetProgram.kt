package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import java.time.LocalDate

class Control37DatoForAvsluttetProgram : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.DATO_FOR_AVSLUTTET_PROGRAM_37.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val status = context[STATUS_COL_NAME]
        val endDate = context.fieldAs<LocalDate?>(AVSL_DATO_COL_NAME)

        return when {
            status in codesThatRequiresDate && endDate == null -> createSingleReportEntryList(
                "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles " +
                        "ut dersom det er krysset av for svaralternativ $codeListThatRequiredDate under feltet for " +
                        "'Hva er status for deltakelsen i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
            )

            status !in codesThatRequiresDate && endDate != null -> createSingleReportEntryList(
                "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant " +
                        "(${context[AVSL_DATO_COL_NAME]}), skal være blankt dersom det er krysset av for " +
                        "svaralternativ $codeListThatDisallowsDate under feltet for 'Hva er status for deltakelsen " +
                        "i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
            )

            else -> null
        }
    }

    companion object {
        internal val codesThatRequiresDate = setOf("3", "4", "5")

        internal val codeListThatRequiredDate = fieldDefinitions
            .byColumnName(STATUS_COL_NAME).codeList
            .filter { it.code in codesThatRequiresDate }
            .map { it.toString() }

        internal val codeListThatDisallowsDate = fieldDefinitions
            .byColumnName(STATUS_COL_NAME).codeList
            .filter { it.code !in codesThatRequiresDate }
            .map { it.toString() }
    }
}