package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import org.xml.sax.SAXException

class AvgiverTypeTest : BehaviorSpec({

    given("misc Avgiver XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                                "Organisasjonsnummer=\"999999999\"/>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Versjon */
            row(
                "missing Versjon",
                "<Avgiver Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-complex-type.4: Attribute 'Versjon' must appear on element 'Avgiver'."
            ),
            row(
                "empty Versjon",
                "<Avgiver Versjon=\"\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-datatype-valid.1.2.1: '' is not a valid value for 'gYear'."
            ),
            row(
                "invalid Versjon",
                "<Avgiver Versjon=\"42\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-datatype-valid.1.2.1: '42' is not a valid value for 'gYear'."
            ),

            /** Kommunenummer */
            row(
                "missing Kommunenummer",
                "<Avgiver Versjon=\"2022\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-complex-type.4: Attribute 'Kommunenummer' must appear on element 'Avgiver'."
            ),
            row(
                "empty Kommunenummer",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-pattern-valid: Value '' is not facet-valid with respect to pattern '\\d{4}' for " +
                        "type '#AnonType_KommunenummerAvgiverType'."
            ),
            row(
                "invalid Kommunenummer",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"42\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-pattern-valid: Value '42' is not facet-valid with respect to pattern '\\d{4}' for " +
                        "type '#AnonType_KommunenummerAvgiverType'."
            ),

            /** Kommunenavn */
            row(
                "missing Kommunenavn",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Organisasjonsnummer=\"999999999\"/>",
                "cvc-complex-type.4: Attribute 'Kommunenavn' must appear on element 'Avgiver'."
            ),
            row(
                "empty Kommunenavn",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_KommunenavnAvgiverType'."
            ),
            row(
                "too long Kommunenavn",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"${"a".repeat(251)}\" " +
                        "Organisasjonsnummer=\"999999999\"/>",
                "cvc-maxLength-valid: Value '${"a".repeat(251)}' with length = '251' is not facet-valid " +
                        "with respect to maxLength '250' for type '#AnonType_KommunenavnAvgiverType'."
            ),

            /** Organisasjonsnummer */
            row(
                "missing Organisasjonsnummer",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" />",
                "cvc-complex-type.4: Attribute 'Organisasjonsnummer' must appear on element 'Avgiver'."
            ),
            row(
                "empty Organisasjonsnummer",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"\"/>",
                "cvc-pattern-valid: Value '' is not facet-valid with respect to pattern '\\d{9}' for " +
                        "type '#AnonType_OrganisasjonsnummerAvgiverType'."
            ),
            row(
                "invalid Organisasjonsnummer",
                "<Avgiver Versjon=\"2022\" Kommunenummer=\"1234\" Kommunenavn=\"~Kommunenavn~\" " +
                        "Organisasjonsnummer=\"42\"/>",
                "cvc-pattern-valid: Value '42' is not facet-valid with respect to pattern '\\d{9}' for " +
                        "type '#AnonType_OrganisasjonsnummerAvgiverType'."
            )
        ) { description, partialXml, expectedError ->
            `when`(description) {
                val thrown = shouldThrow<SAXException> {
                    getSchemaValidator().validate(buildXmlInTest(partialXml).toStreamSource())
                }

                then("thrown should be as expected") {
                    thrown.message shouldBe expectedError
                }
            }
        }
    }
}) {
    companion object {
        private fun buildXmlInTest(avgiverXml: String): String =
            "<Barnevern>" +
                    avgiverXml +
                    "<Individ Saksbehandler=\"Sara Saksbehandler\" Avslutta3112=\"1\" " +
                    "StartDato=\"2022-11-14\" Id=\"42\" Journalnummer=\"2022-00004\">" +
                    "</Individ></Barnevern>"
    }
}
