package no.ssb.kostra.barn.xsd

import no.ssb.kostra.barn.convert.LocalDateAdapter
import java.time.LocalDate
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividType", propOrder = ["melding", "plan", "tiltak", "flytting"])
data class IndividType(

    @field:XmlElement(name = "Melding")
    var melding: List<MeldingType>? = null,

    @field:XmlElement(name = "Plan")
    var plan: List<PlanType>? = null,

    @field:XmlElement(name = "Tiltak")
    var tiltak: List<TiltakType>? = null,

    @field:XmlElement(name = "Flytting")
    var flytting: List<FlyttingType>? = null,

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

    @field:XmlAttribute(name = "Journalnummer", required = true)
    var journalnummer: String? = null,

    @field:XmlAttribute(name = "Fodselsnummer")
    var fodselsnummer: String? = null,

    @field:XmlAttribute(name = "DUFnummer")
    var duFnummer: String? = null,

    @field:XmlAttribute(name = "Bydelsnummer")
    var bydelsnummer: String? = null,

    @field:XmlAttribute(name = "Bydelsnavn")
    var bydelsnavn: String? = null,

    @field:XmlAttribute(name = "Distriktsnummer")
    var distriktsnummer: String? = null,

    @field:XmlAttribute(name = "Saksbehandler", required = true)
    var saksbehandler: String? = null,

    @field:XmlAttribute(name = "Avslutta3112", required = true)
    var avslutta3112: String? = null
) {
    companion object {

        fun createRandomIndividType(id: String, year: Int) : IndividType =
            IndividType(
                id = id,
                //startDato = DateUtils.createRandomDate(year),
                journalnummer = "~Journalnummer~", // TODO
                saksbehandler = "~saksbehandler~",
                avslutta3112 = (1..2).random().toString()
            )
    }
}
