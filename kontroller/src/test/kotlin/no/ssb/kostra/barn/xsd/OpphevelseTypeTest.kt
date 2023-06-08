package no.ssb.kostra.barn.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.INDIVID_XSD_RESOURCE
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.barn.xsd.XsdTestUtils.EMPTY_PRESISERING_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.LOVHJEMMEL_XML
import no.ssb.kostra.barn.xsd.XsdTestUtils.TOO_LONG_PRESISERING_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.buildIndividXml
import org.xml.sax.SAXException

class OpphevelseTypeTest : BehaviorSpec({

    Given("misc Opphevelse XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
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
                EMPTY_PRESISERING_ERROR
            ),
            row(
                "too long Presisering",
                "<Opphevelse Kode=\"1\"><Presisering>${"a".repeat(1001)}</Presisering></Opphevelse>",
                TOO_LONG_PRESISERING_ERROR
            )
        ) { description, partialXml, expectedError ->
            When(description) {
                val thrown = shouldThrow<SAXException> {
                    getSchemaValidator(INDIVID_XSD_RESOURCE).validate(buildXmlInTest(partialXml).toStreamSource())
                }

                Then("thrown should be as expected") {
                    thrown.message shouldBe expectedError
                }
            }
        }
    }
}) {
    companion object {
        private fun buildXmlInTest(opphevelseXml: String): String = buildIndividXml(
            "<Tiltak Id=\"42\" StartDato=\"2022-11-14\">" +
                    LOVHJEMMEL_XML +
                    "<Kategori Kode=\"1.1\" />" +
                    opphevelseXml +
                    "</Tiltak>"
        )
    }
}
