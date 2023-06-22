package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.toKostraRecord
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRules.kvalifiseringRules
import no.ssb.kostra.validation.rule.sosial.rule.Rule05aFoedselsnummerDubletter
import no.ssb.kostra.validation.rule.sosial.rule.Rule05bJournalnummerDubletter

object KvalifiseringValidator {

    @JvmStatic
    fun validateKvalifisering(
        arguments: KotlinArguments
    ): List<ValidationReportEntry> = validateKvalifiseringInternal(
        kostraRecords = arguments
            .getInputContentAsStringList()
            .withIndex()
            .map { (index, recordString) ->
                recordString.toKostraRecord(
                    index = index + 1,
                    fieldDefinitions = fieldDefinitions
                )
            },
        arguments = arguments
    )

    internal fun validateKvalifiseringInternal(
        kostraRecords: List<KostraRecord>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry> {
        val reportEntries = kostraRecords.asSequence().map { record ->
            kvalifiseringRules
                .mapNotNull { it.validate(record, arguments) }
                .flatten()
                .map { reportEntry ->
                    reportEntry.copy(
                        caseworker = record.getFieldAsString(SAKSBEHANDLER_COL_NAME),
                        journalId = record.getFieldAsString(PERSON_JOURNALNR_COL_NAME),
                        individId = record.getFieldAsString(PERSON_FODSELSNR_COL_NAME),
                        lineNumbers = listOf(record.index)
                    )
                }
        }.filter { it.any() }.flatten().toList()

        val duplicateValidationErrors = setOf(Rule05aFoedselsnummerDubletter(), Rule05bJournalnummerDubletter())
            .mapNotNull { it.validate(kostraRecords, arguments) }
            .flatten()

        return reportEntries + duplicateValidationErrors
    }
}