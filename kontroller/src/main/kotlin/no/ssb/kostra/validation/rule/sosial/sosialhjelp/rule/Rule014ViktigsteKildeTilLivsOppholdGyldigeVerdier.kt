package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VKLO_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule014ViktigsteKildeTilLivsOppholdGyldigeVerdier : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K014_VKLO_GYLDIGE_VERDIER.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ) = context.filter {
        fieldDefinitions
            .byColumnName(VKLO_COL_NAME)
            .codeIsMissing(it[VKLO_COL_NAME])
    }.takeIf { it.any() }?.map {
        createValidationReportEntry(
            "Mottakerens viktigste kilde til livsopphold ved siste kontakt med sosial-/NAV-kontoret " +
                    "skal oppgis. Fant '(${it[VKLO_COL_NAME]})', forventet én av " +
                    "'(${fieldDefinitions.byColumnName(VKLO_COL_NAME).codeListToString()})'."
        )
    }
}