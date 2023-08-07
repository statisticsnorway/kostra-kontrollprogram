package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule016ViktigsteKildeTilLivsOppholdKode2 : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K016_VKLO_KURS.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
        .filter {
            it[VKLO_COL_NAME] == "2"
        }.filterNot {
            it[ARBSIT_COL_NAME] in validCodes
        }.map { kostraRecord ->
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
        }.ifEmpty { null }

    companion object {
        val validCodes = listOf("03", "05", "06")
    }
}