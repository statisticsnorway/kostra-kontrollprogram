package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.extensions.hasVarighet
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId

class Rule031HarKvalifiseringssumMenManglerVarighet : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.HAR_KVALIFISERINGSSUM_MEN_MANGLER_VARIGHET_31.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[STATUS_COL_NAME] == "1"
        }.filter {
            0 < it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME)
        }.filterNot {
            it.hasVarighet()
        }.map {
            createValidationReportEntry(
                "Deltakeren har fått kvalifiseringsstønad (${it[KVP_STONAD_COL_NAME]}) " +
                        "i løpet av året, men mangler utfylling for hvilke måneder stønaden gjelder. Feltet er " +
                        "obligatorisk å fylle ut."
            )
        }.ifEmpty { null }
}