package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule027StonadssumMangler : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K027_STONADSSUM.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            it.fieldAsIntOrDefault(BIDRAG_COL_NAME) > 0 || it.fieldAsIntOrDefault(LAAN_COL_NAME) > 0
        }.map {
            createValidationReportEntry(
                "Det er ikke oppgitt hvor mye mottakeren har fått i økonomisk sosialhjelp " +
                        "(bidrag (${it[BIDRAG_COL_NAME]}) eller lån (${it[LAAN_COL_NAME]})) i løpet av året, " +
                        "eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}
