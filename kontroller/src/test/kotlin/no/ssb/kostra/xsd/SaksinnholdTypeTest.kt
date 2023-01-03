package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.XsdTestUtils.EMPTY_PRESISERING_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.TOO_LONG_PRESISERING_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.buildKostraXml
import org.xml.sax.SAXException

class SaksinnholdTypeTest : BehaviorSpec({

    given("misc Saksinnhold XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Saksinnhold Kode=\"1\"><Presisering>~Presisering~</Presisering></Saksinnhold>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Kode */
            row(
                "missing Kode",
                "<Saksinnhold />",
                "cvc-complex-type.4: Attribute 'Kode' must appear on element 'Saksinnhold'."
            ),
            row(
                "empty Kode",
                "<Saksinnhold Kode=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_KodeSaksinnholdType'."
            ),
            row(
                "invalid Kode",
                "<Saksinnhold Kode=\"42\" />",
                "cvc-enumeration-valid: Value '42' is not facet-valid with respect to enumeration " +
                        "'[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, " +
                        "25, 26, 27, 28, 29, 30, 31]'. It must be a value from the enumeration."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<Saksinnhold Kode=\"1\"><Presisering></Presisering></Saksinnhold>",
                EMPTY_PRESISERING_ERROR
            ),
            row(
                "too long Presisering",
                "<Saksinnhold Kode=\"1\"><Presisering>${"a".repeat(1001)}</Presisering></Saksinnhold>",
                TOO_LONG_PRESISERING_ERROR
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
        private fun buildXmlInTest(saksinnholdXml: String): String = buildKostraXml(
            "<Melding Id=\"42\" StartDato=\"2022-11-14\">" +
                    saksinnholdXml +
                    "</Melding>"
        )
    }
}
