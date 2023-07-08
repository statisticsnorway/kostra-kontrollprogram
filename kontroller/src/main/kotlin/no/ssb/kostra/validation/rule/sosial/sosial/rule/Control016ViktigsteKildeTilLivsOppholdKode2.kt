package no.ssb.kostra.validation.rule.sosial.sosial.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosial.SosialRuleId

class Control016ViktigsteKildeTilLivsOppholdKode2 : AbstractRule<List<KostraRecord>>(
    SosialRuleId.K016_VKLO_KURS.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[SosialColumnNames.VKLO_COL_NAME] == "2"
        }.filterNot {
            it[SosialColumnNames.ARBSIT_COL_NAME] in validCodes
        }.takeIf {
            it.any()
        }?.map { kostraRecord ->
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "er ${
                            SosialFieldDefinitions.fieldDefinitions.byColumnName(SosialColumnNames.VKLO_COL_NAME).codeList
                                .first { it.code == kostraRecord[SosialColumnNames.VKLO_COL_NAME] }.value
                        }. Arbeidssituasjonen er '(${kostraRecord[SosialColumnNames.ARBSIT_COL_NAME]})', " +
                        "forventet én av '(${
                            SosialFieldDefinitions.fieldDefinitions.byColumnName(SosialColumnNames.ARBSIT_COL_NAME).codeList
                                .filter { it.code in validCodes }
                        })'. Feltet er obligatorisk å fylle ut."
            )
        }

    companion object {
        val validCodes = listOf("03", "05", "06")
    }
}