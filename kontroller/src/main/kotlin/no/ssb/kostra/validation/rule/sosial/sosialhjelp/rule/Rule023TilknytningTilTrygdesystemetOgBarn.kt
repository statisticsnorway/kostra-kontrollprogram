package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule023TilknytningTilTrygdesystemetOgBarn : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K023_TRYGDESYSTEMET_BARN.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[TRYGDESIT_COL_NAME] == "05" }
        .filterNot { it[HAR_BARN_UNDER_18_COL_NAME] == "1" && it.fieldAs<Int>(ANT_BARN_UNDER_18_COL_NAME) > 0 }
        .map {
            createValidationReportEntry(
                "Mottakeren mottar overgangsstønad, men det er ikke oppgitt barn under 18 år i husholdningen."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}