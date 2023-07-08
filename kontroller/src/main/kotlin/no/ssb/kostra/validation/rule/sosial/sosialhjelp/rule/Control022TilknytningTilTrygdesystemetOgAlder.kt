package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialRuleId

class Control022TilknytningTilTrygdesystemetOgAlder : AbstractRule<List<KostraRecord>>(
    SosialRuleId.SOSIAL_K022_TRYGDESYSTEMET_ALDER.title,
    Severity.WARNING
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[SosialColumnNames.VKLO_COL_NAME] == "5"
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
        val validCodes = listOf("02", "04", "05", "06", "07", "08")
    }
}