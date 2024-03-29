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
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.EMPTY_PRESISERING_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.INVALID_DATE_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.TOO_LONG_ID_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.TOO_LONG_PRESISERING_ERROR
import no.ssb.kostra.barnevern.xsd.XsdTestUtils.buildIndividXml
import org.xml.sax.SAXException

class UndersokelseTypeTest : BehaviorSpec({

    Given("misc Undersokelse XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\" " +
                                "SluttDato=\"2022-11-15\" Konklusjon=\"1\">" +
                                "<Presisering>~Presisering~</Presisering></Undersokelse>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Id */
            row(
                "missing Id",
                "<Undersokelse StartDato=\"2022-11-14\" />",
                "cvc-complex-type.4: Attribute 'Id' must appear on element 'Undersokelse'."
            ),
            row(
                "empty Id",
                "<Undersokelse Id=\"\" StartDato=\"2022-11-14\" />",
                EMPTY_ID_ERROR
            ),
            row(
                "too long Id",
                "<Undersokelse Id=\"${"a".repeat(31)}\" StartDato=\"2022-11-14\"/>",
                TOO_LONG_ID_ERROR
            ),

            /** StartDato */
            row(
                "missing StartDato",
                "<Undersokelse Id=\"42\"/>",
                "cvc-complex-type.4: Attribute 'StartDato' must appear on element 'Undersokelse'."
            ),
            row(
                "empty StartDato",
                "<Undersokelse Id=\"42\" StartDato=\"\"/>",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid StartDato",
                "<Undersokelse Id=\"42\" StartDato=\"2022\"/>",
                INVALID_DATE_ERROR
            ),

            /** SluttDato */
            row(
                "empty SluttDato",
                "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"\"/>",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid SluttDato",
                "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\" SluttDato=\"2022\"/>",
                INVALID_DATE_ERROR
            ),

            /** Konklusjon */
            row(
                "empty Konklusjon",
                "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\" Konklusjon=\"\">",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect " +
                        "to minLength '1' for type '#AnonType_KonklusjonUndersokelseType'."
            ),
            row(
                "invalid Konklusjon",
                "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\" Konklusjon=\"9\">",
                "cvc-enumeration-valid: Value '9' is not facet-valid with respect to enumeration " +
                        "'[1, 2, 3, 4, 5, 6]'. It must be a value from the enumeration."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\">" +
                        "<Presisering></Presisering></Undersokelse>",
                EMPTY_PRESISERING_ERROR
            ),
            row(
                "too long Presisering",
                "<Undersokelse Id=\"42\" StartDato=\"2022-11-14\">" +
                        "<Presisering>${"a".repeat(1001)}</Presisering></Undersokelse>",
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
        private fun buildXmlInTest(undersokelseXml: String) = buildIndividXml(
            "<Melding Id=\"42\" StartDato=\"2022-11-14\">" +
                    undersokelseXml + "</Melding>"
        )
    }
}