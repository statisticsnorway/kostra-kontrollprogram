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


class Control30HarVarighetMenManglerKvalifiseringssum : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            (1..12).all { monthId ->
                fieldDefinitions.byColumnName("${MONTH_PREFIX}$monthId")
                    .codeIsMissing(it["$MONTH_PREFIX$monthId"])
            }
        }.map {
                createValidationReportEntry(
                    "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
                            "(${it[KVP_STONAD_COL_NAME]}) i løpet av " +
                            "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
                )
        }.ifEmpty { null }

//     fun validate(context: KostraRecord, arguments: KotlinArguments) =
//        (1..12).all { monthId ->
//            fieldDefinitions.byColumnName("${MONTH_PREFIX}$monthId")
//                .codeIsMissing(context["$MONTH_PREFIX$monthId"])
//        }.takeIf { !it && context.fieldAs<Int?>(KVP_STONAD_COL_NAME) == null }?.let {
//            createSingleReportEntryList(
//                "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
//                        "(${context[KVP_STONAD_COL_NAME]}) i løpet av " +
//                        "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
//            )
//        }
//
    companion object {
        internal const val MONTH_PREFIX = "STMND_"
    }
}