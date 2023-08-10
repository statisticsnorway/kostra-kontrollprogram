package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.PERMISJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.extensions.hasNotVarighet
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule028MaanederMedKvalifiseringsstonad : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.MAANEDER_MED_KVALIFISERINGSSTONAD_28.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            it[STATUS_COL_NAME] == PERMISJON
        }.filter {
            it.hasNotVarighet()
        }.map {
            createValidationReportEntry(
                "Det er ikke krysset av for hvilke måneder deltakeren har fått utbetalt " +
                        "kvalifiseringsstønad (${it[KVP_STONAD_COL_NAME]}) i løpet " +
                        "av rapporteringsåret. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
                individId = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }
}