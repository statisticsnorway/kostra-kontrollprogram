package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.PERMISJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.extensions.hasVarighet
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId


class Control30HarVarighetMenManglerKvalifiseringssum : AbstractRule<List<KostraRecord>>(
    KvalifiseringRuleId.HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[STATUS_COL_NAME] != PERMISJON
                    && it.hasVarighet()
                    && (
                    it.fieldAsTrimmedString(KVP_STONAD_COL_NAME).isBlank()
                            ||
                            it.fieldAsIntOrDefault(KVP_STONAD_COL_NAME) == 0
                    )
        }.map {
            createValidationReportEntry(
                "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
                        "(${it[KVP_STONAD_COL_NAME]}) i løpet av " +
                        "året, eller feltet inneholder andre tegn enn tall. Feltet er obligatorisk å fylle ut."
            )
        }.ifEmpty { null }
}