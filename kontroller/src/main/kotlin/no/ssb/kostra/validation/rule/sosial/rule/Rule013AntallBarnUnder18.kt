package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule013AntallBarnUnder18 : AbstractNoArgsRule<List<KostraRecord>>(
    SosialRuleId.ANT_BU_18_13.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { it.fieldAs<Int>(ANT_BARN_UNDER_18_COL_NAME) < CHILD_COUNT_THRESHOLD }
        .map {
            createValidationReportEntry(
                "Antall barn (${it[ANT_BARN_UNDER_18_COL_NAME]}) under 18 år i husholdningen " +
                        "er $CHILD_COUNT_THRESHOLD eller flere, er dette riktig?",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private const val CHILD_COUNT_THRESHOLD = 14
    }
}