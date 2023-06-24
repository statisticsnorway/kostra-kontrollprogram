package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BU18_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control12AntBu18Bu18 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.ANT_BU_18_BU_18_12.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsString(BU18_COL_NAME).takeUnless { it == "1" }
            ?.let { context.getFieldAsIntegerOrDefault(ANT_BU18_COL_NAME) }
            ?.takeIf { it > 0 }
            ?.let { numberOfChildren ->
                createSingleReportEntryList(
                    "Det er oppgitt $numberOfChildren barn under 18 år som bor i husholdningen som " +
                            "mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt at " +
                            "det bor barn i husholdningen. Feltet er obligatorisk å fylle ut når det er oppgitt " +
                            "antall barn under 18 år som bor i husholdningen."
                )
            }
}