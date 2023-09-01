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

class Rule011HarBarnUnder18MotAntallBarnUnder18 : AbstractNoArgsRule<List<KostraRecord>>(
    SosialRuleId.BU_18_ANT_BU_18_11.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[HAR_BARN_UNDER_18_COL_NAME] == JA }
        .filterNot { 0 < it.fieldAs<Int>(ANT_BARN_UNDER_18_COL_NAME) }
        .map {
            createValidationReportEntry(
                "Det er krysset av for at det bor barn under 18 år i husholdningen som mottaker eller " +
                        "ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt hvor mange barn " +
                        "'(${it[ANT_BARN_UNDER_18_COL_NAME]})' som bor i husholdningen. Feltet er obligatorisk å " +
                        "fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}