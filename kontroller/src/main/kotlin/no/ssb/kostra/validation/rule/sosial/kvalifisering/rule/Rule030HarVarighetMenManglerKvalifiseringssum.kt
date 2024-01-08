package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.PERMISJON
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.extensions.hasVarighet
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRuleId


class Rule030HarVarighetMenManglerKvalifiseringssum : AbstractNoArgsRule<List<KostraRecord>>(
    KvalifiseringRuleId.HAR_VARIGHET_MEN_MANGLER_KVALIFISERINGSSUM_30.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter {
            it[STATUS_COL_NAME] != PERMISJON
                    && it.hasVarighet()
                    && it.fieldAsTrimmedString(KVP_STONAD_COL_NAME).isBlank()
        }.map {
            createValidationReportEntry(
                "Det er ikke oppgitt hvor mye deltakeren har fått i kvalifiseringsstønad " +
                        "(${it[KVP_STONAD_COL_NAME]}) i løpet av " +
                        "året, eller feltet inneholder andre tegn enn tall."
            ).copy(
                caseworker = it[KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}