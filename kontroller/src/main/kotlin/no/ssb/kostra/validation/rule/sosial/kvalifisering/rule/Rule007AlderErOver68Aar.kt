package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
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
        .filter {
            it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME]
                .ageInYears(arguments.aargang.toInt())
                ?.let { age -> MAX_AGE < age } ?: false
        }.map {
            val alder = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME]
                .ageInYears(arguments.aargang.toInt())

            createValidationReportEntry(
                messageText = "Mottakeren ($alder år) er over $MAX_AGE år.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        const val MAX_AGE = 68
    }
}