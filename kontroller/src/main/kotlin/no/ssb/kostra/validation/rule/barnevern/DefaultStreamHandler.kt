package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.barn.KostraValidationUtils
import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import java.io.InputStream
import java.io.StringReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamReader

object DefaultStreamHandler : BarnevernStreamHandler {

    private const val AVGIVER_XML_TAG = "Avgiver"
    private const val INDIVID_XML_TAG = "Individ"

    override fun handleStream(
        fileStream: InputStream,
        arguments: KotlinArguments,
        incrementAvgiverCount: () -> Unit,
        incrementIndividCount: () -> Unit,
        fodselsnummerAndJournalIdFunc: (String, String) -> Unit
    ): List<ValidationReportEntry> = mutableListOf<ValidationReportEntry>().apply {
        XMLInputFactory.newInstance().createXMLStreamReader(fileStream).let { xmlStreamReader ->
            while (xmlStreamReader.hasNext()) {
                xmlStreamReader.next()

                if (xmlStreamReader.eventType != XMLStreamConstants.START_ELEMENT) continue

                when (xmlStreamReader.localName) {
                    /** process  avgiver */
                    AVGIVER_XML_TAG -> {
                        incrementAvgiverCount()
                        addAll(
                            processAvgiver(
                                xmlStreamReader = xmlStreamReader,
                                arguments = arguments
                            )
                        )
                    }

                    /** process individ */
                    INDIVID_XML_TAG -> {
                        incrementIndividCount()

                        addAll(
                            processIndivid(
                                xmlStreamReader = xmlStreamReader,
                                arguments = arguments,
                                fodselsnummerAndJournalIdFunc = fodselsnummerAndJournalIdFunc
                            )
                        )
                    }
                }
            }
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

            if (KostraValidationUtils.validate(
                    xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(avgiverType)),
                    xsdResource = KostraValidationUtils.AVGIVER_XSD_RESOURCE
                )
            ) {
                addAll(AvgiverRules.avgiverRules.mapNotNull { it.validate(avgiverType, arguments) }.flatten())
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

            if (KostraValidationUtils.validate(
                    xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(individType)),
                    xsdResource = KostraValidationUtils.INDIVID_XSD_RESOURCE
                )
            ) {
                addAll(
                    IndividRules.individRules.mapNotNull { it.validate(individType, arguments) }.flatten()
                        .map { reportEntry ->
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