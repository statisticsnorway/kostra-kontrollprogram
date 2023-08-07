package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.ageInYears
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule022TilknytningTilTrygdesystemetOgAlder : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K022_TRYGDESYSTEMET_ALDER.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[TRYGDESIT_COL_NAME] == "07"
        }.filterNot {
            (it[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1) > 62
        }.map {
            createValidationReportEntry(
                "Mottakeren (${
                    (it[PERSON_FODSELSNR_COL_NAME].ageInYears(arguments.aargang.toInt()) ?: -1)
                } år) er 62 år eller yngre og mottar alderspensjon."
            )
        }.ifEmpty { null }
}