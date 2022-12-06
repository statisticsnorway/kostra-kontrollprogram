package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.XsdTestUtils.LOVHJEMMEL_XML
import no.ssb.kostra.xsd.XsdTestUtils.buildKostraXml
import org.xml.sax.SAXException

class OpphevelseTypeTest : BehaviorSpec({

    given("misc Opphevelse XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Opphevelse Kode=\"1\"><Presisering>~Presisering~</Presisering></Opphevelse>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Kode */
            row(
                "missing Kode",
                "<Opphevelse />",
                "cvc-complex-type.4: Attribute 'Kode' must appear on element 'Opphevelse'."
            ),
            row(
                "empty Kode",
                "<Opphevelse Kode=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_KodeOpphevelseType'."
            ),
            row(
                "invalid Kode",
                "<Opphevelse Kode=\"5\" />",
                "cvc-enumeration-valid: Value '5' is not facet-valid with respect to enumeration '[1, 2, 3, 4]'. " +
                        "It must be a value from the enumeration."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<Opphevelse Kode=\"1\"><Presisering></Presisering></Opphevelse>",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect " +
                        "to minLength '1' for type '#AnonType_Presisering'."
            ),
            row(
                "too long Presisering",
                "<Opphevelse Kode=\"1\"><Presisering>${"a".repeat(1001)}</Presisering></Opphevelse>",
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
        private fun buildXmlInTest(opphevelseXml: String): String = buildKostraXml(
            "<Tiltak Id=\"42\" StartDato=\"2022-11-14\">" +
                    LOVHJEMMEL_XML +
                    "<Kategori Kode=\"1.1\" />" +
                    opphevelseXml +
                    "</Tiltak>"
        )
    }
}
