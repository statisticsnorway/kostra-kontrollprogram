package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule008Kjonn : AbstractNoArgsRule<List<KostraRecord>>(
    SosialRuleId.KJONN_08.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { fieldDefinition.codeExists(it[KJONN_COL_NAME]) }
        .map {
            createValidationReportEntry(
                "Korrigér kjønn. Fant '${it[KJONN_COL_NAME]}', forventet én av " +
                        "${fieldDefinitions.byColumnName(KJONN_COL_NAME).codeListToString()}." +
                        " Mottakerens kjønn er ikke fylt ut, eller feil kode er benyttet. " +
                        "Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val fieldDefinition = fieldDefinitions.byColumnName(KJONN_COL_NAME)
    }
}