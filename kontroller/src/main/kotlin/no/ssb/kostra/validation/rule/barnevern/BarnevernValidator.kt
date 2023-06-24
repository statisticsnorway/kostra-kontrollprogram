package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.area.sosial.extension.addKeyOrAddValueIfKeyIsPresent
import no.ssb.kostra.area.sosial.extension.mapToValidationReportEntries
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.ValidationResult
import no.ssb.kostra.validation.rule.barnevern.AvgiverRules.avgiverRules
import no.ssb.kostra.validation.rule.barnevern.IndividRules.individRules

object BarnevernValidator {

    @JvmStatic
    fun validateBarnevern(arguments: KotlinArguments) = validateBarnevern(arguments, DefaultStreamHandler)

    fun validateBarnevern(
        arguments: KotlinArguments,
        streamHandler: BarnevernStreamHandler
    ): ValidationResult {

        arguments.inputFileStream.use { fileStream ->
            val reportEntries = mutableListOf<ValidationReportEntry>()

            val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
            val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

            var seenAvgivere = 0
            var seenIndivider = 0

            try {
                reportEntries.addAll(
                    streamHandler.handleStream(
                        fileStream = fileStream!!,
                        arguments = arguments,
                        incrementAvgiverCount = { seenAvgivere++ },
                        incrementIndividCount = { seenIndivider++ }
                    ) { journalnummer: String, fodselsnummer: String ->
                        seenFodselsnummer.addKeyOrAddValueIfKeyIsPresent(fodselsnummer, journalnummer)
                        seenJournalNummer.addKeyOrAddValueIfKeyIsPresent(journalnummer, fodselsnummer)
                    }
                )

                if (seenAvgivere != 1) reportEntries.add(singleAvgiverError(seenAvgivere))
                if (seenIndivider < 1) reportEntries.add(individMissingError)

                reportEntries.addAll(
                    seenFodselsnummer.mapToValidationReportEntries(
                        IndividRuleId.INDIVID_04.title,
                        messageTemplateFunc = { key, values -> "Dublett for fødselsnummer ($key) for journalnummer ($values)" }
                    )
                )

                reportEntries.addAll(
                    seenJournalNummer.mapToValidationReportEntries(
                        IndividRuleId.INDIVID_05.title,
                        messageTemplateFunc = { key, values -> "Dublett for journalnummer ($key) for fødselsnummer ($values)" }
                    )
                )
            } catch (thrown: Throwable) {
                reportEntries.add(
                    ValidationReportEntry(
                        severity = Severity.ERROR,
                        messageText = "Klarer ikke å lese fil. Får feilmeldingen: ${thrown.message}"
                    )
                )
            }

            return ValidationResult(
                reportEntries = reportEntries,
                numberOfControls = seenAvgivere * avgiverRules.size + seenIndivider * individRules.size
            )
        }
    }

    private fun singleAvgiverError(found: Int) = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = AvgiverRuleId.AVGIVER_00.title,
        messageText = "Antall avgivere skal være 1, fant $found"
    )

    private val individMissingError = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = IndividRuleId.INDIVID_00.title,
        messageText = "Filen mangler individer"
    )
}
