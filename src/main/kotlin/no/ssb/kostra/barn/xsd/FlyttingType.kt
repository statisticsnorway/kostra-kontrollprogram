package no.ssb.kostra.barn.xsd

import no.ssb.kostra.barn.convert.LocalDateAdapter
import java.time.LocalDate
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlyttingType", propOrder = ["arsakFra", "flyttingTil"])
data class FlyttingType(

    @field:XmlElement(name = "ArsakFra", required = true)
    var arsakFra: ArsakFraType? = null,

    @field:XmlElement(name = "FlyttingTil", required = true)
    var flyttingTil: FlyttingTilType? = null,

    @field:XmlAttribute(name = "Id", required = true)
    var id: String? = null,

    @field:XmlAttribute(name = "SluttDato", required = true)
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    var sluttDato: LocalDate? = null
) {
    companion object {
        fun createRandomFlyttingType(id: String, year: Int): FlyttingType =
            FlyttingType(
                arsakFra = ArsakFraType.createRandomArsakFraType(),
                flyttingTil = FlyttingTilType.createRandomFlyttingTilType(),
                id = id,
                //sluttDato = DateUtils.createRandomDate(year)
            )
    }
}
