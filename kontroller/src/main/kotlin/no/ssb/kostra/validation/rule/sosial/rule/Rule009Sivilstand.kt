package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule009Sivilstand : AbstractRule<List<KostraRecord>>(
    SosialRuleId.SIVILSTAND_09.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            fieldDefinitions.byColumnName(EKTSTAT_COL_NAME).codeExists(it[EKTSTAT_COL_NAME])
        }.map {
            createValidationReportEntry(
                "Korrigér sivilstand. Fant '${it[EKTSTAT_COL_NAME]}', forventet én av " +
                        "${fieldDefinitions.byColumnName(EKTSTAT_COL_NAME).codeListToString()}." +
                        " Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er " +
                        "ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}