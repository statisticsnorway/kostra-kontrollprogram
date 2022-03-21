package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.*

@XmlRootElement(name = "Barnevern")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BarnevernType", propOrder = ["avgiver", "individ"])
data class KostraBarnevernType(

    @field:XmlElement(name = "Avgiver", required = true)
    var avgiver: KostraAvgiverType? = null,

    @field:XmlElement(name = "Individ", required = true)
    var individ: List<KostraIndividType>? = null
) {
    companion object {
        fun createRandomBarnevernType(year: Int): KostraBarnevernType =
            KostraBarnevernType(
                avgiver = KostraAvgiverType.createRandomAvgiverType(year),
                individ = listOf(KostraIndividType.createRandomIndividType("~ĩd~", year))
            )
    }
}
