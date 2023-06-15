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

class Control28MaanederMedKvalifiseringsstonad : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.MAANEDER_MED_KVALIFISERINGSSTONAD_28.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(KVP_STONAD_COL_NAME)
        val value = context.getFieldAsInteger(KVP_STONAD_COL_NAME)

        return createSingleReportEntryList(
            "Det er ikke krysset av for hvilke måneder deltakeren har fått utbetalt " +
                    "kvalifiseringsstønad ($value) i løpet av rapporteringsåret. Feltet er obligatorisk å fylle ut."
        )
    }
}