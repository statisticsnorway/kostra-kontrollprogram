package no.ssb.kostra.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.CONFLICT
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.validation.validator.Validator
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.service.FileValidator
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import javax.validation.ConstraintViolationException


/**
 * API controller
 */
@Controller("/api")
open class ApiController(
    private val uiConfig: UiConfig,
    private val fileValidator: FileValidator,
    private val objectMapper: ObjectMapper,
    private val validator: Validator
) {

    @Post(
        value = "/kontroller-skjema",
        consumes = [MediaType.MULTIPART_FORM_DATA],
        produces = [MediaType.APPLICATION_JSON]
    )
    @SingleResult
    fun kontrollerSkjema(
        kostraFormAsJson: String,
        file: StreamingFileUpload
    ): Mono<HttpResponse<FileReportVm>> {

        /**  we'll have to deserialize and validate our self because of multipart request */
        val kostraForm = objectMapper.readValue(kostraFormAsJson, KostraFormVm::class.java)
        validator.validate(kostraForm).takeIf { it.isNotEmpty() }?.apply {
            throw ConstraintViolationException(iterator().asSequence().toSet())
        }

        /** target stream, file content will end up here */
        val outputStream = ByteArrayOutputStream()

        return Mono.from(file.transferTo(outputStream))
            .map { success: Boolean ->
                if (success) {
                    HttpResponse.ok(
                        fileValidator.validateDataFile(
                            kostraForm,
                            outputStream.toByteArray().inputStream()
                        )
                    )
                } else HttpResponse.status(CONFLICT)
            }
    }

    @Get(
        value = "/skjematyper",
        produces = [MediaType.APPLICATION_JSON]
    )
    fun skjematyper(): Collection<KostraFormTypeVm> = uiConfig.skjematyper
}