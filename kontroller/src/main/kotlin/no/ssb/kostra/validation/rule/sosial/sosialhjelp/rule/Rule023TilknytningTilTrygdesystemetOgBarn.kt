package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule023TilknytningTilTrygdesystemetOgBarn : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K023_TRYGDESYSTEMET_BARN.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[TRYGDESIT_COL_NAME] == "05"
        }.filterNot {
            it[HAR_BARN_UNDER_18_COL_NAME] == "1" && it.fieldAs<Int>(ANT_BARN_UNDER_18_COL_NAME) > 0
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen."
            )
        }
}