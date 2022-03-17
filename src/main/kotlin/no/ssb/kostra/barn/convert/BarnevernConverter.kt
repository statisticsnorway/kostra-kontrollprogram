package no.ssb.kostra.barn.convert

import no.ssb.kostra.barn.xsd.BarnevernType
import java.io.StringWriter
import javax.xml.bind.JAXBContext

object BarnevernConverter {

    private val jaxbContext: JAXBContext =
        JAXBContext.newInstance(BarnevernType::class.java)

    @JvmStatic
    fun unmarshallXml(xml: String): BarnevernType =
        jaxbContext
            .createUnmarshaller()
            .unmarshal(xml.byteInputStream()) as BarnevernType

    @JvmStatic
    fun marshallInstance(barnevernType: BarnevernType): String =
        StringWriter().use {
            jaxbContext
                .createMarshaller()
                .marshal(barnevernType, it)
            return it.toString()
        }
}