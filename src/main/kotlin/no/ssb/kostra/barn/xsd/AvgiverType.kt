package no.ssb.kostra.barn.xsd

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvgiverType")
data class AvgiverType(

    @field:XmlAttribute(name = "Organisasjonsnummer", required = true)
    var organisasjonsnummer: String? = null,

    @field:XmlAttribute(name = "Versjon", required = true)
    var versjon: Int? = null,

    @field:XmlAttribute(name = "Kommunenummer", required = true)
    var kommunenummer: String? = null,

    @field:XmlAttribute(name = "Kommunenavn", required = true)
    var kommunenavn: String? = null
) {
    companion object {
        fun createRandomAvgiverType(year: Int): AvgiverType =
            AvgiverType(
                organisasjonsnummer = "~organisasjonsnummer~",
                versjon = year,
                kommunenummer = "~kommunenummer~",
                kommunenavn = "~kommunenavn~"
            )
    }
}
