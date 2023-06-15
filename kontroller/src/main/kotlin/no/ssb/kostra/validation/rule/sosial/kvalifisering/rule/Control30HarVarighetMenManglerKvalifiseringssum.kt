package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control30HarVarighetMenManglerKvalifiseringssum : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = KvalifiseringFieldDefinitions.fieldDefinitions.findByColumnName(KvalifiseringColumnNames.KVP_STONAD_COL_NAME)
        val value = context.getFieldAsInteger(KvalifiseringColumnNames.KVP_STONAD_COL_NAME)

        return createSingleReportEntryList(
            "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad ($value) i løpet av " +
                    "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
        )
    }
}