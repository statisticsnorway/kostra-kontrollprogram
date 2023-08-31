package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule036StatusForDeltakelseIKvalifiseringsprogram : AbstractNoArgsRule<List<KostraRecord>>(
    KvalifiseringRuleId.STATUS_FOR_DELTAKELSE_I_KVALIFISERINGSPROGRAM_36.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot {
            fieldDefinitions.byColumnName(STATUS_COL_NAME).codeExists(it[STATUS_COL_NAME])
        }.map {
            createValidationReportEntry(
                "Korrigér status. Fant '${it[STATUS_COL_NAME]}', forventet én av " +
                        "'${fieldDefinitions.byColumnName(STATUS_COL_NAME).codeListToString()}'. " +
                        "Feltet er obligatorisk å fylle ut."
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}