package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArsakFraType")

data class ArsakFraType(

    @field:XmlAttribute(name = "Kode", required = true)
    var kode: String? = null,

    @field:XmlAttribute(name = "Presisering")
    var presisering: String? = null
) {
    companion object {

        fun createRandomArsakFraType(): ArsakFraType =
            ArsakFraType(
                kode = "1.1.1" // TODO: Kodeliste
            )
    }
}
