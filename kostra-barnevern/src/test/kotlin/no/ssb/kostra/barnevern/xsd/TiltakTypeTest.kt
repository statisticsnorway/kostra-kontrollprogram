package no.ssb.kostra.barnevern.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barnevern.KostraValidationUtils.INDIVID_XSD_RESOURCE
import no.ssb.kostra.barnevern.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.EMPTY_DATE_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.EMPTY_ID_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.INVALID_DATE_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.JMFR_LOVHJEMMEL_XML
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.LOVHJEMMEL_XML
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.TOO_LONG_ID_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.buildIndividXml
import org.xml.sax.SAXException

class TiltakTypeTest : BehaviorSpec({

    Given("misc TiltakType XML") {

        /** make sure it's possible to make a valid test XML */
        When("minimal valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator(INDIVID_XSD_RESOURCE).validate(
                    buildIndividXml(
                        "<Tiltak Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"2022-11-15\">" +
                                LOVHJEMMEL_XML +
                                "<Kategori Kode=\"1.1\"/>" +
                                "</Tiltak>"
                    ).toStreamSource()
                )
            }
        }

        /** make sure it's possible to make a valid test XML */
        When("maximal valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator(INDIVID_XSD_RESOURCE).validate(
                    buildIndividXml(
                        "<Tiltak Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"2022-11-15\">" +
                                LOVHJEMMEL_XML +
                                JMFR_LOVHJEMMEL_XML +
                                "<Kategori Kode=\"1.1\"/>" +
                                "<Tiltaksgrunnlag Kode=\"1\"/>" +
                                "<Opphevelse Kode=\"4\"><Presisering>~presisering~</Presisering></Opphevelse>" +
                                "</Tiltak>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Id */
            row(
                "duplicate Id",
                "<Tiltak  Id=\"42\" StartDato=\"2022-11-14\">" +
                        LOVHJEMMEL_XML + "<Kategori Kode=\"1.1\"/></Tiltak>" +
                        "<Tiltak  Id=\"42\" StartDato=\"2022-11-14\">" +
                        LOVHJEMMEL_XML + "<Kategori Kode=\"1.1\"/></Tiltak>",
                "cvc-identity-constraint.4.1: Duplicate unique value [42] declared for identity " +
                        "constraint \"TiltakIdUnique\" of element \"Individ\"."
            ),
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
                    getSchemaValidator(INDIVID_XSD_RESOURCE).validate(buildIndividXml(partialXml).toStreamSource())
                }

                Then("thrown should be as expected") {
                    thrown.message shouldBe expectedError
                }
            }
        }
    }
})