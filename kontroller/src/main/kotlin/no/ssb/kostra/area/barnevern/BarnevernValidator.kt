package no.ssb.kostra.area.barnevern

import no.ssb.kostra.area.barnevern.avgiverrule.AvgiverRules
import no.ssb.kostra.area.barnevern.individrule.IndividRuleId
import no.ssb.kostra.area.barnevern.individrule.IndividRules
import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.Arguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import java.io.Reader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

const val KOSTRA_IS_CLOSED_TRUE = "1"
const val KOSTRA_IS_CLOSED_FALSE = "2"

class BarnevernValidator {
    private val avgiverRules = AvgiverRules()
    private val individRules = IndividRules()

    fun validate(xmlReader: Reader, arguments: Arguments) {
        val xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(xmlReader)

        val validationErrors = mutableListOf<ValidationReportEntry>()

        var individType: KostraIndividType?

        val seenFodselsnummer = mutableMapOf<String, MutableList<String>>()
        val seenJournalNummer = mutableMapOf<String, MutableList<String>>()

        while (xmlStreamReader.hasNext()) {
            xmlStreamReader.next()

            if (xmlStreamReader.eventType != XMLStreamConstants.START_ELEMENT) continue

            when (xmlStreamReader.localName) {
                /** capture avgiver */
                AVGIVER_XML_TAG -> {
                    validationErrors.addAll(
                        avgiverRules.validate(
                            KostraBarnevernConverter.XML_MAPPER.readValue(
                                xmlStreamReader, KostraAvgiverType::class.java
                            )
                        )
                    )
                }

                /** process current individual */
                INDIVID_XML_TAG -> {
                    individType = KostraBarnevernConverter.XML_MAPPER.readValue(
                        xmlStreamReader, KostraIndividType::class.java
                    )
                    validationErrors.addAll(individRules.validate(individType, arguments))
                    when (val fodselsnummer = individType.fodselsnummer) {
                        null -> {}
                        else -> {
                            if (seenFodselsnummer.containsKey(fodselsnummer)) {
                                seenFodselsnummer[fodselsnummer]!!.add(individType.journalnummer)
                            } else seenFodselsnummer[fodselsnummer] = mutableListOf()

                            if (seenJournalNummer.containsKey(individType.journalnummer)) {
                                seenJournalNummer[individType.journalnummer]!!.add(fodselsnummer)
                            } else seenJournalNummer[individType.journalnummer] = mutableListOf()
                        }
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
