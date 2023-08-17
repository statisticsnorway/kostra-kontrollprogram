package no.ssb.kostra.barnevern.xsd

import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VedtaksgrunnlagType", propOrder = ["presisering"])
data class KostraVedtaksgrunnlagType(

    @field:XmlAttribute(name = "Kode", required = true)
    val kode: String,

    @field:XmlElement(name = "Presisering")
    val presisering: String? = null
)