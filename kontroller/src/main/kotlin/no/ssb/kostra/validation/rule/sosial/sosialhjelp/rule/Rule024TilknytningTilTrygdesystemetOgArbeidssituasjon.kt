package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.sosial.sosial.SosialhjelpConstants.UNKNOWN
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule024TilknytningTilTrygdesystemetOgArbeidssituasjon : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K024_TRYGDESYSTEMET_ARBEIDSSITUASJON.title,
    Severity.WARNING
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[TRYGDESIT_COL_NAME] in listOf("04", "07")
        }.filterNot {
            it[ARBSIT_COL_NAME] in listOf("02", "04", "07")
        }.takeIf {
            it.any()
        }?.map { kostraRecord ->
            val trygdesituasjon = fieldDefinitions.byColumnName(TRYGDESIT_COL_NAME).codeList.firstOrNull {
                it.code == kostraRecord[TRYGDESIT_COL_NAME]
            } ?: Code(kostraRecord[TRYGDESIT_COL_NAME], UNKNOWN)

            val arbeidssituasjon = fieldDefinitions.byColumnName(ARBSIT_COL_NAME).codeList.firstOrNull {
                it.code == kostraRecord[ARBSIT_COL_NAME]
            } ?: Code(kostraRecord[ARBSIT_COL_NAME], UNKNOWN)

            createValidationReportEntry(
                "Mottakeren mottar trygd ($trygdesituasjon), " +
                        "men det er oppgitt ugyldig kode ($arbeidssituasjon) på arbeidssituasjon."
            )
        }
}
