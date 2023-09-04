package no.ssb.kostra.barnevern.xsd

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArsakFraType")
data class KostraArsakFraType(

    @field:XmlAttribute(name = "Kode", required = true)
    val kode: String,

    @field:XmlAttribute(name = "Presisering")
    val presisering: String? = null
)
