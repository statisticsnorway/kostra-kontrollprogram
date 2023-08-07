package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_9_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule036BidragSum : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K036_BIDRAGSUM.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .map {
            it to months.sumOf { month -> it.fieldAsIntOrDefault(month) }
        }.filterNot {
            it.first.fieldAsIntOrDefault(BIDRAG_COL_NAME) == it.second
        }.map {
            createValidationReportEntry(
                "Det er ikke fylt ut bidrag (${it.second}) fordelt på måneder eller sum stemmer ikke " +
                        "med sum bidrag (${it.first[BIDRAG_COL_NAME]}) utbetalt i løpet av året.",
                lineNumbers = listOf(it.first.lineNumber)
            ).copy(
                caseworker = it.first[SAKSBEHANDLER_COL_NAME],
                journalId = it.first[PERSON_JOURNALNR_COL_NAME],
                individId = it.first[PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        val months = listOf(
            BIDRAG_1_COL_NAME,
            BIDRAG_2_COL_NAME,
            BIDRAG_3_COL_NAME,
            BIDRAG_4_COL_NAME,
            BIDRAG_5_COL_NAME,
            BIDRAG_6_COL_NAME,
            BIDRAG_7_COL_NAME,
            BIDRAG_8_COL_NAME,
            BIDRAG_9_COL_NAME,
            BIDRAG_10_COL_NAME,
            BIDRAG_11_COL_NAME,
            BIDRAG_12_COL_NAME,
        )
    }
}
