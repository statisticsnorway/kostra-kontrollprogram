package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialRuleId

class Control020ViktigsteKildeTilLivsOppholdKode3 : AbstractRule<List<KostraRecord>>(
    SosialRuleId.SOSIAL_K020_TRYGD.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[VKLO_COL_NAME] == "3"
        }.filterNot {
            it[TRYGDESIT_COL_NAME] in validCodes
        }.takeIf {
            it.any()
        }?.map { kostraRecord ->
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "er ${
                            fieldDefinitions.byColumnName(VKLO_COL_NAME).codeList
                                .first { it.code == kostraRecord[VKLO_COL_NAME] }.value
                        }. Arbeidssituasjonen er '(${kostraRecord[TRYGDESIT_COL_NAME]})', " +
                        "forventet én av '(${
                            fieldDefinitions.byColumnName(TRYGDESIT_COL_NAME).codeList
                                .filter { it.code in validCodes }
                        })'. Feltet er obligatorisk å fylle ut."
            )
        }

    companion object {
        val validCodes = listOf("01", "02", "04", "05", "06", "07", "09", "10", "11")
    }
}