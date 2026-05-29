package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule023ATilknytningTilTrygdesystemetOgAlder :
    AbstractRule<List<KostraRecord>>(
        SosialhjelpRuleId.SOSIALHJELP_K023A_TRYGDESYSTEMET_ALDER.title,
        Severity.ERROR,
    ) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments,
    ) = context
        .filter { record ->  record[TRYGDESIT_COL_NAME] == "07" }
        .map { record ->  (record to (record[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1)) }
        .filter { (_, age) -> age < 62 }
        .map {(record, age) ->
            createValidationReportEntry(
                "Mottakeren (${age} år) er yngre enn 62 år og mottar alderspensjon.",
                lineNumbers = listOf(record.lineNumber),
            ).copy(
                caseworker = record[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = record[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}
