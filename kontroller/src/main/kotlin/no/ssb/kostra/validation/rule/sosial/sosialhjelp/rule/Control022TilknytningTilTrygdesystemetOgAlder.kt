package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Control022TilknytningTilTrygdesystemetOgAlder : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIAL_K022_TRYGDESYSTEMET_ALDER.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[TRYGDESIT_COL_NAME] == "07"
        }.filterNot {
            (it[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1) > 62
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Mottakeren (${
                    (it[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1)
                } år) er 62 år eller yngre og mottar alderspensjon."
            )
        }
}