package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.JA
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule012AntallBarnUnder18MotHarBarnUnder18 : AbstractNoArgsRule<List<KostraRecord>>(
    SosialRuleId.ANT_BU_18_BU_18_12.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            0 < it.fieldAs<Int>(ANT_BARN_UNDER_18_COL_NAME)
        }.filterNot {
            it[HAR_BARN_UNDER_18_COL_NAME] == JA
        }.map {
            createValidationReportEntry(
                "Det er oppgitt ${it[ANT_BARN_UNDER_18_COL_NAME]} barn under 18 år som bor i husholdningen som " +
                        "mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt at " +
                        "det bor barn i husholdningen. Feltet er obligatorisk å fylle ut når det er oppgitt " +
                        "antall barn under 18 år som bor i husholdningen.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}