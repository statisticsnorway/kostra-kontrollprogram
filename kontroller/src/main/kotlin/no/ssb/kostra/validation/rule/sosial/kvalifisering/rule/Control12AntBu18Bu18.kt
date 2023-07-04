package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.JA
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control12AntBu18Bu18 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.ANT_BU_18_BU_18_12.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) = context[HAR_BARN_UNDER_18_COL_NAME]
        .takeUnless { it == JA }
        ?.let { context.fieldAs<Int?>(ANT_BARN_UNDER_18_COL_NAME) }
        ?.takeIf { numberOfChildren -> numberOfChildren > 0 }
        ?.let { numberOfChildren ->
            createSingleReportEntryList(
                "Det er oppgitt $numberOfChildren barn under 18 år som bor i husholdningen som " +
                        "mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt at " +
                        "det bor barn i husholdningen. Feltet er obligatorisk å fylle ut når det er oppgitt " +
                        "antall barn under 18 år som bor i husholdningen."
            )
        }
}