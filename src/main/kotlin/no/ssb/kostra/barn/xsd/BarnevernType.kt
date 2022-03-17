package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BarnevernType", propOrder = ["avgiver", "individ"])
data class BarnevernType(

    @field:XmlElement(name = "Avgiver", required = true)
    var avgiver: AvgiverType? = null,

    @field:XmlElement(name = "Individ", required = true)
    var individ: List<IndividType>? = null
) {
    companion object {
        fun createRandomBarnevernType(year: Int): BarnevernType =
            BarnevernType(
                avgiver = AvgiverType.createRandomAvgiverType(year),
                individ = listOf(IndividType.createRandomIndividType("~ĩd~", year))
            )
    }
}
