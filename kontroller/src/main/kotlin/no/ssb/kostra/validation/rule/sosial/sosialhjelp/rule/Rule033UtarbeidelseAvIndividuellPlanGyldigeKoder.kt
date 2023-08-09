package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpConstants.UNKNOWN
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule033UtarbeidelseAvIndividuellPlanGyldigeKoder : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K033_INDIVIDUELLPLAN.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filterNot {
            fieldDefinitions.byColumnName(FAAT_INDIVIDUELL_PLAN_COL_NAME).codeExists(it[FAAT_INDIVIDUELL_PLAN_COL_NAME])
        }.map { kostraRecord ->
            val utfylt = Code(kostraRecord[FAAT_INDIVIDUELL_PLAN_COL_NAME], UNKNOWN)

            createValidationReportEntry(
                "Det er ikke krysset av for om mottakeren har fått utarbeidet individuell plan. " +
                        "Utfylt verdi er '($utfylt)'. Feltet er obligatorisk å fylle ut."
            )
        }.ifEmpty { null }
}