package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control31HarKvalifiseringssumMenManglerVarighet : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.HAR_KVALIFISERINGSSUM_MEN_MANGLER_VARIGHET_31.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KVP_STONAD_COL_NAME)
        val value = context.getFieldAsInteger(KVP_STONAD_COL_NAME)

        return createSingleReportEntryList(
            "Deltakeren har fått kvalifiseringsstønad ($value) i løpet av året, men mangler utfylling " +
                    "for hvilke måneder stønaden gjelder. Feltet er obligatorisk å fylle ut."
        )
    }
}