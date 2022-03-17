package no.ssb.kostra.barn.xsd

import no.ssb.kostra.barn.convert.LocalDateAdapter
import java.time.LocalDate
import javax.xml.bind.annotation.*
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TiltakType", propOrder = ["lovhjemmel", "jmfrLovhjemmel", "kategori", "opphevelse"])
data class TiltakType(

    @field:XmlElement(name = "Lovhjemmel", required = true)
    var lovhjemmel: LovhjemmelType? = null,

    @field:XmlElement(name = "JmfrLovhjemmel")
    var jmfrLovhjemmel: List<LovhjemmelType>? = null,

    @field:XmlElement(name = "Kategori", required = true)
    var kategori: KategoriType? = null,

    @field:XmlElement(name = "Opphevelse")
    var opphevelse: OpphevelseType? = null,

    @field:XmlAttribute(name = "Id", required = true)
    var id: String? = null,

    @field:XmlAttribute(name = "StartDato", required = true)
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    var startDato: LocalDate? = null,

    @field:XmlAttribute(name = "SluttDato")
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    var sluttDato: LocalDate? = null
) {
    companion object {
        fun createRandomTiltakType(id: String, year: Int): TiltakType =
            TiltakType(
                lovhjemmel = LovhjemmelType.createRandomLovhjemmelType(),
                kategori = KategoriType.createRandomKategoriType(),
                id = id,
                //startDato = DateUtils.createRandomDate(year)
            )
    }
}
