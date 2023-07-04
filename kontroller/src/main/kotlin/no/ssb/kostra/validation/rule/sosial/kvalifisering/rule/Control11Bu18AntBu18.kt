package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.JA
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control11Bu18AntBu18 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.BU_18_ANT_BU_18_11.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) = context[HAR_BARN_UNDER_18_COL_NAME]
        .takeIf { it == JA }
        ?.let { context.fieldAs<Int>(ANT_BARN_UNDER_18_COL_NAME) }
        ?.takeIf { numberOfChildren -> numberOfChildren < 1 }
        ?.let { numberOfChildren ->
            createSingleReportEntryList(
                "Det er krysset av for at det bor barn under 18 år i husholdningen som mottaker eller " +
                        "ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt hvor mange barn " +
                        "'($numberOfChildren)' som bor i husholdningen. Feltet er obligatorisk å fylle ut når " +
                        "det er oppgitt at det bor barn under 18 år i husholdningen."
            )
        }
}