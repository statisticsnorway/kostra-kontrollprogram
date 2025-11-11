package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule007AlderEr96AarEllerOver : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K007_ALDER_ER_96_AAR_ELLER_OVER.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt())
                ?.let { age -> 96 <= age } ?: false
        }
        .map {
            val alder = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt())

            createValidationReportEntry(
                messageText = "Mottakeren ($alder år) er 96 år eller eldre.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}