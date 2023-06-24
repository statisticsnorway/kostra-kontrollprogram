package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import javax.xml.stream.XMLStreamReader

fun interface XmlElementHandler<out T : Any> {
    fun handleXmlElement(
        xmlStreamReader: XMLStreamReader,
        arguments: KotlinArguments
    ): Pair<List<ValidationReportEntry>, T?>
}