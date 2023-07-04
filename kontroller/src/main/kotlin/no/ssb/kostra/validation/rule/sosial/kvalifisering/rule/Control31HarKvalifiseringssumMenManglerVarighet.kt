package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX

class Control31HarKvalifiseringssumMenManglerVarighet : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.HAR_KVALIFISERINGSSUM_MEN_MANGLER_VARIGHET_31.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (1..12).all { monthId ->
            fieldDefinitions.byColumnName("${MONTH_PREFIX}$monthId")
                .codeIsMissing(context["$MONTH_PREFIX$monthId"])
        }.takeIf { it && context.fieldAs<Int?>(KVP_STONAD_COL_NAME) != null }?.let {
            createSingleReportEntryList(
                "Deltakeren har fått kvalifiseringsstønad (${context[KVP_STONAD_COL_NAME]}) " +
                        "i løpet av året, men mangler utfylling for hvilke måneder stønaden gjelder. Feltet er " +
                        "obligatorisk å fylle ut."
            )
        }
}