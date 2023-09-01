package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule007AlderEr68AarEllerOver : AbstractRule<List<KostraRecord>>(
    SosialRuleId.ALDER_ER_96_AAR_ELLER_OVER_07.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt())
                ?.let { age -> 67 < age } ?: false
        }.map {
            val alder = it[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt())

            createValidationReportEntry(
                messageText = "Deltakeren ($alder år) er 68 år eller eldre.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SAKSBEHANDLER_COL_NAME],
                journalId = it[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}