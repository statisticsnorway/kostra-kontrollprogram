package no.ssb.kostra.barn.xsd

import java.time.LocalDate
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FlyttingType", propOrder = ["arsakFra", "flyttingTil"])
data class KostraFlyttingType(

    @field:XmlAttribute(name = "Id", required = true)
    override val id: String,

    @field:XmlAttribute(name = "SluttDato", required = true)
    @field:XmlSchemaType(name = "date")
    val sluttDato: LocalDate,

    @field:XmlElement(name = "ArsakFra", required = true)
    val arsakFra: KostraArsakFraType,

    @field:XmlElement(name = "FlyttingTil", required = true)
    val flyttingTil: KostraFlyttingTilType
) : KostraEntity