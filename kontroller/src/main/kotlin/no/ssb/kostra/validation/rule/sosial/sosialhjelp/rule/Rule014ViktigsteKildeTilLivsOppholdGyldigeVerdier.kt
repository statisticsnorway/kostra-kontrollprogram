package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule014ViktigsteKildeTilLivsOppholdGyldigeVerdier : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K014_VKLO_GYLDIGE_VERDIER.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filter { fieldDefinitions.byColumnName(VKLO_COL_NAME).codeIsMissing(it[VKLO_COL_NAME]) }
        .map {
            createValidationReportEntry(
                "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                        "skal oppgis. Fant '(${it[VKLO_COL_NAME]})', forventet én av " +
                        "'(${fieldDefinitions.byColumnName(VKLO_COL_NAME).codeListToString()})'.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}