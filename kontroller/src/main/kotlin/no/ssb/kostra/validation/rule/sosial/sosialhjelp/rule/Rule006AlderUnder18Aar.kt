package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule006AlderUnder18Aar : AbstractRule<List<KostraRecord>>(
    SosialRuleId.ALDER_UNDER_18_AAR_06.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            it[SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt())
                ?.let { age -> 18 <= age } ?: true
        }.map {
            val alder = it[SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt())

            createValidationReportEntry(
                messageText = "Deltakeren ($alder år) er under 18 år.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}