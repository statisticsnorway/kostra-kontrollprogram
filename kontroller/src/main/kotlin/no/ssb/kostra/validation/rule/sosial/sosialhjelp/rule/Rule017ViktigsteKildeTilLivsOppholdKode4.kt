package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule017ViktigsteKildeTilLivsOppholdKode4 : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K017_VKLO_STIPEND.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[VKLO_COL_NAME] == "4"
        }.filterNot {
            it[ARBSIT_COL_NAME] in validCodes
        }.takeIf {
            it.any()
        }?.map { kostraRecord ->
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "er ${
                            fieldDefinitions.byColumnName(VKLO_COL_NAME).codeList
                                .first { it.code == kostraRecord[VKLO_COL_NAME] }.value
                        }. Arbeidssituasjonen er '(${kostraRecord[ARBSIT_COL_NAME]})', " +
                        "forventet én av '(${
                            fieldDefinitions.byColumnName(ARBSIT_COL_NAME).codeList
                                .filter { it.code in validCodes }
                        })'. Feltet er obligatorisk å fylle ut."
            )
        }

    companion object {
        val validCodes = listOf("03")
    }
}