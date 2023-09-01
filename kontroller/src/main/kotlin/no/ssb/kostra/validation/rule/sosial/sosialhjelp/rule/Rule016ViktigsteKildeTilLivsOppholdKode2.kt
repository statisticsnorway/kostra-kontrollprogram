package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule016ViktigsteKildeTilLivsOppholdKode2 : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K016_VKLO_KURS.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { it[VKLO_COL_NAME] == "2" }
        .filterNot { it[ARBSIT_COL_NAME] in validCodes }
        .map {
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "er ${
                            fieldDefinitions.byColumnName(VKLO_COL_NAME).codeList
                                .first { item -> item.code == it[VKLO_COL_NAME] }.value
                        }. Arbeidssituasjonen er '(${it[ARBSIT_COL_NAME]})', " +
                        "forventet én av '(${
                            fieldDefinitions.byColumnName(ARBSIT_COL_NAME).codeList
                                .filter { item -> item.code in validCodes }
                        })'. Feltet er obligatorisk å fylle ut."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        val validCodes = listOf("03", "05", "06")
    }
}