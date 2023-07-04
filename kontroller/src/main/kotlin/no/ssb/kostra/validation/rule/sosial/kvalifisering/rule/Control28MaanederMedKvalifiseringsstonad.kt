package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.PERMISJON
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.findByColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Control28MaanederMedKvalifiseringsstonad : AbstractRule<KostraRecord>(
    KvalifiseringRuleId.MAANEDER_MED_KVALIFISERINGSSTONAD_28.title,
    Severity.WARNING
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        context.getFieldAsString(STATUS_COL_NAME)
            .takeUnless { it == PERMISJON }
            ?.takeIf {
                (1..12).all { monthId ->
                    fieldDefinitions.findByColumnName("$MONTH_PREFIX$monthId").codeIsMissing(
                        context.getFieldAsString("$MONTH_PREFIX$monthId")
                    )
                }
            }?.let {
                createSingleReportEntryList(
                    "Det er ikke krysset av for hvilke måneder deltakeren har fått utbetalt " +
                            "kvalifiseringsstønad (${fieldDefinitions.findByColumnName(KVP_STONAD_COL_NAME)}) i løpet " +
                            "av rapporteringsåret. Feltet er obligatorisk å fylle ut."
                )
            }

    companion object {
        internal const val MONTH_PREFIX = "STMND_"
    }
}