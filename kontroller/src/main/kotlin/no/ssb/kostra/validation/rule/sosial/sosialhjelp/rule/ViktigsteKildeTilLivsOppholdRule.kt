package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule

abstract class ViktigsteKildeTilLivsOppholdRule(
    ruleName: String,
    severity: Severity
) : AbstractNoArgsRule<List<KostraRecord>>(
    ruleName = ruleName,
    severity = severity
) {
    protected abstract val vkldColumnFilter: String
    protected abstract val validCodes: Set<String>

    override fun validate(context: List<KostraRecord>) = context
        .filter { it[VKLO_COL_NAME] == vkldColumnFilter }
        .filterNot { it[ARBSIT_COL_NAME] in validCodes }
        .map {
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret er " +
                        "${vkldFieldDefinition.codeList.first { item -> item.code == it[VKLO_COL_NAME] }.value}. " +
                        "Arbeidssituasjonen er '(${it[ARBSIT_COL_NAME]})', forventet én av " +
                        "'(${arbSitFieldDefinition.codeList.filter { item -> item.code in validCodes }})'. " +
                        "Feltet er obligatorisk å fylle ut."
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val vkldFieldDefinition = SosialhjelpFieldDefinitions.fieldDefinitions.byColumnName(VKLO_COL_NAME)
        private val arbSitFieldDefinition = SosialhjelpFieldDefinitions.fieldDefinitions.byColumnName(ARBSIT_COL_NAME)
    }
}