package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.codeIsMissing
import no.ssb.kostra.area.sosial.kvalifisering.codeListToString
import no.ssb.kostra.area.sosial.kvalifisering.findByColumnName
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control10Bu18 : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.BU_18_10.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments): List<ValidationReportEntry>? {
        val fieldDefinition = fieldDefinitions.findByColumnName(BU18_COL_NAME)
        val value = context.getFieldAsTrimmedString(BU18_COL_NAME)

        return if (fieldDefinition.codeIsMissing(value)) {
            createSingleReportEntryList(
                "Korrigér forsørgerplikt. Fant '$value', forventet én av " +
                        "${fieldDefinition.codeListToString()}'. Det er ikke krysset av for om deltakeren " +
                        "har barn under 18 år, som deltakeren (eventuelt ektefelle/samboer) har forsørgerplikt for, og " +
                        "som bor i husholdningen ved siste kontakt. Feltet er obligatorisk å fylle ut."
            )
        } else null
    }
}