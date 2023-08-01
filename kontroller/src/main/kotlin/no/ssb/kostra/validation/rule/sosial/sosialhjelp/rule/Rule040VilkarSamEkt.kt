package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSAMEKT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule040VilkarSamEkt : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K040_VILKARSAMEKT.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filterNot {
            fieldDefinitions.byColumnName(VILKARSAMEKT_COL_NAME).codeExists(it[VILKARSAMEKT_COL_NAME])
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Det er ikke krysset av for om det stilles vilkår til mottakeren etter " +
                        "sosialtjenesteloven. Registreres for første vilkår i kalenderåret. Feltet er obligatorisk."
            )
        }
}