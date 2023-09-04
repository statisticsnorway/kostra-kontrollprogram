package no.ssb.kostra.barnevern.xsd

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import jakarta.xml.bind.annotation.*

@XmlRootElement(name = "Barnevern")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BarnevernType")
data class KostraBarnevernType(

    @field:XmlElement(name = "Avgiver", required = true)
    val avgiver: KostraAvgiverType,

    @field:JacksonXmlProperty(localName = "Individ")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val individ: MutableList<KostraIndividType> = mutableListOf()
)
