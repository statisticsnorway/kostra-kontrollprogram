package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LovhjemmelType")
data class KostraLovhjemmelType(

    @field:XmlAttribute(name = "Lov", required = true)
    var lov: String? = null,

    @field:XmlAttribute(name = "Kapittel", required = true)
    var kapittel: String? = null,

    @field:XmlAttribute(name = "Paragraf", required = true)
    var paragraf: String? = null,

    @field:XmlAttribute(name = "Ledd", required = true)
    var ledd: String? = null,

    @field:XmlAttribute(name = "Punktum")
    var punktum: String? = null
) {
    companion object {
        fun createRandomLovhjemmelType(): KostraLovhjemmelType =
            KostraLovhjemmelType(
                lov = "~lov~",
                kapittel = "~kapittel~",
                paragraf = "~paragraf~",
                ledd = "~ledd~"
            )
    }
}
