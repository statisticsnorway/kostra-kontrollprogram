package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.SharedConstants.OSLO_DISTRICTS
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule003Bydelsnummer : AbstractRule<List<KostraRecord>>(
    SosialRuleId.BYDELSNUMMER_03.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            when (it[KOMMUNE_NR_COL_NAME]) {
                OSLO_MUNICIPALITY_ID -> it[BYDELSNR_COL_NAME] in OSLO_DISTRICTS
                else -> it[BYDELSNR_COL_NAME].isBlank()
            }
        }.map {
            createValidationReportEntry(
                when (it[KOMMUNE_NR_COL_NAME]) {
                    OSLO_MUNICIPALITY_ID -> "Korrigér bydel. Fant ${it[BYDELSNR_COL_NAME]}, forventet en av ${OSLO_DISTRICTS}."
                    else -> "Korrigér bydel. Fant ${it[BYDELSNR_COL_NAME]}, forventet blank / '  '."
                },
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}
