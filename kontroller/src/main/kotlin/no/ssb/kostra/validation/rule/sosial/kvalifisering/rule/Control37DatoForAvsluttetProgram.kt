package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.END_DATE_COL_NAME
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
        val fieldDefinition = fieldDefinitions.findByColumnName(STATUS_COL_NAME)
        val endDate = context.getFieldAsTrimmedString(END_DATE_COL_NAME)

        val codes = fieldDefinition.codeList.filter { it.code in codeFilter }.map { it.toString() }

        return createSingleReportEntryList(
            "Feltet for 'Hvilken dato avsluttet deltakeren programmet?', fant ($endDate), må fylles " +
                    "ut dersom det er krysset av for svaralternativ $codes under feltet for 'Hva er status for " +
                    "deltakelsen i kvalifiseringsprogrammet per 31.12.${arguments.aargang}'?"
        )
    }
    companion object{
        private val codeFilter = setOf("3", "4", "5")
    }
}