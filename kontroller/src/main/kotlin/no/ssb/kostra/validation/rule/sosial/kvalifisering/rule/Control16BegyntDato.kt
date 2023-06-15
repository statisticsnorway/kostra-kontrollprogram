package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringUtils.isValidDate
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringUtils.parseYearDefaultZero

class Control16BegyntDato : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.BEGYNT_DATO_16.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val reportingYear = context.getFieldAsIntegerDefaultEquals0(VERSION_COL_NAME)
        val begyntDate = context.getFieldAsString(BEGYNT_DATO_COL_NAME)
        val invalidBegyntDate =
            !begyntDate.isValidDate(context.getFieldDefinitionByName(BEGYNT_DATO_COL_NAME)!!.datePattern)

        return if (invalidBegyntDate || (reportingYear - begyntDate.parseYearDefaultZero()) > 4) {
            createSingleReportEntryList(
                "Feltet for 'Hvilken dato begynte deltakeren i program? (iverksettelse)' med " +
                        "verdien ($begyntDate) enten mangler utfylling, har ugyldig dato eller dato som er eldre enn 4 år " +
                        "fra rapporteringsåret ($reportingYear). Feltet er obligatorisk å fylle ut."
            )
        } else null
    }
}