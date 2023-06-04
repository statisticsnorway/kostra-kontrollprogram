package no.ssb.kostra.barn.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.barn.xsd.XsdTestUtils.EMPTY_DATE_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.EMPTY_ID_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.INVALID_DATE_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.LOVHJEMMEL_XML
import no.ssb.kostra.barn.xsd.XsdTestUtils.TOO_LONG_ID_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.buildKostraXml
import org.xml.sax.SAXException

class TiltakTypeTest : BehaviorSpec({

    Given("misc TiltakType XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(buildKostraXml(
                    "<Tiltak Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"2022-11-15\">" +
                            LOVHJEMMEL_XML +
                            "<Kategori Kode=\"1.1\"/>" +
                            "</Tiltak>").toStreamSource())
            }
        }

        forAll(
            /** Id */
            row(
                "missing Id",
                "<Tiltak " +
                        "StartDato=\"2022-11-14\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                "cvc-complex-type.4: Attribute 'Id' must appear on element 'Tiltak'."
            ),
            row(
                "empty Id",
                "<Tiltak Id=\"\" " +
                        "StartDato=\"2022-11-14\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                EMPTY_ID_ERROR
            ),
            row(
                "too long Id",
                "<Tiltak Id=\"${"a".repeat(31)}\" " +
                        "StartDato=\"2022-11-14\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                TOO_LONG_ID_ERROR
            ),

            /** StartDato */
            row(
                "missing StartDato",
                "<Tiltak Id=\"42\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                "cvc-complex-type.4: Attribute 'StartDato' must appear on element 'Tiltak'."
            ),
            row(
                "empty StartDato",
                "<Tiltak Id=\"42\" StartDato=\"\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid StartDato",
                "<Tiltak Id=\"42\" StartDato=\"2022\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                INVALID_DATE_ERROR
            ),

            /** SluttDato */
            row(
                "empty SluttDato",
                "<Tiltak Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid SluttDato",
                "<Tiltak Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"2022\">" +
                        LOVHJEMMEL_XML +
                        "<Kategori Kode=\"1.1\"/>" +
                        "</Tiltak>",
                INVALID_DATE_ERROR
            )
        ) { description, partialXml, expectedError ->
            When(description) {
                val thrown = shouldThrow<SAXException> {
                    getSchemaValidator().validate(buildKostraXml(partialXml).toStreamSource())
                }

                Then("thrown should be as expected") {
                    thrown.message shouldBe expectedError
                }
            }
        }
    }
})