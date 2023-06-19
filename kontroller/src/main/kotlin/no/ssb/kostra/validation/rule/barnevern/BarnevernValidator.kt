package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.area.sosial.extension.addKeyOrAddValueIfKeyIsPresent
import no.ssb.kostra.area.sosial.extension.mapToValidationReportEntries
import no.ssb.kostra.barn.KostraValidationUtils.AVGIVER_XSD_RESOURCE
import no.ssb.kostra.barn.KostraValidationUtils.INDIVID_XSD_RESOURCE
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
import javax.xml.stream.XMLStreamReader

object BarnevernValidator {

    private const val AVGIVER_XML_TAG = "Avgiver"
    private const val INDIVID_XML_TAG = "Individ"

    private val avgiverRules = AvgiverRules()
    private val individRules = IndividRules()

    @JvmStatic
    fun validateBarnevern(arguments: KotlinArguments): List<ValidationReportEntry> {

        arguments.inputFileStream.use { fileStream ->
            val reportEntries = mutableListOf<ValidationReportEntry>()

            val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
            val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

            try {
                val xmlStreamReader: XMLStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(fileStream)

                while (xmlStreamReader.hasNext()) {
                    xmlStreamReader.next()

                    if (xmlStreamReader.eventType != XMLStreamConstants.START_ELEMENT) continue

                    when (xmlStreamReader.localName) {
                        /** process  avgiver */
                        AVGIVER_XML_TAG -> {
                            reportEntries.addAll(processAvgiver(xmlStreamReader, arguments))
                        }

                        /** process individ */
                        INDIVID_XML_TAG -> {
                            reportEntries.addAll(
                                processIndivid(
                                    xmlStreamReader,
                                    arguments
                                ) { journalnummer: String, fodselsnummer: String ->
                                    seenFodselsnummer.addKeyOrAddValueIfKeyIsPresent(fodselsnummer, journalnummer)
                                    seenJournalNummer.addKeyOrAddValueIfKeyIsPresent(journalnummer, fodselsnummer)
                                }
                            )
                        }
                    }
                }

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

            return reportEntries
        }
    }

    private fun processAvgiver(
        xmlStreamReader: XMLStreamReader,
        arguments: KotlinArguments
    ) = mutableListOf<ValidationReportEntry>().apply {
        try {
            val avgiverType = KostraBarnevernConverter.XML_MAPPER.readValue(
                xmlStreamReader,
                KostraAvgiverType::class.java
            )

            if (validate(
                    xmlReader = StringReader(marshallInstance(avgiverType)),
                    xsdResource = AVGIVER_XSD_RESOURCE
                )
            ) {
                addAll(
                    avgiverRules.validate(
                        context = avgiverType,
                        arguments = arguments
                    )
                )
            } else add(avgiverFileError)
        } catch (thrown: Throwable) {
            add(avgiverFileError)
        }
    }

    private fun processIndivid(
        xmlStreamReader: XMLStreamReader,
        arguments: KotlinArguments,
        fodselsnummerAndJournalIdFunc: (String, String) -> Unit
    ) = mutableListOf<ValidationReportEntry>().apply {
        try {
            val individType = KostraBarnevernConverter.XML_MAPPER.readValue(
                xmlStreamReader, KostraIndividType::class.java
            )

            if (validate(
                    xmlReader = StringReader(marshallInstance(individType)),
                    xsdResource = INDIVID_XSD_RESOURCE
                )
            ) {
                addAll(
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

                if (!individType.fodselsnummer.isNullOrBlank()) {
                    fodselsnummerAndJournalIdFunc(
                        individType.journalnummer,
                        individType.fodselsnummer
                    )
                }
            } else add(individFileError)
        } catch (thrown: Throwable) {
            add(individFileError)
        }
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
