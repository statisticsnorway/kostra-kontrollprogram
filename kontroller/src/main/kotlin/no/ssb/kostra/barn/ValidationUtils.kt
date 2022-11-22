package no.ssb.kostra.barn

import org.xml.sax.SAXParseException
import java.io.InputStream
import java.io.Reader
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator


object ValidationUtils {

    private const val XSD_RESOURCE = "KostraBarnevernSingleFile.xsd"
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
        } catch (e: SAXParseException) {
            false
        }
    }

    @JvmStatic
    fun getSchemaValidator(): Validator {
        val newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        newInstance.setFeature(DISALLOW_DOCTYPE_DECL, true)
        return newInstance
            .newSchema(StreamSource(getResourceFromClasspath()))
            .newValidator()
    }

    private fun getResourceFromClasspath(): InputStream? =
        this::class.java.classLoader.getResource(XSD_RESOURCE)!!.openStream()
}
