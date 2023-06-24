package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BU18_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control13AntBu18 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.ANT_BU_18_13.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsIntegerOrDefault(ANT_BU18_COL_NAME)
            .takeIf { it >= CHILD_COUNT_THRESHOLD }
            ?.let { numberOfChildren ->
                createSingleReportEntryList(
                    "Antall barn ($numberOfChildren) under 18 år i husholdningen " +
                            "er $CHILD_COUNT_THRESHOLD eller flere, er dette riktig?"
                )
            }

    companion object {
        const val CHILD_COUNT_THRESHOLD = 14
    }
}