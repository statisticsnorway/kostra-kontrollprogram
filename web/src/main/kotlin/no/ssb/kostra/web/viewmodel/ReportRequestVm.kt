package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

@Introspected
data class ReportRequestVm(
    val aar: Int,
    val skjema: String,
    val region: String,
    val organisasjon: String?,
    val base64EncodedContent: String? = null
) {
    val contentAsInputStream: InputStream
        get() = ByteArrayInputStream(Base64.getDecoder().decode(base64EncodedContent))
}
