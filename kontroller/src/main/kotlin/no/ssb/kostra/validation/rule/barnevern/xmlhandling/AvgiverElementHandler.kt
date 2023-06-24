package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barn.KostraValidationUtils
import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.XML_MAPPER
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.AvgiverRules.avgiverRules
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.DefaultStreamHandler.Companion.avgiverFileError
import java.io.StringReader
import javax.xml.stream.XMLStreamReader

object AvgiverElementHandler : XmlElementHandler<KostraAvgiverType> {
    override fun handleXmlElement(
        xmlStreamReader: XMLStreamReader,
        arguments: KotlinArguments,
    ): Pair<List<ValidationReportEntry>, KostraAvgiverType?> = XML_MAPPER.readValue(
        xmlStreamReader,
        KostraAvgiverType::class.java
    ).let { avgiverType ->
        if (KostraValidationUtils.validate(
                xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(avgiverType)),
                xsdResource = KostraValidationUtils.AVGIVER_XSD_RESOURCE
            )
        ) avgiverRules.mapNotNull { it.validate(avgiverType, arguments) }.flatten() to avgiverType
        else listOf(avgiverFileError) to null
    }
}