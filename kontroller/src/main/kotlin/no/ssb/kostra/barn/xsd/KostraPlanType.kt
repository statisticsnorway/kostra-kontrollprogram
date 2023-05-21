package no.ssb.kostra.barn.xsd

import java.time.LocalDate
import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlanType")
data class KostraPlanType(

    @field:XmlAttribute(name = "Id", required = true)
    val id: String,

    @field:XmlAttribute(name = "StartDato", required = true)
    @field:XmlSchemaType(name = "date")
    val startDato: LocalDate,

    @field:XmlAttribute(name = "SluttDato")
    @field:XmlSchemaType(name = "date")
    val sluttDato: LocalDate? = null,

    @field:XmlAttribute(name = "Plantype", required = true)
    val plantype: String,

    @field:XmlAttribute(name = "EvaluertDato")
    @field:XmlSchemaType(name = "date")
    val evaluertDato: LocalDate? = null
)