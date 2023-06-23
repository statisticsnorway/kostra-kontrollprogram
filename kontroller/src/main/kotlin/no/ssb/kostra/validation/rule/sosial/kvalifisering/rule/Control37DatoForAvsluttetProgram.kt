package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control37DatoForAvsluttetProgram : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.DATO_FOR_AVSLUTTET_PROGRAM_37.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val status = context.getFieldAsString(STATUS_COL_NAME)
        val endDate = context.getFieldAsLocalDate(AVSL_DATO_COL_NAME)

        return when {
            status in codesThatRequiresDate && endDate == null -> createSingleReportEntryList(
                "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', må fylles " +
                        "ut dersom det er krysset av for svaralternativ $codeListThatRequiredDate under feltet for 'Hva er status for " +
                        "deltakelsen i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
            )

            status !in codesThatRequiresDate && endDate != null -> createSingleReportEntryList(
                "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant " +
                        "(${context.getFieldAsString(AVSL_DATO_COL_NAME)}), skal være blankt dersom det er krysset " +
                        "av for svaralternativ $codeListThatDisallowsDate under feltet for 'Hva er status for deltakelsen i " +
                        "kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
            )

            else -> null
        }
    }

    companion object {
        internal val codesThatRequiresDate = setOf("3", "4", "5")

        internal val codeListThatRequiredDate = fieldDefinitions
            .findByColumnName(STATUS_COL_NAME).codeList
            .filter { it.code in codesThatRequiresDate }
            .map { it.toString() }

        internal val codeListThatDisallowsDate = fieldDefinitions
            .findByColumnName(STATUS_COL_NAME).codeList
            .filter { it.code !in codesThatRequiresDate }
            .map { it.toString() }
    }
}