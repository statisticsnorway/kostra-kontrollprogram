package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LovhjemmelType")
data class KostraLovhjemmelType(

    @field:XmlAttribute(name = "Lov", required = true)
    val lov: String,

    @field:XmlAttribute(name = "Kapittel", required = true)
    val kapittel: String,

    @field:XmlAttribute(name = "Paragraf", required = true)
    val paragraf: String,

    @field:XmlAttribute(name = "Bokstav")
    val bokstav: String? = null,

    @field:XmlAttribute(name = "Ledd")
    val ledd: String? = null,

    @field:XmlAttribute(name = "Punktum")
    val punktum: String? = null
)
