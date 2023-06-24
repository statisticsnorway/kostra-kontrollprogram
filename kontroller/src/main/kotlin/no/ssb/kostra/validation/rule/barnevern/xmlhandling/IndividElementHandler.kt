package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barn.KostraValidationUtils
import no.ssb.kostra.barn.KostraValidationUtils.INDIVID_XSD_RESOURCE
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.XML_MAPPER
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.marshallInstance
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.IndividRules.individRules
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.DefaultStreamHandler.Companion.individFileError
import java.io.StringReader
import javax.xml.stream.XMLStreamReader

object IndividElementHandler : XmlElementHandler<KostraIndividType> {
    override fun handleXmlElement(
        xmlStreamReader: XMLStreamReader,
        arguments: KotlinArguments
    ): Pair<List<ValidationReportEntry>, KostraIndividType?> = XML_MAPPER.readValue(
        xmlStreamReader,
        KostraIndividType::class.java
    ).let { individType ->
        if (KostraValidationUtils.validate(
                xmlReader = StringReader(marshallInstance(individType)),
                xsdResource = INDIVID_XSD_RESOURCE
            )
        ) individRules.mapNotNull { it.validate(individType, arguments) }.flatten().map { reportEntry ->
            reportEntry.copy(
                caseworker = individType.saksbehandler,
                journalId = individType.journalnummer,
                individId = individType.id
            )
        } to individType
        else listOf(individFileError) to null
    }
}