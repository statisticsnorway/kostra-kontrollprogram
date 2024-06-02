package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barnevern.KostraValidationUtils
import no.ssb.kostra.barnevern.convert.KostraBarnevernConverter
import no.ssb.kostra.barnevern.convert.KostraBarnevernConverter.XML_MAPPER
import no.ssb.kostra.barnevern.xsd.KostraAvgiverType
import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.validation.rule.barnevern.AvgiverRules
import no.ssb.kostra.validation.rule.barnevern.IndividRules
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.FixedValidationErrors.avgiverFileError
import no.ssb.kostra.validation.rule.barnevern.xmlhandling.FixedValidationErrors.individFileError
import java.io.StringReader

object XmlElementHandlers {

    val individElementHandler = XmlElementHandler { xmlStreamReader, arguments ->
        XML_MAPPER.readValue(
            xmlStreamReader,
            KostraIndividType::class.java
        ).let { individType ->
            val (valid, errorMessage) = KostraValidationUtils.validate(
                xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(individType)),
                xsdResource = KostraValidationUtils.INDIVID_XSD_RESOURCE
            )
            if (valid)
                IndividRules.individRules
                    .mapNotNull { it.validate(individType, arguments) }
                    .flatten()
                    .map { reportEntry ->
                        reportEntry.copy(
                            caseworker = individType.saksbehandler,
                            journalId = individType.journalnummer,
                            individId = individType.id
                        )
                    } to individType
            else {
                listOf(individFileError(errorMessage)) to null
            }
        }
    }

    val avgiverXmlElementHandler = XmlElementHandler { xmlStreamReader, arguments ->
        XML_MAPPER.readValue(
            xmlStreamReader,
            KostraAvgiverType::class.java
        ).let { avgiverType ->
            val (valid, errorMessage) = KostraValidationUtils.validate(
                xmlReader = StringReader(KostraBarnevernConverter.marshallInstance(avgiverType)),
                xsdResource = KostraValidationUtils.AVGIVER_XSD_RESOURCE
            )
            if (valid)
                AvgiverRules.avgiverRules
                    .mapNotNull { it.validate(avgiverType, arguments) }
                    .flatten() to avgiverType
            else listOf(avgiverFileError(errorMessage)) to null
        }
    }
}