package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaksinnholdType")
data class KostraSaksinnholdType(

    @field:XmlAttribute(name = "Kode", required = true)
    val kode: String,

    @field:XmlElement(name = "Presisering")
    val presisering: String? = null,
)