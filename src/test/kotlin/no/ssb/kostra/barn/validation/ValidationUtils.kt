package no.ssb.kostra.barn.validation

import org.xml.sax.SAXParseException
import java.io.InputStream
import java.io.StringReader
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator


object ValidationUtils {

    @JvmStatic
    fun validate(xml: String): Boolean {
        return try {
            getSchemaValidator("BarnevernSingleFile.xsd")
                .validate(StreamSource(StringReader(xml)))
            true
        } catch (e: SAXParseException) {
            false
        }
    }

    private fun getSchemaValidator(xsdResourceName: String): Validator {
        val newInstance = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        newInstance.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true)
        return newInstance
            .newSchema(StreamSource(getSourceFromClasspath(xsdResourceName)))
            .newValidator()
    }

    private fun getSourceFromClasspath(resourceName: String): InputStream? =
        this::class.java.classLoader.getResource(resourceName)!!.openStream()
}
