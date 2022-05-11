package no.ssb.kostra.barn.xsd

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import java.time.LocalDate
import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividType", propOrder = ["melding", "plan", "tiltak", "flytting"])
data class KostraIndividType(

    @field:XmlAttribute(name = "Id", required = true)
    val id: String,

    @field:XmlAttribute(name = "Journalnummer", required = true)
    val journalnummer: String,

    @field:XmlAttribute(name = "StartDato", required = true)
    @field:XmlSchemaType(name = "date")
    val startDato: LocalDate,

    @field:XmlAttribute(name = "Saksbehandler", required = true)
    val saksbehandler: String,

    @field:XmlAttribute(name = "Avslutta3112", required = true)
    val avslutta3112: String, // 1 = Ja, 2 = Nei

    @field:XmlAttribute(name = "SluttDato")
    @field:XmlSchemaType(name = "date")
    val sluttDato: LocalDate? = null,

    @field:XmlAttribute(name = "Fodselsnummer")
    val fodselsnummer: String? = null,

    @field:XmlAttribute(name = "DUFnummer")
    val duFnummer: String? = null,

    @field:XmlAttribute(name = "Bydelsnummer")
    val bydelsnummer: String? = null,

    @field:XmlAttribute(name = "Bydelsnavn")
    val bydelsnavn: String? = null,

    @field:XmlAttribute(name = "Distriktsnummer")
    val distriktsnummer: String? = null,

    @field:JacksonXmlProperty(localName = "Melding")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val melding: List<KostraMeldingType> = listOf(),

    @field:JacksonXmlProperty(localName = "Plan")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val plan: List<KostraPlanType> = listOf(),

    @field:JacksonXmlProperty(localName = "Tiltak")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val tiltak: List<KostraTiltakType> = listOf(),

    @field:JacksonXmlProperty(localName = "Flytting")
    @field:JacksonXmlElementWrapper(useWrapping = false)
    val flytting: List<KostraFlyttingType> = listOf()
)
