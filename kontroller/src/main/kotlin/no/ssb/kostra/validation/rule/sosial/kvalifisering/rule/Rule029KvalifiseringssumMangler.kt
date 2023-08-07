package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule029KvalifiseringssumMangler : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.KVALIFISERINGSSUM_MANGLER_29.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            0 < it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME)
        }.map {
            createValidationReportEntry(
                "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
                        "(${it.fieldAsTrimmedString(KVP_STONAD_COL_NAME)}) i løpet av " +
                        "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
                individId = it[KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME],
            )
        }.ifEmpty { null }
}