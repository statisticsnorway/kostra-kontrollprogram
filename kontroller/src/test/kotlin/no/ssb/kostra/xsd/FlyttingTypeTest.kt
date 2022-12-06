package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.TestUtils.EMPTY_DATE_ERROR
import no.ssb.kostra.xsd.TestUtils.EMPTY_ID_ERROR
import no.ssb.kostra.xsd.TestUtils.INVALID_DATE_ERROR
import no.ssb.kostra.xsd.TestUtils.TOO_LONG_ID_ERROR
import no.ssb.kostra.xsd.TestUtils.buildKostraXml
import org.xml.sax.SAXException

class FlyttingTypeTest : BehaviorSpec({

    given("misc Flytting XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildFlyttingXml(
                        "<Flytting Id=\"42\" " +
                                "SluttDato=\"2022-11-14\">"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Id */
            row(
                "missing Id",
                "<Flytting SluttDato=\"2022-11-14\">",
                "cvc-complex-type.4: Attribute 'Id' must appear on element 'Flytting'."
            ),
            row(
                "empty Id",
                "<Flytting Id=\"\" SluttDato=\"2022-11-14\">",
                EMPTY_ID_ERROR
            ),
            row(
                "too long Id",
                "<Flytting Id=\"${"a".repeat(31)}\" SluttDato=\"2022-11-14\">",
                TOO_LONG_ID_ERROR
            ),

            /** SluttDato */
            row(
                "missing SluttDato",
                "<Flytting Id=\"42\">",
                "cvc-complex-type.4: Attribute 'SluttDato' must appear on element 'Flytting'."
            ),
            row(
                "empty SluttDato",
                "<Flytting Id=\"42\" SluttDato=\"\">",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid SluttDato",
                "<Flytting Id=\"42\" SluttDato=\"2022\">",
                INVALID_DATE_ERROR
            )
        ) { description, partialXml, expectedError ->
            `when`(description) {
                val thrown = shouldThrow<SAXException> {
                    getSchemaValidator().validate(buildFlyttingXml(partialXml).toStreamSource())
                }

                then("thrown should be as expected") {
                    thrown.message shouldBe expectedError
                }
            }
        }
    }
}) {
    companion object {
        private fun buildFlyttingXml(startTag: String) = buildKostraXml(
            startTag +
                    "<ArsakFra Kode=\"1.1.1\" />" +
                    "<FlyttingTil Kode=\"1\" />" +
                    "</Flytting>"
        )
    }
}