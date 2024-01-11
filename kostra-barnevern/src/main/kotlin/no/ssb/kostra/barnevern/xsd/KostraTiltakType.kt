package no.ssb.kostra.barnevern.xsd

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import jakarta.xml.bind.annotation.*
import java.time.LocalDate


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TiltakType", propOrder = ["lovhjemmel", "jmfrLovhjemmel", "kategori", "opphevelse"])
data class KostraTiltakType(

    @field:XmlAttribute(name = "Id", required = true)
    val id: String,

    @field:XmlAttribute(name = "StartDato", required = true)
    @field:XmlSchemaType(name = "date")
    val startDato: LocalDate,

    @field:XmlAttribute(name = "SluttDato")
    @field:XmlSchemaType(name = "date")
    val sluttDato: LocalDate? = null,

    @field:XmlElement(name = "Kategori", required = true)
    val kategori: KostraKategoriType,

    @field:JacksonXmlProperty(localName = "Tiltaksgrunnlag")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val tiltaksgrunnlag: MutableList<KostraTiltaksgrunnlagType> = mutableListOf(),

    @field:XmlElement(name = "Lovhjemmel", required = true)
    val lovhjemmel: KostraLovhjemmelType,

    @field:XmlElement(name = "Opphevelse")
    val opphevelse: KostraOpphevelseType? = null,

    @field:JacksonXmlProperty(localName = "JmfrLovhjemmel")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val jmfrLovhjemmel: MutableList<KostraLovhjemmelType> = mutableListOf()
)