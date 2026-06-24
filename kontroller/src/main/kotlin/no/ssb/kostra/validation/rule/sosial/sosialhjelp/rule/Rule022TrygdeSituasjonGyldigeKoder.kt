package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.codeIsMissing
import no.ssb.kostra.program.extension.codeListToString
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.AbstractNoArgsRule
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpRuleId

class Rule022TrygdeSituasjonGyldigeKoder :
    AbstractNoArgsRule<List<KostraRecord>>(
        SosialhjelpRuleId.SOSIALHJELP_K022_TRYGDESITUASJON_GYLDIGE_KODER.title,
        Severity.ERROR,
    ) {

    override fun validate(context: List<KostraRecord>) = context
        .filter {
            fieldDefinitions.byColumnName(TRYGDESIT_COL_NAME)
                .codeIsMissing(it[TRYGDESIT_COL_NAME])
        }
        .map {
            val validCodes = fieldDefinitions.byColumnName(TRYGDESIT_COL_NAME)
                .codeListToString()
            createValidationReportEntry(
                "Mottakerens trygdesituasjon ved siste kontakt med sosial-/NAV-kontoret skal oppgis. " +
                        "Fant '(${it[TRYGDESIT_COL_NAME]})', forventet én av '(${validCodes})'.",
                lineNumbers = listOf(it.lineNumber)
            ).copy(
                caseworker = it[SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME],
                journalId = it[SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME],
            )
        }.ifEmpty { null }
}