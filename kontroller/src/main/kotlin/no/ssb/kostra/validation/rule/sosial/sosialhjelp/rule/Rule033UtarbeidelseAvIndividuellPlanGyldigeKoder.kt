package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpConstants.UNKNOWN
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule033UtarbeidelseAvIndividuellPlanGyldigeKoder : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K033_INDIVIDUELLPLAN.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { fieldDefinition.codeExists(it[FAAT_INDIVIDUELL_PLAN_COL_NAME]) }
        .map {
            val utfylt = Code(it[FAAT_INDIVIDUELL_PLAN_COL_NAME], UNKNOWN)

            createValidationReportEntry(
                "Det er ikke krysset av for om mottakeren har fått utarbeidet individuell plan. " +
                        "Utfylt verdi er '($utfylt)'. Feltet er obligatorisk å fylle ut."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val fieldDefinition = fieldDefinitions.byColumnName(FAAT_INDIVIDUELL_PLAN_COL_NAME)
    }
}