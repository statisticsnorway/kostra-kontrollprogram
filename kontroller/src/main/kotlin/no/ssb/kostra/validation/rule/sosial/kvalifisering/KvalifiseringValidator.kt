package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.toKostraRecord
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.sosial.SosialCommonRules.sosialRules
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRules.kvalifiseringRules

object KvalifiseringValidator {

    @JvmStatic
    fun validateKvalifisering(arguments: KotlinArguments): List<ValidationReportEntry> = arguments
        .getInputContentAsStringList()
        .asSequence()
        .withIndex()
        .map { (index, recordString) ->
            recordString.toKostraRecord(
                index = index + 1,
                fieldDefinitions = fieldDefinitions
            )
        }.map { record ->
            (sosialRules + kvalifiseringRules)
                .mapNotNull { it.validate(record, arguments) }
                .flatten()
                .map { reportEntry ->
                    reportEntry.copy(
                        caseworker = record.getFieldAsString(SAKSBEHANDLER_COL_NAME),
                        journalId = record.getFieldAsString(PERSON_JOURNALNR_COL_NAME),
                        individId = record.getFieldAsString(PERSON_FODSELSNR_COL_NAME)
                    )
                }
        }.filter { it.any() }.flatten().toList()
}