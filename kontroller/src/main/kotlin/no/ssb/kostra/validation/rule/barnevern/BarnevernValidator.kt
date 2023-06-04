package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.AvgiverRuleId
import no.ssb.kostra.validation.rule.barnevern.avgiverrule.AvgiverRules
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRuleId
import no.ssb.kostra.validation.rule.barnevern.individrule.IndividRules
import java.io.Reader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

class BarnevernValidator {
    private val avgiverRules = AvgiverRules()
    private val individRules = IndividRules()

    fun validate(xmlReader: Reader, arguments: Arguments) {
        val xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(xmlReader)

        val validationErrors = mutableListOf<ValidationReportEntry>()

        val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
        val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

        while (xmlStreamReader.hasNext()) {
            xmlStreamReader.next()

            if (xmlStreamReader.eventType != XMLStreamConstants.START_ELEMENT) continue

            when (xmlStreamReader.localName) {
                /** capture avgiver */
                AVGIVER_XML_TAG -> {

                    try {
                        val avgiverType = KostraBarnevernConverter.XML_MAPPER.readValue(
                            xmlStreamReader, KostraAvgiverType::class.java
                        )

                        validationErrors.addAll(
                            avgiverRules.validate(
                                context = avgiverType,
                                arguments = arguments
                            )
                        )
                    } catch (thrown: Throwable) {
                        validationErrors.add(
                            ValidationReportEntry(
                                severity = Severity.FATAL,
                                ruleId = AvgiverRuleId.AVGIVER_01.title,
                                messageText = "Klarer ikke å validere Avgiver mot filspesifikasjon"
                            )
                        )
                    }
                }

                /** process current individual */
                INDIVID_XML_TAG -> {
                    try {
                        val individType = KostraBarnevernConverter.XML_MAPPER.readValue(
                            xmlStreamReader, KostraIndividType::class.java
                        )

                        validationErrors.addAll(
                            individRules.validate(
                                context = individType,
                                arguments = arguments
                            )
                        )

                        val fodselsnummer = individType.fodselsnummer
                        if (fodselsnummer != null) {
                            if (seenFodselsnummer.containsKey(fodselsnummer)) {
                                seenFodselsnummer[fodselsnummer]!!.add(individType.journalnummer)
                            } else seenFodselsnummer[fodselsnummer] = mutableListOf()

                            if (seenJournalNummer.containsKey(individType.journalnummer)) {
                                seenJournalNummer[individType.journalnummer]!!.add(fodselsnummer)
                            } else seenJournalNummer[individType.journalnummer] = mutableListOf()
                        }
                    } catch (thrown: Throwable) {
                        validationErrors.add(
                            ValidationReportEntry(
                                severity = Severity.FATAL,
                                ruleId = IndividRuleId.INDIVID_01.title,
                                messageText = "Definisjon av Individ er feil i forhold til filspesifikasjonen"
                            )
                        )
                    }
                }
            }
        }

        seenFodselsnummer.entries
            .filter { it.value.any() }
            .map {
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleId = IndividRuleId.INDIVID_04.title,
                    messageText = "Dublett for fødselsnummer for journalnummer (${it.value.joinToString(", ")})"
                )
            }

        seenJournalNummer.entries
            .filter { it.value.any() }
            .map {
                ValidationReportEntry(
                    severity = Severity.ERROR,
                    ruleId = IndividRuleId.INDIVID_05.title,
                    messageText = "Dublett for journalnummer for fødselsnummer (${it.value.joinToString(", ")})"
                )
            }
    }

    companion object {
        const val AVGIVER_XML_TAG = "Avgiver"
        const val INDIVID_XML_TAG = "Individ"
    }
}
