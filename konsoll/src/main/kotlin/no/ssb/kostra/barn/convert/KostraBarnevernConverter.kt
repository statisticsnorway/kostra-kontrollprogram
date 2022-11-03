package no.ssb.kostra.barn.convert

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.ssb.kostra.barn.xsd.KostraBarnevernType

object KostraBarnevernConverter {

    private val kotlinModule: KotlinModule = KotlinModule.Builder()
        .configure(KotlinFeature.StrictNullChecks, false)
        // needed, else it will break for null https://github.com/FasterXML/jackson-module-kotlin/issues/130#issuecomment-546625625
        .configure(KotlinFeature.NullIsSameAsDefault, true)
        .build()

    @JvmStatic
    val XML_MAPPER = XmlMapper(JacksonXmlModule()).apply {
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .registerModule(kotlinModule)
            .registerModule(JavaTimeModule())
            .registerModule(JaxbAnnotationModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // to parse the dates as LocalDate, else parsing error
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    @JvmStatic
    fun unmarshallXml(xml: String): KostraBarnevernType =
        XML_MAPPER.readValue(xml, KostraBarnevernType::class.java)

    @JvmStatic
    fun marshallInstance(barnevernType: KostraBarnevernType): String =
        XML_MAPPER.writeValueAsString(barnevernType)
}