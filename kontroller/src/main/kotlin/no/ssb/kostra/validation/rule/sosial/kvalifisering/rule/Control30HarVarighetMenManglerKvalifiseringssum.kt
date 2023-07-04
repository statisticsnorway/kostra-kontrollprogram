package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.findByColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control30HarVarighetMenManglerKvalifiseringssum : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (1..12).all { monthId ->
            fieldDefinitions.findByColumnName("${MONTH_PREFIX}$monthId")
                .codeIsMissing(context.getFieldAsString("$MONTH_PREFIX$monthId"))
        }.takeIf { !it && context.getFieldAsInteger(KVP_STONAD_COL_NAME) == null }?.let {
            createSingleReportEntryList(
                "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
                        "(${context.getFieldAsString(KVP_STONAD_COL_NAME)}) i løpet av " +
                        "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
            )
        }
}