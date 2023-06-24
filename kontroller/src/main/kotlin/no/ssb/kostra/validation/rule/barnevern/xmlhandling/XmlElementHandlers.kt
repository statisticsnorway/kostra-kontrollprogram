package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barn.KostraValidationUtils
import no.ssb.kostra.barn.convert.KostraBarnevernConverter
import no.ssb.kostra.barn.convert.KostraBarnevernConverter.XML_MAPPER
import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.validation.rule.barnevern.AvgiverRules
import no.ssb.kostra.validation.rule.barnevern.IndividRules
import java.io.StringReader

object XmlElementHandlers {

    val individElementHandler = XmlElementHandler { xmlStreamReader, arguments ->
        XML_MAPPER.readValue(
            xmlStreamReader,
            KostraIndividType::class.java
        ).let { individType ->
            if (KostraValidationUtils.validate(
                    xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(individType)),
                    xsdResource = KostraValidationUtils.INDIVID_XSD_RESOURCE
                )
            ) IndividRules.individRules.mapNotNull { it.validate(individType, arguments) }.flatten()
                .map { reportEntry ->
                    reportEntry.copy(
                        caseworker = individType.saksbehandler,
                        journalId = individType.journalnummer,
                        individId = individType.id
                    )
                } to individType
            else listOf(DefaultXmlStreamHandler.individFileError) to null
        }
    }

    val avgiverXmlElementHandler = XmlElementHandler { xmlStreamReader, arguments ->
        XML_MAPPER.readValue(
            xmlStreamReader,
            KostraAvgiverType::class.java
        ).let { avgiverType ->
            if (KostraValidationUtils.validate(
                    xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(avgiverType)),
                    xsdResource = KostraValidationUtils.AVGIVER_XSD_RESOURCE
                )
            ) AvgiverRules.avgiverRules.mapNotNull { it.validate(avgiverType, arguments) }.flatten() to avgiverType
            else listOf(DefaultXmlStreamHandler.avgiverFileError) to null
        }
    }
}