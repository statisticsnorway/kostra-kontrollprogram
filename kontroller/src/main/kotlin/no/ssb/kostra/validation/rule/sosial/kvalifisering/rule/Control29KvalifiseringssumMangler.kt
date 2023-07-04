package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control29KvalifiseringssumMangler : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.KVALIFISERINGSSUM_MANGLER_29.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        if (context.fieldAs<Int?>(KVP_STONAD_COL_NAME) == null)
            createSingleReportEntryList(
                "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
                        "(${context[KVP_STONAD_COL_NAME]}) i løpet av " +
                        "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
            )
        else null
}