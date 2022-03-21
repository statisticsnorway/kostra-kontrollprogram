package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MelderType", propOrder = ["presisering"])
data class KostraMelderType(

    @field:XmlElement(name = "Presisering")
    var presisering: String? = null,

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null
) {
    companion object {
        fun createRandomMelderType(): KostraMelderType =
            KostraMelderType(
                kode = (1..23).random().toString()
            ).apply {
                presisering = if (kode == "22") "~presisering~" else null
            }
    }
}
