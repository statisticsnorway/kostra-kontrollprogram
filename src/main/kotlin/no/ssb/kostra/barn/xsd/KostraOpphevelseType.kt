package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpphevelseType", propOrder = ["presisering"])
data class KostraOpphevelseType(

    @field:XmlElement(name = "Presisering")
    var presisering: String? = null,

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null
)