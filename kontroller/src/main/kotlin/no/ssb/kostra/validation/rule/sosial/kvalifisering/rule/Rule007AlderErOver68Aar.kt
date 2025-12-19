package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule007AlderErOver68Aar : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.KVALIFISERING_07_ALDER_ER_OVER_68_AAR.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .map { kostraRecord ->
            val age = kostraRecord[PERSON_FODSELSNR_COL_NAME]
                .ageInYears(arguments.aargang.toInt()) ?: 0
            kostraRecord to age
        }.filter { (_, age) ->
            AGE_THRESHOLD < age
        }.map { (kostraRecord, age) ->
            createValidationReportEntry(
                messageText = "Deltakeren ($age år) er over $AGE_THRESHOLD år.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            ).copy(
                caseworker = kostraRecord[SAKSBEHANDLER_COL_NAME],
                journalId = kostraRecord[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        const val AGE_THRESHOLD = 68
    }
}