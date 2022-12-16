package no.ssb.kostra.xsd

import java.io.StringReader
import javax.xml.transform.stream.StreamSource


fun String.toStreamSource() = StreamSource(StringReader(this))


object XsdTestUtils {

    const val LOVHJEMMEL_XML = "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" Ledd=\"3\"/>"

    const val EMPTY_DATE_ERROR = "cvc-datatype-valid.1.2.1: '' is not a valid value for 'date'."
    const val INVALID_DATE_ERROR = "cvc-datatype-valid.1.2.1: '2022' is not a valid value for 'date'."

    const val EMPTY_ID_ERROR = "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
            "minLength '1' for type '#AnonType_Id'."
    const val TOO_LONG_ID_ERROR = "cvc-maxLength-valid: Value 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' with length = '31' " +
            "is not facet-valid with respect to maxLength '30' for type '#AnonType_Id'."

    const val EMPTY_PRESISERING_ERROR = "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with " +
            "respect to minLength '1' for type '#AnonType_Presisering'."
    val TOO_LONG_PRESISERING_ERROR = "cvc-maxLength-valid: Value '${"a".repeat(1001)}' with length = '1001' " +
            "is not facet-valid with respect to maxLength '1000' for type '#AnonType_Presisering'."

    fun buildKostraXml(innerXml: String) =
        "<Barnevern>" +
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                "Organisasjonsnummer=\"999999999\"/>" +
                "<Individ Saksbehandler=\"Sara Saksbehandler\" Avslutta3112=\"1\" " +
                "StartDato=\"2022-11-14\" Id=\"42\" Journalnummer=\"2022-00004\">" +
                innerXml +
                "</Individ></Barnevern>"
}
