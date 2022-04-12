package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvgiverType")
data class KostraAvgiverType(

    @field:XmlAttribute(name = "Organisasjonsnummer", required = true)
    val organisasjonsnummer: String,

    @field:XmlAttribute(name = "Versjon", required = true)
    val versjon: Int,

    @field:XmlAttribute(name = "Kommunenummer", required = true)
    val kommunenummer: String,

    @field:XmlAttribute(name = "Kommunenavn", required = true)
    val kommunenavn: String
)