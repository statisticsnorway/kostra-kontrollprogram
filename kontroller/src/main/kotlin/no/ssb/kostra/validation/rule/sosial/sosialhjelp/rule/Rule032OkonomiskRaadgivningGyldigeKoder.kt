package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.GITT_OKONOMIRAD_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpConstants.UNKNOWN
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule032OkonomiskRaadgivningGyldigeKoder : AbstractNoArgsRule<List<KostraRecord>>(
    SosialhjelpRuleId.SOSIALHJELP_K032_OKONIMISKRAADGIVNING.title,
    Severity.ERROR
) {
    override fun validate(context: List<KostraRecord>) = context
        .filterNot { fieldDefinition.codeExists(it[GITT_OKONOMIRAD_COL_NAME]) }
        .map {
            val utfylt = Code(it[GITT_OKONOMIRAD_COL_NAME], UNKNOWN)

            createValidationReportEntry(
                "Det er ikke krysset av for om mottakeren er gitt økonomisk rådgiving i forbindelse med " +
                        "utbetaling av økonomisk sosialhjelp. Utfylt verdi er '($utfylt)'. " +
                        "Feltet er obligatorisk å fylle ut.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }

    companion object {
        private val fieldDefinition = fieldDefinitions.byColumnName(GITT_OKONOMIRAD_COL_NAME)
    }
}
