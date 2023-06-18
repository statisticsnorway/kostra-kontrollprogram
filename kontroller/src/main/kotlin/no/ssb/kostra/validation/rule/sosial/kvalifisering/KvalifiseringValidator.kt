package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.toKostraRecord
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.sosial.SosialCommonRules.sosialRules
import no.ssb.kostra.validation.rule.sosial.SosialRuleId
import no.ssb.kostra.validation.rule.sosial.extension.addKeyOrAddValueIfKeyIsPresent
import no.ssb.kostra.validation.rule.sosial.extension.mapToValidationReportEntries
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringRules.kvalifiseringRules

object KvalifiseringValidator {

    @JvmStatic
    fun validateKvalifisering(arguments: KotlinArguments): List<ValidationReportEntry> {

        val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
        val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

        val reportEntries = arguments
            .getInputContentAsStringList()
            .asSequence()
            .withIndex()
            .map { (index, recordString) ->
                recordString.toKostraRecord(
                    index = index + 1,
                    fieldDefinitions = fieldDefinitions
                )
            }.onEach { record ->
                val fodselsnummer = record.getFieldAsString(PERSON_FODSELSNR_COL_NAME)
                val journalnummer = record.getFieldAsString(PERSON_JOURNALNR_COL_NAME)

                seenFodselsnummer.addKeyOrAddValueIfKeyIsPresent(fodselsnummer, journalnummer)
                seenJournalNummer.addKeyOrAddValueIfKeyIsPresent(journalnummer, fodselsnummer)
            }
            .map { record ->
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

        return reportEntries + seenFodselsnummer.mapToValidationReportEntries(
            SosialRuleId.FODSELSNUMMER_DUBLETTER_05A.title,
            "Dublett for fødselsnummer for journalnummer"
        ) + seenJournalNummer.mapToValidationReportEntries(
            SosialRuleId.JOURNALNUMMER_DUBLETTER_05B.title,
            "Dublett for journalnummer for fødselsnummer"
        )
    }
}