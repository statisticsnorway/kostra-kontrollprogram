package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpphevelseType", propOrder = ["presisering"])
data class OpphevelseType(

    @field:XmlElement(name = "Presisering")
    var presisering: String? = null,

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null
) {
    companion object {
        fun createRandomOpphevelseType(): OpphevelseType =
            OpphevelseType(
                kode = (1..4).random().toString()
            ).apply {
                presisering = if (kode == "4") {
                    "~presisering~"
                } else {
                    null
                }
            }
    }
}
