package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.XsdTestUtils.EMPTY_DATE_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.EMPTY_ID_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.INVALID_DATE_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.TOO_LONG_ID_ERROR
import no.ssb.kostra.xsd.XsdTestUtils.buildKostraXml
import org.xml.sax.SAXException

class MeldingTypeTest : BehaviorSpec({

    Given("misc Melding XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildKostraXml(
                        "<Melding Id=\"42\" StartDato=\"2022-11-14\" " +
                                "SluttDato=\"2022-11-15\" Konklusjon=\"1\" />"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Id */
            row(
                "missing Id",
                "<Melding StartDato=\"2022-11-14\" />",
                "cvc-complex-type.4: Attribute 'Id' must appear on element 'Melding'."
            ),
            row(
                "empty Id",
                "<Melding Id=\"\" StartDato=\"2022-11-14\" />",
                EMPTY_ID_ERROR
            ),
            row(
                "too long Id",
                "<Melding Id=\"${"a".repeat(31)}\" StartDato=\"2022-11-14\" />",
                TOO_LONG_ID_ERROR
            ),

            /** StartDato */
            row(
                "missing StartDato",
                "<Melding Id=\"42\" />",
                "cvc-complex-type.4: Attribute 'StartDato' must appear on element 'Melding'."
            ),
            row(
                "empty StartDato",
                "<Melding Id=\"42\" StartDato=\"\" />",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid StartDato",
                "<Melding Id=\"42\" StartDato=\"2022\" />",
                INVALID_DATE_ERROR
            ),

            /** SluttDato */
            row(
                "empty SluttDato",
                "<Melding Id=\"42\" StartDato=\"2022-11-14\" " +
                        "SluttDato=\"\" Konklusjon=\"1\" />",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid SluttDato",
                "<Melding Id=\"42\" StartDato=\"2022-11-14\" " +
                        "SluttDato=\"2022\" Konklusjon=\"1\" />",
                INVALID_DATE_ERROR
            ),

            /** Konklusjon */
            row(
                "empty Konklusjon",
                "<Melding Id=\"42\" StartDato=\"2022-11-14\" " +
                        "SluttDato=\"2022-11-15\" Konklusjon=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect " +
                        "to minLength '1' for type '#AnonType_KonklusjonMeldingType'."
            ),
            row(
                "invalid Konklusjon",
                "<Melding Id=\"42\" StartDato=\"2022-11-14\" " +
                        "SluttDato=\"2022-11-15\" Konklusjon=\"5\" />",
                "cvc-enumeration-valid: Value '5' is not facet-valid with respect to enumeration '[1, 2, 3, 4]'. " +
                        "It must be a value from the enumeration."
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