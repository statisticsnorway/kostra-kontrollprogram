package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.INDIVID_XSD_RESOURCE
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.XsdTestUtils.EMPTY_PRESISERING_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.TOO_LONG_PRESISERING_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.buildIndividXml
import org.xml.sax.SAXException

class VedtaksgrunnlagTypeTest : BehaviorSpec({

    Given("misc Vedtaksgrunnlag XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Vedtaksgrunnlag Kode=\"1\">" +
                                "<Presisering>~Presisering~</Presisering></Vedtaksgrunnlag>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Kode */
            row(
                "missing Kode",
                "<Vedtaksgrunnlag />",
                "cvc-complex-type.4: Attribute 'Kode' must appear on element 'Vedtaksgrunnlag'."
            ),
            row(
                "empty Kode",
                "<Vedtaksgrunnlag Kode=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_KodeSaksinnholdType'."
            ),
            row(
                "invalid Kode",
                "<Vedtaksgrunnlag Kode=\"42\" />",
                "cvc-enumeration-valid: Value '42' is not facet-valid with respect to enumeration " +
                        "'[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, " +
                        "25, 26, 27, 28, 29, 30, 31]'. It must be a value from the enumeration."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<Vedtaksgrunnlag Kode=\"1\"><Presisering></Presisering></Vedtaksgrunnlag>",
                EMPTY_PRESISERING_ERROR
            ),
            row(
                "too long Presisering",
                "<Vedtaksgrunnlag Kode=\"1\"><Presisering>${"a".repeat(1001)}</Presisering></Vedtaksgrunnlag>",
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
        private fun buildXmlInTest(vedtaksgrunnlagXml: String) = buildIndividXml(
            "<Melding Id=\"42\" StartDato=\"2022-11-14\">" +
                    "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\">" +
                    vedtaksgrunnlagXml +
                    "</Undersokelse></Melding>"
        )
    }
}
