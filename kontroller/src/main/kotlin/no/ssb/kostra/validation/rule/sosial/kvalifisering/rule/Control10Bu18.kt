package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control10Bu18 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.BU_18_10.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (fieldDefinitions.byColumnName(HAR_BARN_UNDER_18_COL_NAME) to context[HAR_BARN_UNDER_18_COL_NAME])
            .takeIf { (fieldDefinition, value) -> fieldDefinition.codeIsMissing(value) }
            ?.let { (fieldDefinition, value) ->
                createSingleReportEntryList(
                    "Korrigér forsørgerplikt. Fant '$value', forventet én av " +
                            "${fieldDefinition.codeListToString()}'. Det er ikke krysset av for om deltakeren " +
                            "har barn under 18 år, som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt " +
                            "for, og som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut."
                )
            }
}