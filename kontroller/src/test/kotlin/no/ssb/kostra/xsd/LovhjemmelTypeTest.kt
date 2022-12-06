package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.TestUtils.buildKostraXml
import org.xml.sax.SAXException

class LovhjemmelTypeTest : BehaviorSpec({

    given("misc Lovhjemmel XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                                "Bokstav=\"a\" Ledd=\"3\" Punktum=\"2\" />"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Lov */
            row(
                "missing Lov",
                "<Lovhjemmel Kapittel=\"4\" Paragraf=\"8\" />",
                "cvc-complex-type.4: Attribute 'Lov' must appear on element 'Lovhjemmel'."
            ),
            row(
                "empty Lov",
                "<Lovhjemmel Lov=\"\" Kapittel=\"4\" Paragraf=\"8\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_LovLovhjemmelType'."
            ),
            row(
                "too long Lov",
                "<Lovhjemmel Lov=\"${"a".repeat(101)}\" Kapittel=\"4\" Paragraf=\"8\" />",
                "cvc-maxLength-valid: Value '${"a".repeat(101)}' with length = '101' is not facet-valid " +
                        "with respect to maxLength '100' for type '#AnonType_LovLovhjemmelType'."
            ),

            /** Kapittel */
            row(
                "missing Kapittel",
                "<Lovhjemmel Lov=\"BVL\" Paragraf=\"8\" />",
                "cvc-complex-type.4: Attribute 'Kapittel' must appear on element 'Lovhjemmel'."
            ),
            row(
                "empty Kapittel",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"\" Paragraf=\"8\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_KapittelLovhjemmelType'."
            ),
            row(
                "too long Kapittel",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"123456\" Paragraf=\"8\" />",
                "cvc-maxLength-valid: Value '123456' with length = '6' is not facet-valid with respect to " +
                        "maxLength '5' for type '#AnonType_KapittelLovhjemmelType'."
            ),

            /** Paragraf */
            row(
                "missing Paragraf",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" />",
                "cvc-complex-type.4: Attribute 'Paragraf' must appear on element 'Lovhjemmel'."
            ),
            row(
                "empty Paragraf",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_ParagrafLovhjemmelType'."
            ),
            row(
                "too long Paragraf",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"123456\" />",
                "cvc-maxLength-valid: Value '123456' with length = '6' is not facet-valid with respect to " +
                        "maxLength '5' for type '#AnonType_ParagrafLovhjemmelType'."
            ),

            /** Bokstav */
            row(
                "empty Bokstav",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                        "Bokstav=\"\" Ledd=\"3\" Punktum=\"2\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_BokstavLovhjemmelType'."
            ),
            row(
                "too long Bokstav",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                        "Bokstav=\"ab\" Ledd=\"3\" Punktum=\"2\" />",
                "cvc-maxLength-valid: Value 'ab' with length = '2' is not facet-valid with respect to " +
                        "maxLength '1' for type '#AnonType_BokstavLovhjemmelType'."
            ),

            /** Ledd */
            row(
                "empty Ledd",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                        "Bokstav=\"a\" Ledd=\"\" Punktum=\"2\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_LeddLovhjemmelType'."
            ),
            row(
                "too long Ledd",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                        "Bokstav=\"a\" Ledd=\"123456\" Punktum=\"2\" />",
                "cvc-maxLength-valid: Value '123456' with length = '6' is not facet-valid with respect to " +
                        "maxLength '5' for type '#AnonType_LeddLovhjemmelType'."
            ),

            /** Punktum */
            row(
                "empty Punktum",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                        "Bokstav=\"a\" Ledd=\"3\" Punktum=\"\" />",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '1' for type '#AnonType_PunktumLovhjemmelType'."
            ),
            row(
                "too long Punktum",
                "<Lovhjemmel Lov=\"BVL\" Kapittel=\"4\" Paragraf=\"8\" " +
                        "Bokstav=\"a\" Ledd=\"3\" Punktum=\"123456\" />",
                "cvc-maxLength-valid: Value '123456' with length = '6' is not facet-valid with respect to " +
                        "maxLength '5' for type '#AnonType_PunktumLovhjemmelType'."
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
        private fun buildXmlInTest(lovhjemmelXml: String) = buildKostraXml(
            "<Tiltak Id=\"42\" StartDato=\"2022-11-14\">" +
                    lovhjemmelXml +
                    "<Kategori Kode=\"1.1\"><Presisering>~Presisering~</Presisering></Kategori>" +
                    "</Tiltak>"
        )
    }
}
