package no.ssb.kostra.barn.xsd

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.time.LocalDate
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeldingType", propOrder = ["melder", "saksinnhold", "undersokelse"])
data class KostraMeldingType(

    @field:XmlAttribute(name = "Id", required = true)
    val id: String,

    @field:XmlAttribute(name = "StartDato", required = true)
    @field:XmlSchemaType(name = "date")
    val startDato: LocalDate,

    @field:XmlAttribute(name = "SluttDato")
    @field:XmlSchemaType(name = "date")
    val sluttDato: LocalDate? = null,

    @field:XmlAttribute(name = "Konklusjon")
    val konklusjon: String? = null,

    @field:XmlElement(name = "Undersokelse")
    val undersokelse: KostraUndersokelseType? = null,

    @field:JacksonXmlProperty(localName = "Melder")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val melder: List<KostraMelderType> = listOf(),

    @field:JacksonXmlProperty(localName = "Saksinnhold")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val saksinnhold: List<KostraSaksinnholdType> = listOf()
)