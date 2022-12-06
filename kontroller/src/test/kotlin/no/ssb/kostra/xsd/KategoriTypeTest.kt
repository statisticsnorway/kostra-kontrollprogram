package no.ssb.kostra.xsd

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.barn.KostraValidationUtils.getSchemaValidator
import no.ssb.kostra.xsd.TestUtils.LOVHJEMMEL_XML
import no.ssb.kostra.xsd.TestUtils.buildKostraXml
import org.xml.sax.SAXException

class KategoriTypeTest : BehaviorSpec({

    given("misc Kategori XML") {

        /** make sure it's possible to make a valid test XML */
        `when`("valid XML, expect no exceptions") {
            shouldNotThrowAny {
                getSchemaValidator().validate(
                    buildXmlInTest(
                        "<Kategori Kode=\"1.1\"><Presisering>~Presisering~</Presisering></Kategori>"
                    ).toStreamSource()
                )
            }
        }

        forAll(
            /** Kode */
            row(
                "missing Kode",
                "<Kategori><Presisering>~Presisering~</Presisering></Kategori>",
                "cvc-complex-type.4: Attribute 'Kode' must appear on element 'Kategori'."
            ),
            row(
                "empty Kode",
                "<Kategori Kode=\"\"><Presisering>~Presisering~</Presisering></Kategori>",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to " +
                        "minLength '3' for type '#AnonType_KodeKategoriType'."
            ),
            row(
                "invalid Kode",
                "<Kategori Kode=\"42.42\"><Presisering>~Presisering~</Presisering></Kategori>",
                "cvc-enumeration-valid: Value '42.42' is not facet-valid with respect to enumeration " +
                        "'[1.1, 1.2, 1.99, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.99, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, " +
                        "3.8, 3.9, 3.10, 3.99, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 4.99, 5.1, 5.2, 5.3, " +
                        "5.4, 5.99, 6.1, 6.2, 6.3, 6.4, 6.5, 6.99, 7.1, 7.2, 7.3, 7.99, 8.1, 8.2, 8.3, 8.99]'. " +
                        "It must be a value from the enumeration."
            ),

            /** Presisering */
            row(
                "empty Presisering",
                "<Kategori Kode=\"1.1\"><Presisering></Presisering></Kategori>",
                "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect " +
                        "to minLength '1' for type '#AnonType_Presisering'."
            ),
            row(
                "too long Presisering",
                "<Kategori Kode=\"1.1\"><Presisering>${"a".repeat(101)}</Presisering></Kategori>",
                "cvc-maxLength-valid: Value '${"a".repeat(101)}' with length = '101' is not facet-valid " +
                        "with respect to maxLength '100' for type '#AnonType_Presisering'."
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
        private fun buildXmlInTest(kategoriXml: String) = buildKostraXml(
            "<Tiltak Id=\"42\" StartDato=\"2022-11-14\">" +
                    LOVHJEMMEL_XML +
                    kategoriXml +
                    "</Tiltak>"
        )
    }
}
