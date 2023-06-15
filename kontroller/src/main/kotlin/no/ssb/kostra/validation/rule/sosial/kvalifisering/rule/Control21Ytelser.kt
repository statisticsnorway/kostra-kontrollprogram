package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control21Ytelser : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.YTELSER_21.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KOMMNR_KVP_KOMM_COL_NAME)
        val kvpKomm = context.getFieldAsString(KVP_KOMM_COL_NAME)

        return createSingleReportEntryList(
            "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før registrert søknad " +
                    "ved NAV-kontoret en eller flere av følgende ytelser?' inneholder ugyldige verdier."
        )
    }
}