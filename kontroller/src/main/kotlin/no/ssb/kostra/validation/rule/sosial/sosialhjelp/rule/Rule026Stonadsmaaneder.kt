package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.LAAN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_10_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_11_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_12_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_1_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_2_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_3_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_4_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_5_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_6_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_7_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_8_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.STMND_9_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule026Stonadsmaaneder : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K026_STONADSMAANEDER.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filterNot {
            listOf(
                STMND_1_COL_NAME,
                STMND_2_COL_NAME,
                STMND_3_COL_NAME,
                STMND_4_COL_NAME,
                STMND_5_COL_NAME,
                STMND_6_COL_NAME,
                STMND_7_COL_NAME,
                STMND_8_COL_NAME,
                STMND_9_COL_NAME,
                STMND_10_COL_NAME,
                STMND_11_COL_NAME,
                STMND_12_COL_NAME
            ).any { fieldName ->
                fieldDefinitions.byColumnName(fieldName).codeExists(it[fieldName])
            }
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Det er ikke krysset av for hvilke måneder mottakeren har fått utbetalt økonomisk " +
                        "sosialhjelp (bidrag (${it[BIDRAG_COL_NAME]}) eller lån (${it[LAAN_COL_NAME]})) i løpet " +
                        "av rapporteringsåret. Feltet er obligatorisk å fylle ut."
            )
        }
}
