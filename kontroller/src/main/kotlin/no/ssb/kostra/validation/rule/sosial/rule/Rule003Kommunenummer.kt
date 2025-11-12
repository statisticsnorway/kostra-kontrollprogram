package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule003Kommunenummer : AbstractRule<List<KostraRecord>>(
    SosialRuleId.KOMMUNENUMMER_03.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .map { kostraRecord ->
            val kommunenummer = arguments.region.municipalityIdFromRegion()
            kostraRecord to kommunenummer
        }.filter { (kostraRecord, kommunenummer) ->
            kostraRecord[KOMMUNE_NR_COL_NAME] != kommunenummer
        }.map { (kostraRecord, kommunenummer) ->
            createValidationReportEntry(
                "Korrigér kommunenummeret. Fant ${kostraRecord[KOMMUNE_NR_COL_NAME]}, " +
                        "forventet $kommunenummer.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            ).copy(
                caseworker = kostraRecord[SAKSBEHANDLER_COL_NAME],
                journalId = kostraRecord[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}