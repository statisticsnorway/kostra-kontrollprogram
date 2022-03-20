package no.ssb.kostra.barn.convert

import no.ssb.kostra.barn.xsd.KostraBarnevernType
import java.io.StringWriter
import javax.xml.bind.JAXBContext

object BarnevernConverter {

    private val jaxbContext: JAXBContext =
        JAXBContext.newInstance(KostraBarnevernType::class.java)

    @JvmStatic
    fun unmarshallXml(xml: String): KostraBarnevernType =
        jaxbContext
            .createUnmarshaller()
            .unmarshal(xml.byteInputStream()) as KostraBarnevernType

    @JvmStatic
    fun marshallInstance(barnevernType: KostraBarnevernType): String =
        StringWriter().use {
            jaxbContext
                .createMarshaller()
                .marshal(barnevernType, it)
            return it.toString()
        }
}