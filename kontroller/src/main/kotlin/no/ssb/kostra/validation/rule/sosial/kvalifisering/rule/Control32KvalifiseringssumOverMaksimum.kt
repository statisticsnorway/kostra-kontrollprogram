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

class Control32KvalifiseringssumOverMaksimum : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KVALIFISERINGSSUM_OVER_MAKSIMUM_32.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KVP_STONAD_COL_NAME)
        val value = context.getFieldAsInteger(KVP_STONAD_COL_NAME)

        return createSingleReportEntryList(
            "Kvalifiseringsstønaden ($value) som deltakeren har fått i løpet av rapporteringsåret " +
                    "overstiger Statistisk sentralbyrås kontrollgrense på kr. $stonadSumMax,-."
        )
    }

    companion object {
        const val stonadSumMax = 600_000
    }
}