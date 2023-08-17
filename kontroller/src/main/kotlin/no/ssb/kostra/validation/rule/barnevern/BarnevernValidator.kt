package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.ValidationResult
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.AvgiverRules.avgiverRules
import no.ssb.kostra.validation.rule.barnevern.IndividRules.individRules
import no.ssb.kostra.validation.rule.barnevern.extension.addKeyOrAddValueIfKeyIsPresent
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.BarnevernXmlStreamHandler
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.DefaultXmlStreamHandler
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.FixedValidationErrors.individMissingError
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.FixedValidationErrors.singleAvgiverError
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.FixedValidationErrors.xmlFileError
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
        var avgiverCount = 0
        var individCount = 0

        arguments.inputFileStream.use { fileStream ->
            val reportEntries = mutableListOf<ValidationReportEntry>()

            val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
            val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

            try {
                reportEntries.addAll(
                    streamHandler.handleStream(
                        fileStream = fileStream!!,
                        arguments = arguments,
                        { _ -> avgiverCount++ }
                    ) { kostraIndivid: KostraIndividType ->
                        individCount++

                        kostraIndivid.fodselsnummer?.also { fodselsnummer ->
                            seenFodselsnummer.addKeyOrAddValueIfKeyIsPresent(
                                fodselsnummer,
                                kostraIndivid.journalnummer
                            )
                            seenJournalNummer.addKeyOrAddValueIfKeyIsPresent(
                                kostraIndivid.journalnummer,
                                fodselsnummer
                            )
                        }
                    }
                )

                if (avgiverCount != 1) reportEntries.add(singleAvgiverError(avgiverCount))
                if (individCount < 1) reportEntries.add(individMissingError)

                reportEntries.addAll(seenFodselsnummer
                    .filterValues { it.size > 1 }
                    .flatMap { entry ->
                        entry.value.map { currentJournalId ->
                            ValidationReportEntry(
                                severity = Severity.ERROR,
                                ruleName = IndividRuleId.INDIVID_04.title,
                                journalId = currentJournalId,
                                messageText = "Fødselsnummeret i journalnummer $currentJournalId fins også i " +
                                        "journalene ${
                                            entry.value.filter { it != currentJournalId }.joinToString(", ")
                                        }."
                            )
                        }
                    }
                )

                reportEntries.addAll(seenJournalNummer
                    .filterValues { it.size > 1 }
                    .map { entry ->
                        ValidationReportEntry(
                            severity = Severity.ERROR,
                            ruleName = IndividRuleId.INDIVID_05.title,
                            messageText = "Journalnummer ${entry.key} forekommer ${entry.value.size} ganger."
                        )
                    }
                )
            } catch (thrown: Throwable) {
                reportEntries.add(xmlFileError(thrown.message))
            }

            return ValidationResult(
                reportEntries = reportEntries,
                numberOfControls = avgiverCount * avgiverRules.size + individCount * individRules.size
            )
        }
    }
}
