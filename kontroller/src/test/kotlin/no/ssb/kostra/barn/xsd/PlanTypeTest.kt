package no.ssb.kostra.barn.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.INDIVID_XSD_RESOURCE
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.barn.xsd.XsdTestUtils.EMPTY_DATE_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.EMPTY_ID_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.INVALID_DATE_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.TOO_LONG_ID_ERROR
import no.ssb.kostra.barn.xsd.XsdTestUtils.buildIndividXml
import org.xml.sax.SAXException

class PlanTypeTest : BehaviorSpec({

    Given("misc Plan XML") {

        /** make sure it's possible to make a valid test XML */
        When("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator(INDIVID_XSD_RESOURCE).validate(
                    buildIndividXml(
                        "<Plan Id=\"42\" StartDato=\"2022-11-14\" Plantype=\"1\"/>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Id */
            row(
                "duplicate Id",
                "<Plan Id=\"42\"  StartDato=\"2022-11-14\" Plantype=\"1\" />" +
                        "<Plan Id=\"42\"  StartDato=\"2022-11-14\" Plantype=\"1\" />",
                "cvc-identity-constraint.4.1: Duplicate unique value [42] declared for identity " +
                        "constraint \"PlanIdUnique\" of element \"Individ\"."
            ),
            row(
                "missing Id",
                "<Plan StartDato=\"2022-11-14\" Plantype=\"1\" />",
                "cvc-complex-type.4: Attribute 'Id' must appear on element 'Plan'."
            ),
            row(
                "empty Id",
                "<Plan Id=\"\" StartDato=\"2022-11-14\" Plantype=\"1\" />",
                EMPTY_ID_ERROR
            ),
            row(
                "too long Id",
                "<Plan Id=\"${"a".repeat(31)}\" StartDato=\"2022-11-14\" Plantype=\"1\" />",
                TOO_LONG_ID_ERROR
            ),

            /** StartDato */
            row(
                "missing StartDato",
                "<Plan Id=\"42\" Plantype=\"1\" />",
                "cvc-complex-type.4: Attribute 'StartDato' must appear on element 'Plan'."
            ),
            row(
                "empty StartDato",
                "<Plan Id=\"42\" StartDato=\"\" Plantype=\"1\" />",
                EMPTY_DATE_ERROR
            ),
            row(
                "invalid StartDato",
                "<Plan Id=\"42\" StartDato=\"2022\" Plantype=\"1\" />",
                INVALID_DATE_ERROR
            ),

            /** Plantype */
            row(
                "missing Plantype",
                "<Plan Id=\"42\" StartDato=\"2022-11-14\" />",
                "cvc-complex-type.4: Attribute 'Plantype' must appear on element 'Plan'."
            ),
            row(
                "empty Plantype",
                "<Plan Id=\"42\" StartDato=\"2022-11-14\" Plantype=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_PlantypePlanType'."
            ),
            row(
                "invalid Plantype",
                "<Plan Id=\"42\" StartDato=\"2022-11-14\" Plantype=\"42\" />",
                "cvc-enumeration-valid: Value '42' is not facet-valid with respect to " +
                        "enumeration '[1, 2, 3, 4, 5, 6, 7, 8]'. It must be a value from the enumeration."
            ),
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