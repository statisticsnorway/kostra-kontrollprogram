package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.TestUtils.buildKostraXml
import org.xml.sax.SAXException

class MelderTypeTest : BehaviorSpec({

    given("misc Melder XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Melder Kode=\"1\"><Presisering>~Presisering~</Presisering></Melder>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Kode */
            row(
                "missing Kode",
                "<Melder><Presisering>~Presisering~</Presisering></Melder>",
                "cvc-complex-type.4: Attribute 'Kode' must appear on element 'Melder'."
            ),
            row(
                "empty Kode",
                "<Melder Kode=\"\"><Presisering>~Presisering~</Presisering></Melder>",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_KodeMelderType'."
            ),
            row(
                "invalid Kode",
                "<Melder Kode=\"42\"><Presisering>~Presisering~</Presisering></Melder>",
                "cvc-enumeration-valid: Value '42' is not facet-valid with respect to enumeration " +
                        "'[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]'. " +
                        "It must be a value from the enumeration."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<Melder Kode=\"1\"><Presisering></Presisering></Melder>",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect " +
                        "to minLength '1' for type '#AnonType_Presisering'."
            ),
            row(
                "too long Presisering",
                "<Melder Kode=\"1\"><Presisering>${"a".repeat(1001)}</Presisering></Melder>",
                "cvc-maxLength-valid: Value '${"a".repeat(1001)}' with length = '1001' is not facet-valid " +
                        "with respect to maxLength '1000' for type '#AnonType_Presisering'."
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
        private fun buildXmlInTest(melderXml: String): String = buildKostraXml(
            "<Melding Id=\"42\" StartDato=\"2022-11-14\">" +
                    melderXml +
                    "</Melding>"
        )
    }
}
