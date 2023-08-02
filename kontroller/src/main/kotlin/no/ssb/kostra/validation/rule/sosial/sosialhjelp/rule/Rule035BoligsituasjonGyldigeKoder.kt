package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.BOSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.sosial.SosialhjelpConstants.UNKNOWN
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule035BoligsituasjonGyldigeKoder : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K035_BOLIGSITUASJON.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filterNot {
            fieldDefinitions.byColumnName(BOSIT_COL_NAME).codeExists(it[BOSIT_COL_NAME])
        }.takeIf {
            it.any()
        }?.map { kostraRecord ->
            val utfylt = Code(kostraRecord[BOSIT_COL_NAME], UNKNOWN)

            createValidationReportEntry(
                "Det er ikke krysset av for mottakerens boligsituasjon. Utfylt verdi er '($utfylt)'. " +
                        "Feltet er obligatorisk å fylle ut."
            )
        }
}
