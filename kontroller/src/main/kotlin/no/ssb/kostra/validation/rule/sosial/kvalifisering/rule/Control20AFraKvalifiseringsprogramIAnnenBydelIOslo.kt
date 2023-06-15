package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_OSLO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.MUNICIPALITY_ID_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeIsMissing
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control20AFraKvalifiseringsprogramIAnnenBydelIOslo : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.FRA_KVALIFISERINGSPROGRAM_I_ANNEN_BYDEL_I_OSLO_20A.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val kommunenummer = context.getFieldAsString(MUNICIPALITY_ID_COL_NAME)

        return if (kommunenummer == "0301"
            && fieldDefinitions
                .findByColumnName(KVP_OSLO_COL_NAME)
                .codeIsMissing(context.getFieldAsString(KVP_OSLO_COL_NAME))
        ) {
            createSingleReportEntryList(
                "Manglende/ugyldig utfylling for om deltakeren kommer fra kvalifiseringsprogram i " +
                        "annen bydel (). Feltet er obligatorisk å fylle ut for Oslo."
            )
        } else null
    }
}