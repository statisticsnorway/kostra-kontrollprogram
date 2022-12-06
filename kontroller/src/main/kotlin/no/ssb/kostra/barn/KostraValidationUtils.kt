package no.ssb.kostra.barn

import org.xml.sax.SAXParseException
import java.io.Reader
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator


object KostraValidationUtils {

    private const val XSD_RESOURCE = "KostraBarnevern.xsd"
    private const val DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl"

    @JvmStatic
    fun validate(xml: String): Boolean {
        return validate(StringReader(xml))

    }

    @JvmStatic
    fun validate(xmlReader: Reader): Boolean {
        return try {
            getSchemaValidator().validate(StreamSource(xmlReader))
            true
        } catch (thrown: SAXParseException) {
            false
        }
    }

    @JvmStatic
    fun getSchemaValidator(): Validator = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        .apply { setFeature(DISALLOW_DOCTYPE_DECL, true) }
        .newSchema(javaClass.classLoader.getResource(XSD_RESOURCE))
        .newValidator()
}
