package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARANNET_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARARBEID_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARDIGPLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARJOBBLOG_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARJOBBTILB_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARKURS_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARLIVSH_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKAROKRETT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSAMT_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialColumnNames.VILKARUTD_COL_NAME
import no.ssb.kostra.area.sosial.sosial.SosialFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule043UtfyltVilkar : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K037_LAANSUM.title,
    Severity.ERROR
) {
    override fun validate(
        context: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? = context
        .filter {
            it[VILKARSOSLOV_COL_NAME] == "1"
        }.filterNot {
            listOf(
                VILKARARBEID_COL_NAME,
                VILKARKURS_COL_NAME,
                VILKARUTD_COL_NAME,
                VILKARJOBBLOG_COL_NAME,
                VILKARJOBBTILB_COL_NAME,
                VILKARSAMT_COL_NAME,
                VILKAROKRETT_COL_NAME,
                VILKARLIVSH_COL_NAME,
                VILKARHELSE_COL_NAME,
                VILKARANNET_COL_NAME,
                VILKARDIGPLAN_COL_NAME,
            ).any { vilkar ->
                fieldDefinitions.byColumnName(vilkar).codeExists(it[vilkar])
            }
        }.takeIf {
            it.any()
        }?.map {
            createValidationReportEntry(
                "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter " +
                        "sosialtjenesteloven', så skal det oppgis hvilke vilkår som stilles til mottakeren. " +
                        "Feltet er obligatorisk å fylle ut."
            )
        }
}