package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control21Ytelser : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.YTELSER_21.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsString(YTELSE_SOSHJELP_COL_NAME)
            .takeIf { value ->
                value == "1" && fieldDefinitions
                    .findByColumnName(YTELSE_TYPE_SOSHJ_COL_NAME)
                    .codeIsMissing(context.getFieldAsString(YTELSE_TYPE_SOSHJ_COL_NAME))
            }?.let {
                createSingleReportEntryList(
                    "Feltet for 'Hadde deltakeren i løpet av de siste to månedene før registrert søknad " +
                            "ved NAV-kontoret en eller flere av følgende ytelser?' inneholder ugyldige verdier."
                )
            }
}