package no.ssb.kostra.barnevern.convert

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.ssb.kostra.barnevern.xsd.KostraAvgiverType
import no.ssb.kostra.barnevern.xsd.KostraBarnevernType
import no.ssb.kostra.barnevern.xsd.KostraIndividType

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
            .registerModule(JakartaXmlBindAnnotationModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // to parse the dates as LocalDate, else parsing error
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    @JvmStatic
    fun marshallInstance(barnevernType: KostraBarnevernType): String =
        XML_MAPPER.writeValueAsString(barnevernType)

    @JvmStatic
    fun marshallInstance(avgiverType: KostraAvgiverType): String =
        XML_MAPPER.writeValueAsString(avgiverType)

    @JvmStatic
    fun marshallInstance(individType: KostraIndividType): String =
        XML_MAPPER.writeValueAsString(individType)
}