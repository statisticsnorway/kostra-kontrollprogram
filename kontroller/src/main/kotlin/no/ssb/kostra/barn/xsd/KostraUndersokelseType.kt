package no.ssb.kostra.barn.xsd

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.time.LocalDate
import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UndersokelseType", propOrder = ["presisering", "vedtaksgrunnlag"])
data class KostraUndersokelseType(

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

    @field:XmlElement(name = "Presisering")
    val presisering: String? = null,

    @field:JacksonXmlProperty(localName = "Vedtaksgrunnlag")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val vedtaksgrunnlag: MutableList<KostraVedtaksgrunnlagType> = mutableListOf()
)