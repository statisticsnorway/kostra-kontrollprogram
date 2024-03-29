package no.ssb.kostra.barnevern.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barnevern.KostraValidationUtils.INDIVID_XSD_RESOURCE
import no.ssb.kostra.barnevern.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.buildIndividXml
import org.xml.sax.SAXException

class FlyttingTilTypeTest : BehaviorSpec({

    Given("misc FlyttingTil XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildFlyttingTilXml(
                        "<FlyttingTil Kode=\"1\" Presisering=\"~Presisering~\" />"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Kode */
            row(
                "missing Kode",
                "<FlyttingTil />",
                "cvc-complex-type.4: Attribute 'Kode' must appear on element 'FlyttingTil'."
            ),
            row(
                "empty Kode",
                "<FlyttingTil Kode=\"\" />",
                "cvc-length-valid: Value '' with length = '0' is not facet-valid with respect " +
                        "to length '1' for type '#AnonType_KodeFlyttingTilType'."
            ),
            row(
                "invalid Kode",
                "<FlyttingTil Kode=\"42\" />",
                "cvc-length-valid: Value '42' with length = '2' is not facet-valid with respect to " +
                        "length '1' for type '#AnonType_KodeFlyttingTilType'."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<FlyttingTil Kode=\"1\" Presisering=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_PresiseringFlyttingTilType'."
            ),
            row(
                "too long Presisering",
                "<FlyttingTil Kode=\"1\" Presisering=\"${"a".repeat(301)}\" />",
                "cvc-maxLength-valid: Value '${"a".repeat(301)}' with length = '301' is not facet-valid with " +
                        "respect to maxLength '300' for type '#AnonType_PresiseringFlyttingTilType'."
            )
        ) { description, partialXml, expectedError ->
            When(description) {
                val thrown = shouldThrow<SAXException> {
                    getSchemaValidator(INDIVID_XSD_RESOURCE).validate(buildFlyttingTilXml(partialXml).toStreamSource())
                }

                Then("thrown should be as expected") {
                    thrown.message shouldBe expectedError
                }
            }
        }
    }
}) {
    companion object {
        private fun buildFlyttingTilXml(innerXml: String) = buildIndividXml(
            "<Flytting Id=\"42\" SluttDato=\"2022-11-14\">" +
                    "<ArsakFra Kode=\"1.1.1\" Presisering=\"~Presisering~\" />" +
                    innerXml +
                    "</Flytting>"
        )
    }
}