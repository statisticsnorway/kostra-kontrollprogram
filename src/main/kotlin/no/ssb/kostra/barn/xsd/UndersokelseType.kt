package no.ssb.kostra.barn.xsd

import no.ssb.kostra.barn.convert.LocalDateAdapter
import java.time.LocalDate
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UndersokelseType", propOrder = ["presisering", "vedtaksgrunnlag"])
data class UndersokelseType(

    @field:XmlElement(name = "Presisering")
    var presisering: String? = null,

    @field:XmlElement(name = "Vedtaksgrunnlag")
    var vedtaksgrunnlag: List<VedtaksgrunnlagType>? = null,

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

    @field:XmlAttribute(name = "Konklusjon")
    var konklusjon: List<String>? = null
) {
    companion object {

        fun createRandomUndersokelseType(id: String, year: Int): UndersokelseType =
            UndersokelseType(
                id = id,
                //startDato = DateUtils.createRandomDate(year)
            )
    }
}
