package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.extensions.hasVarighet
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule031HarKvalifiseringssumMenManglerVarighet : AbstractNoArgsRule<List<KostraRecord>>(
    KvalifiseringRuleId.HAR_KVALIFISERINGSSUM_MEN_MANGLER_VARIGHET_31.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[STATUS_COL_NAME] == "1" && 0 < it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME) }
        .filterNot { it.hasVarighet() }
        .map {
            createValidationReportEntry(
                "Deltakeren har fått kvalifiseringsstønad (${it[KVP_STONAD_COL_NAME]}) " +
                        "i løpet av året, men mangler utfylling for hvilke måneder stønaden gjelder. Feltet er " +
                        "obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}