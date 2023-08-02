package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.sosial.SosialhjelpConstants
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule025ArbeidssituasjonGyldigeKoder : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K025_ARBEIDSSITUASJON.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filterNot {
            fieldDefinitions.byColumnName(ARBSIT_COL_NAME).codeExists(it[ARBSIT_COL_NAME])
        }.takeIf {
            it.any()
        }?.map { kostraRecord ->
            val arbeidssituasjon = Code(kostraRecord[ARBSIT_COL_NAME], SosialhjelpConstants.UNKNOWN)

            createValidationReportEntry(
                "Mottakerens arbeidssituasjon ved siste kontakt med sosial-/NAV-kontoret er ikke fylt ut, " +
                        "eller feil kode er benyttet. Utfylt verdi er '($arbeidssituasjon)'. " +
                        "Feltet er obligatorisk å fylle ut."
            )
        }
}
