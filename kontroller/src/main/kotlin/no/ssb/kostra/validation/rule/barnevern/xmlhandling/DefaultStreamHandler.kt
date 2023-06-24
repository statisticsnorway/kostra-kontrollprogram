package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.AvgiverRuleId
import no.ssb.kostra.validation.rule.barnevern.IndividRuleId
import java.io.InputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

class DefaultStreamHandler(
    private val avgiverXmlElementHandler: XmlElementHandler<KostraAvgiverType>,
    private val individXmlElementHandler: XmlElementHandler<KostraIndividType>,
    override var seenAvgivere: Int = 0,
    override var seenIndivider: Int = 0,
) : BarnevernStreamHandler {

    override fun handleStream(
        fileStream: InputStream,
        arguments: KotlinArguments,
        individCallbackFunc: (KostraIndividType) -> Unit
    ): List<ValidationReportEntry> = mutableListOf<ValidationReportEntry>().apply {
        XMLInputFactory.newInstance().createXMLStreamReader(fileStream).let { xmlStreamReader ->
            while (xmlStreamReader.hasNext()) {
                xmlStreamReader.next()

                if (xmlStreamReader.eventType != XMLStreamConstants.START_ELEMENT) continue

                when (xmlStreamReader.localName) {
                    /** process  avgiver */
                    AVGIVER_XML_TAG -> {
                        try {
                            addAll(
                                avgiverXmlElementHandler.handleXmlElement(
                                    xmlStreamReader = xmlStreamReader,
                                    arguments = arguments
                                ).first
                            )
                            seenAvgivere++
                        } catch (thrown: Throwable) {
                            add(avgiverFileError)
                        }
                    }

                    /** process individ */
                    INDIVID_XML_TAG -> {
                        try {
                            val (validationErrors, individ) = individXmlElementHandler.handleXmlElement(
                                xmlStreamReader = xmlStreamReader,
                                arguments = arguments
                            )

                            addAll(validationErrors)

                            if (individ != null) {
                                individCallbackFunc(individ)
                            }

                            seenIndivider++
                        } catch (thrown: Throwable) {
                            add(individFileError)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val AVGIVER_XML_TAG = "Avgiver"
        private const val INDIVID_XML_TAG = "Individ"

        val avgiverFileError = ValidationReportEntry(
            severity = Severity.ERROR,
            ruleName = AvgiverRuleId.AVGIVER_01.title,
            messageText = "Klarer ikke å validere Avgiver mot filspesifikasjon"
        )

        val individFileError = ValidationReportEntry(
            severity = Severity.ERROR,
            ruleName = IndividRuleId.INDIVID_01.title,
            messageText = "Definisjon av Individ er feil i forhold til filspesifikasjonen"
        )
    }
}