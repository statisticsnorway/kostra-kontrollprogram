package no.ssb.kostra.barnevern.xsd

import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaksinnholdType", propOrder = ["kode", "presisering"])
data class KostraSaksinnholdType(

    @field:XmlAttribute(name = "Kode", required = true)
    val kode: String = "0",
    /** workaround for data that is missing this field due to previous bug in XSD */

    @field:XmlElement(name = "Presisering")
    val presisering: String? = null
)