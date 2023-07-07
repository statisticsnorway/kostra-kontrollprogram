package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.rule.ValidationResult
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRules.duplicateRules
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRules.kvalifiseringRules

object KvalifiseringValidator {

    @JvmStatic
    fun validateKvalifisering(
        arguments: KotlinArguments
    ): ValidationResult = validateKvalifiseringInternal(
        kostraRecords = arguments
            .getInputContentAsStringList()
            .withIndex()
            .filter { (_, line) -> line.length == fieldDefinitions.last().to }
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
    ): ValidationResult {
        val reportEntries = kostraRecords.asSequence().map { record ->
            kvalifiseringRules
                .mapNotNull { it.validate(record, arguments) }
                .flatten()
                .map { reportEntry ->
                    reportEntry.copy(
                        caseworker = record[SAKSBEHANDLER_COL_NAME],
                        journalId = record[PERSON_JOURNALNR_COL_NAME],
                        individId = record[PERSON_FODSELSNR_COL_NAME],
                        lineNumbers = listOf(record.lineNumber)
                    )
                }
        }.filter { it.any() }.flatten().toList()

        val duplicateValidationErrors = duplicateRules
            .mapNotNull { it.validate(kostraRecords, arguments) }
            .flatten()

        return ValidationResult(
            reportEntries = reportEntries + duplicateValidationErrors,
            numberOfControls = kostraRecords.size * (kvalifiseringRules.size + duplicateRules.size)
        )
    }
}