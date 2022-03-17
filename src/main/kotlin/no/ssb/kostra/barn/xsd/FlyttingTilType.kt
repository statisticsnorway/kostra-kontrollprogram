package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlyttingTilType")
data class FlyttingTilType(

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null,

    @field:XmlAttribute(name = "Presisering")
    var presisering: String? = null
) {
    companion object {
        fun createRandomFlyttingTilType(): FlyttingTilType =
            FlyttingTilType(
                kode = (1..9).random().toString()
            ).apply {
                presisering = if (kode == "8") "~presisering~" else null
            }
    }
}
