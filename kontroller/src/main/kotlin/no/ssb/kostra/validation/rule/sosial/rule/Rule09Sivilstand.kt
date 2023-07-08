package no.ssb.kostra.validation.rule.sosial.rule

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.SosialRuleId

class Rule09Sivilstand : AbstractRule<KostraRecord>(
    SosialRuleId.SIVILSTAND_09.title,
    Severity.ERROR
) {
    override fun validate(context: KostraRecord, arguments: KotlinArguments) =
        (fieldDefinitions.byColumnName(EKTSTAT_COL_NAME) to context[EKTSTAT_COL_NAME])
            .takeIf { (fieldDefinition, value) -> fieldDefinition.codeIsMissing(value) }
            ?.let { (fieldDefinition, value) ->
                createSingleReportEntryList(
                    "Korrigér sivilstand. Fant '$value, forventet én av ${fieldDefinition.codeListToString()}." +
                            " Mottakerens sivilstand/sivilstatus ved siste kontakt med sosial-/NAV-kontoret er " +
                            "ikke fylt ut, eller feil kode er benyttet. Feltet er obligatorisk å fylle ut."
                )
            }
}