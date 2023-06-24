package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.area.sosial.extension.addKeyOrAddValueIfKeyIsPresent
import no.ssb.kostra.area.sosial.extension.mapToValidationReportEntries
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.ValidationResult
import no.ssb.kostra.validation.rule.barnevern.AvgiverRules.avgiverRules
import no.ssb.kostra.validation.rule.barnevern.IndividRules.individRules
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.BarnevernXmlStreamHandler
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.DefaultXmlStreamHandler
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.XmlElementHandlers.avgiverXmlElementHandler
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.XmlElementHandlers.individElementHandler

object BarnevernValidator {

    @JvmStatic
    fun validateBarnevern(arguments: KotlinArguments) = validateBarnevern(
        arguments = arguments,
        streamHandler = DefaultXmlStreamHandler(
            avgiverXmlElementHandler,
            individElementHandler
        )
    )

    fun validateBarnevern(
        arguments: KotlinArguments,
        streamHandler: BarnevernXmlStreamHandler
    ): ValidationResult {
        var seenAvgivere = 0
        var seenIndivider = 0

        arguments.inputFileStream.use { fileStream ->
            val reportEntries = mutableListOf<ValidationReportEntry>()

            val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
            val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

            try {
                reportEntries.addAll(
                    streamHandler.handleStream(
                        fileStream = fileStream!!,
                        arguments = arguments,
                        { _ -> seenAvgivere++ }
                    ) { kostraIndivid: KostraIndividType ->
                        seenIndivider++

                        kostraIndivid.fodselsnummer?.also {
                            seenFodselsnummer.addKeyOrAddValueIfKeyIsPresent(
                                kostraIndivid.fodselsnummer,
                                kostraIndivid.journalnummer
                            )
                            seenJournalNummer.addKeyOrAddValueIfKeyIsPresent(
                                kostraIndivid.journalnummer,
                                kostraIndivid.fodselsnummer
                            )
                        }
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
