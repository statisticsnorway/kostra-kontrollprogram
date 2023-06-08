package no.ssb.kostra.barn

import org.xml.sax.SAXParseException
import java.io.Reader
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator


object KostraValidationUtils {

    private const val KOSTRA_BARNEVERN_XSD_RESOURCE = "KostraBarnevern.xsd"
    const val AVGIVER_XSD_RESOURCE = "Avgiver.xsd"
    const val INDIVID_XSD_RESOURCE = "Individ.xsd"
    private const val DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl"

    @JvmStatic
    fun validate(xml: String): Boolean = validate(StringReader(xml))

    @JvmStatic
    fun validate(xmlReader: Reader, xsdResource: String = KOSTRA_BARNEVERN_XSD_RESOURCE): Boolean = try {
        getSchemaValidator(xsdResource).validate(StreamSource(xmlReader))
        true
    } catch (thrown: SAXParseException) {
        false
    }

    @JvmStatic
    fun getSchemaValidator(xsdResource: String = KOSTRA_BARNEVERN_XSD_RESOURCE): Validator = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        .apply { setFeature(DISALLOW_DOCTYPE_DECL, true) }
        .newSchema(javaClass.classLoader.getResource(xsdResource))
        .newValidator()
}
