package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule020AViktigsteKildeTilLivsOppholdKode3 : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K020A_TRYGD.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { it[SosialhjelpColumnNames.VKLO_COL_NAME] == "3" }
        .filterNot { it[SosialhjelpColumnNames.TRYGDESIT_COL_NAME] in validCodes }
        .map {
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "er ${
                            SosialhjelpFieldDefinitions.fieldDefinitions.byColumnName(SosialhjelpColumnNames.VKLO_COL_NAME).codeList
                                .firstOrNull { item -> item.code == it[SosialhjelpColumnNames.VKLO_COL_NAME] }?.value ?: "ukjent"
                        }. Trygdesituasjonen er '(${it[SosialhjelpColumnNames.TRYGDESIT_COL_NAME]})', " +
                        "forventet én av '(${
                            SosialhjelpFieldDefinitions.fieldDefinitions.byColumnName(SosialhjelpColumnNames.TRYGDESIT_COL_NAME).codeList
                                .filter { item -> item.code in validCodes }
                        })'. Feltet er obligatorisk å fylle ut."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val validCodes = listOf("12")
    }
}