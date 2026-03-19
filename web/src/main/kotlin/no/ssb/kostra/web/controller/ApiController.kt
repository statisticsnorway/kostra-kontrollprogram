package no.ssb.kostra.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.validation.validator.Validator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.ConstraintViolationException
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.program.ControlDispatcher
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.error.ApiError
import no.ssb.kostra.web.extensions.toAltinnReport
import no.ssb.kostra.web.service.ControlRunner
import no.ssb.kostra.web.viewmodel.AltinnReport
import no.ssb.kostra.web.viewmodel.AltinnRequest
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import no.ssb.kostra.web.viewmodel.UiDataVm
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.nio.charset.StandardCharsets

/**
 * API controller
 */
@Controller("/api")
open class ApiController(
    private val uiConfig: UiConfig,
    private val controlRunner: ControlRunner,
    private val objectMapper: ObjectMapper,
    private val validator: Validator,
    private val gitProperties: GitProperties
) {
    @Post(
        value = "/kontroller-skjema",
        consumes = [MediaType.MULTIPART_FORM_DATA],
        produces = [MediaType.APPLICATION_JSON]
    )
    @Operation(
        summary = "Runs controls on supplied data file/input parameters and returns a report"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = FileReportVm::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ApiError::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "System error",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ApiError::class))]
        )
    )
    @SingleResult
    fun kontrollerSkjema(
        kostraFormAsJson: String,
        file: StreamingFileUpload
    ): Mono<HttpResponse<FileReportVm>> {

        /**  we'll have to deserialize and validate our self because of multipart request */
        val kostraForm = objectMapper.readValue<KostraFormVm>(kostraFormAsJson)
        validator.validate(kostraForm).takeIf { it.isNotEmpty() }?.apply {
            throw ConstraintViolationException(iterator().asSequence().toSet())
        }

        ByteArrayOutputStream().use { outputStream ->
            return Mono.from(file.transferTo(outputStream)).handle { success, sink ->
                if (success) sink.next(
                    HttpResponse.ok(
                        controlRunner.runControls(
                            kostraForm,
                            outputStream.toByteArray().inputStream()
                        )
                    )
                ) else sink.error(RuntimeException("Failed to read file at backend"))
            }
        }
    }

    @Get(
        value = "/ui-data",
        produces = [MediaType.APPLICATION_JSON]
    )
    @Operation(
        summary = "Get UI-data required by the frontend app",
        description = "Returns a view model (DTO) containing years, release version and " +
                "form types required by the frontend app"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UiDataVm::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "System error",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = Schema(implementation = ApiError::class)
            )]
        )
    )
    fun uiData(): UiDataVm = UiDataVm(
        releaseVersion = gitProperties.tags,
        years = uiConfig.aarganger,
        formTypes = uiConfig.skjematyper
    )

    @Post(
        value = "/kontroller-altinn-skjema",
        consumes = [MediaType.MULTIPART_FORM_DATA],
        produces = [MediaType.APPLICATION_JSON]
    )
    @Operation(
        summary = "Runs controls on supplied input parameters and returns a report"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = AltinnReport::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ApiError::class))]
        ),
        ApiResponse(
            responseCode = "500",
            description = "System error",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ApiError::class))]
        )
    )
    @SingleResult
    fun kontrollerAltinnSkjema(
        @Body request: AltinnRequest
    ): Mono<HttpResponse<AltinnReport>> {

        return Mono.fromCallable {
            // Decode Base64 file content
            val inputFileContent = String(
                Base64.getDecoder().decode(request.base64EncodedFileAttachment),
                StandardCharsets.ISO_8859_1
            )

            HttpResponse.ok(
                ControlDispatcher.validate(
                    KotlinArguments(
                        aargang = request.period.toString(),
                        kvartal = request.quarter,
                        skjema = request.formId,
                        region = request.region,
                        navn = request.name ?: "Ukjent navn",
                        orgnr = request.organizationId ?: " ".repeat(9),
                        inputFileContent = inputFileContent,


                    )
                ).toAltinnReport()
            )
        }
    }
}