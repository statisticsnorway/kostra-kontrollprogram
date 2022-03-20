package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaksinnholdType", propOrder = ["presisering"])
data class KostraSaksinnholdType(

    @field:XmlElement(name = "Presisering")
    var presisering: String? = null,

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null
) {
    companion object {
        fun createRandomSaksinnholdType(): KostraSaksinnholdType =
            KostraSaksinnholdType(kode = (1..27).random().toString())
    }
}
