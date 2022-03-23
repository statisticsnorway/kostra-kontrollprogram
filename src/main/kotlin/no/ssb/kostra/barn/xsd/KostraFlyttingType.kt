package no.ssb.kostra.barn.xsd

import no.ssb.kostra.barn.convert.LocalDateAdapter
import java.time.LocalDate
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlyttingType", propOrder = ["arsakFra", "flyttingTil"])
data class KostraFlyttingType(

    @field:XmlElement(name = "ArsakFra", required = true)
    var arsakFra: KostraArsakFraType? = null,

    @field:XmlElement(name = "FlyttingTil", required = true)
    var flyttingTil: KostraFlyttingTilType? = null,

    @field:XmlAttribute(name = "Id", required = true)
    var id: String? = null,

    @field:XmlAttribute(name = "SluttDato", required = true)
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    var sluttDato: LocalDate? = null
)