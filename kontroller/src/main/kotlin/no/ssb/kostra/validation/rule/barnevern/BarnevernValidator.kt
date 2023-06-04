package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.barn.KostraValidationUtils.KOSTRA_AVGIVER_XSD_RESOURCE
import no.ssb.kostra.barn.KostraValidationUtils.KOSTRA_INDIVID_XSD_RESOURCE
import no.ssb.kostra.barn.KostraValidationUtils.validate
import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.AvgiverRuleId
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.AvgiverRules
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRules
import java.io.StringReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

object BarnevernValidator {

    private const val AVGIVER_XML_TAG = "Avgiver"
    private const val INDIVID_XML_TAG = "Individ"

    private val avgiverRules = AvgiverRules()
    private val individRules = IndividRules()

    @JvmStatic
    fun validateBarnevern(arguments: KotlinArguments): List<ValidationReportEntry> {

        arguments.inputFileStream.use { fileStream ->
            val validationErrors = mutableListOf<ValidationReportEntry>()
            val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
            val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

            try {
                val xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fileStream)

                while (xmlStreamReader.hasNext()) {
                    xmlStreamReader.next()

                    if (xmlStreamReader.eventType != XMLStreamConstants.START_ELEMENT) continue

                    when (xmlStreamReader.localName) {
                        /** capture avgiver */
                        AVGIVER_XML_TAG -> {

                            try {
                                val avgiverType = KostraBarnevernConverter.XML_MAPPER.readValue(
                                    xmlStreamReader,
                                    KostraAvgiverType::class.java
                                )

                                if (validate(
                                        xmlReader = StringReader(marshallInstance(avgiverType)),
                                        xsdResourceName = KOSTRA_AVGIVER_XSD_RESOURCE
                                    )
                                ) {
                                    validationErrors.addAll(
                                        avgiverRules.validate(
                                            context = avgiverType,
                                            arguments = arguments
                                        )
                                    )
                                } else validationErrors.add(avgiverFileError)
                            } catch (thrown: Throwable) {
                                validationErrors.add(avgiverFileError)
                            }
                        }

                        /** process current individual */
                        INDIVID_XML_TAG -> {

                            try {
                                val individType = KostraBarnevernConverter.XML_MAPPER.readValue(
                                    xmlStreamReader, KostraIndividType::class.java
                                )

                                if (validate(
                                        xmlReader = StringReader(marshallInstance(individType)),
                                        xsdResourceName = KOSTRA_INDIVID_XSD_RESOURCE
                                    )
                                ) {
                                    validationErrors.addAll(
                                        individRules.validate(
                                            context = individType,
                                            arguments = arguments
                                        ).map { reportEntry ->
                                            reportEntry.copy(
                                                caseworker = individType.saksbehandler,
                                                journalId = individType.journalnummer,
                                                individId = individType.id
                                            )
                                        }
                                    )

                                    individType.fodselsnummer
                                        .takeUnless { fnr -> fnr.isNullOrBlank() }
                                        ?.also { fnr ->
                                            if (seenFodselsnummer.containsKey(fnr)) {
                                                seenFodselsnummer[fnr]!!.add(individType.journalnummer)
                                            } else seenFodselsnummer[fnr] = mutableListOf()

                                            if (seenJournalNummer.containsKey(individType.journalnummer)) {
                                                seenJournalNummer[individType.journalnummer]!!.add(fnr)
                                            } else seenJournalNummer[individType.journalnummer] = mutableListOf()
                                        }
                                } else validationErrors.add(individFileError)
                            } catch (thrown: Throwable) {
                                validationErrors.add(individFileError)
                            }
                        }
                    }
                }

                validationErrors.addAll(
                    seenFodselsnummer.mapToValidationReportEntries(
                        IndividRuleId.INDIVID_04.title,
                        "Dublett for fødselsnummer for journalnummer"
                    )
                )

                validationErrors.addAll(
                    seenJournalNummer.mapToValidationReportEntries(
                        IndividRuleId.INDIVID_05.title,
                        "Dublett for journalnummer for fødselsnummer"
                    )
                )
            } catch (thrown: Throwable) {
                validationErrors.add(
                    ValidationReportEntry(
                        severity = Severity.ERROR,
                        messageText = "Klarer ikke å lese fil. Får feilmeldingen: ${thrown.message}"
                    )
                )
            }

            return validationErrors
        }
    }

    private fun Map<String, Collection<String>>.mapToValidationReportEntries(
        ruleName: String,
        messageText: String,
    ) = this.filterValues { it.any() }.map { entry ->
        ValidationReportEntry(
            severity = Severity.ERROR,
            ruleName = ruleName,
            messageText = "$messageText (${entry.value.joinToString(", ")})"
        )
    }

    private val avgiverFileError = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = AvgiverRuleId.AVGIVER_01.title,
        messageText = "Klarer ikke å validere Avgiver mot filspesifikasjon"
    )

    private val individFileError = ValidationReportEntry(
        severity = Severity.ERROR,
        ruleName = IndividRuleId.INDIVID_01.title,
        messageText = "Definisjon av Individ er feil i forhold til filspesifikasjonen"
    )
}
