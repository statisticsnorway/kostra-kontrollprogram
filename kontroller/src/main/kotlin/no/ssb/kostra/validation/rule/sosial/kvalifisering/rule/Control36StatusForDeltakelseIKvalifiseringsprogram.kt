package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.program.extension.findByColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control36StatusForDeltakelseIKvalifiseringsprogram : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.STATUS_FOR_DELTAKELSE_I_KVALIFISERINGSPROGRAM_36.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (fieldDefinitions.findByColumnName(STATUS_COL_NAME) to context.getFieldAsString(STATUS_COL_NAME))
            .takeUnless { (fieldDefinition, value) -> fieldDefinition.codeExists(value) }
            ?.let { (fieldDefinition, value) ->
                createSingleReportEntryList(
                    "Korrigér status. Fant '$value', forventet én av " +
                            "'${fieldDefinition.codeListToString()}'. Feltet er obligatorisk å fylle ut."
                )
            }
}