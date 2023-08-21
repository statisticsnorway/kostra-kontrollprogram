package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARANNET_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARARBEID_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARDIGPLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARJOBBLOG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARJOBBTILB_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARKURS_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARLIVSH_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKAROKRETT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSAMT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARUTD_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule043UtfyltVilkar : AbstractRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K043_TYPEVILKAR.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>, arguments: KotlinArguments) = context
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
        }.map {
            createValidationReportEntry(
                "Feltet for 'Hvis ja på spørsmålet Stilles det vilkår til mottakeren etter " +
                        "sosialtjenesteloven', så skal det oppgis hvilke vilkår som stilles til mottakeren. " +
                        "Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}