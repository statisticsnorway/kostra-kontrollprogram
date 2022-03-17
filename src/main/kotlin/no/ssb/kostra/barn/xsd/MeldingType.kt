package no.ssb.kostra.barn.xsd

import no.ssb.kostra.barn.convert.LocalDateAdapter
import java.time.LocalDate
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeldingType", propOrder = ["melder", "saksinnhold", "undersokelse"])
data class MeldingType(

    @field:XmlElement(name = "Melder")
    var melder: List<MelderType>? = null,

    @field:XmlElement(name = "Saksinnhold")
    var saksinnhold: List<SaksinnholdType>? = null,

    @field:XmlElement(name = "Undersokelse")
    var undersokelse: UndersokelseType? = null,

    @field:XmlAttribute(name = "Id", required = true)
    var id: String? = null,

    @field:XmlAttribute(name = "StartDato", required = true)
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    var startDato: LocalDate? = null,

    @field:XmlAttribute(name = "SluttDato")
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    var sluttDato: LocalDate? = null,

    @field:XmlAttribute(name = "Konklusjon")
    var konklusjon: List<String>? = null
) {
    companion object {
        fun createRandomMeldingType(id: String, year: Int): MeldingType =
            MeldingType(
                id = id,
                //startDato = DateUtils.createRandomDate(year)
            )
    }
}
