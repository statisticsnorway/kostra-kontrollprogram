package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_9_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule037LaanSum : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K037_LAANSUM.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .map {
            it to months.sumOf { month -> it.fieldAsIntOrDefault(month) }
        }.filterNot {
            it.first.fieldAsIntOrDefault(LAAN_COL_NAME) == it.second
        }.map {
            createValidationReportEntry(
                "Det er ikke fylt ut lån (${it.second}) fordelt på måneder eller sum stemmer ikke " +
                        "med sum lån (${it.first[LAAN_COL_NAME]}) utbetalt i løpet av året.",
                lineNumbers = listOf(it.first.lineNumber)
            ).copy(
                caseworker = it.first[SAKSBEHANDLER_COL_NAME],
                journalId = it.first[PERSON_JOURNALNR_COL_NAME],
                individId = it.first[PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        val months = listOf(
            LAAN_1_COL_NAME,
            LAAN_2_COL_NAME,
            LAAN_3_COL_NAME,
            LAAN_4_COL_NAME,
            LAAN_5_COL_NAME,
            LAAN_6_COL_NAME,
            LAAN_7_COL_NAME,
            LAAN_8_COL_NAME,
            LAAN_9_COL_NAME,
            LAAN_10_COL_NAME,
            LAAN_11_COL_NAME,
            LAAN_12_COL_NAME,
        )
    }
}