package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule020AViktigsteKildeTilLivsOppholdKode3 : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K020A_TRYGD.title,
    Severity.WARNING
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { it[VKLO_COL_NAME] == "3" }
        .filterNot { it[TRYGDESIT_COL_NAME] in validCodes }
        .map { kostraRecord ->
            val vkloNullable = SosialhjelpFieldDefinitions
                .fieldDefinitions
                .byColumnName(VKLO_COL_NAME)
                .codeList
                .firstOrNull { item -> item.code == kostraRecord[VKLO_COL_NAME] }

            val codeList = SosialhjelpFieldDefinitions
                .fieldDefinitions
                .byColumnName(TRYGDESIT_COL_NAME)
                .codeList
                .filter { item -> item.code in validCodes }

            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "er ikke trygd/pensjon. Trygdesituasjonen er '(${kostraRecord[TRYGDESIT_COL_NAME]})', " +
                        "forventet én av '(${codeList})'. Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(kostraRecord.lineNumber)
            ).copy(
                caseworker = kostraRecord[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = kostraRecord[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val validCodes = listOf("12")
    }
}