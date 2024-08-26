package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.fieldAs
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule023BTilknytningTilTrygdesystemetOgBarn: AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K023B_TRYGDESYSTEMET_BARN.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[SosialhjelpColumnNames.TRYGDESIT_COL_NAME] == "13" }
        .filterNot { it[KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME] == "1" && it.fieldAs<Int>(
            SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME
        ) > 0 }
        .map {
            createValidationReportEntry(
                "Mottakeren mottar barnetrygd, men det er ikke oppgitt barn under 18 år i husholdningen.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}