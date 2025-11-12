package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME
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
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ) = context
        .map { kostraRecord ->
            val age = kostraRecord[PERSON_FODSELSNR_COL_NAME]
                .ageInYears(arguments.aargang.toInt()) ?: 0
            kostraRecord to age
        }.filter { (_, age) ->
            AGE_THRESHOLD <= age
        }.map { (kostraRecord, age) ->
            createValidationReportEntry(
                messageText = "Mottakeren ($age år) er $AGE_THRESHOLD år eller eldre.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            ).copy(
                caseworker = kostraRecord[SAKSBEHANDLER_COL_NAME],
                journalId = kostraRecord[PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        const val AGE_THRESHOLD = 96
    }
}