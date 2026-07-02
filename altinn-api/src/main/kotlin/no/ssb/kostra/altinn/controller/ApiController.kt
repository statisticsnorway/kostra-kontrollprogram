package no.ssb.kostra.altinn.controller

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import no.ssb.kostra.altinn.converter.toAltinnReport
import no.ssb.kostra.altinn.error.ApiError
import no.ssb.kostra.altinn.model.AltinnRapport
import no.ssb.kostra.altinn.model.AltinnRequest
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.program.ControlDispatcher
import no.ssb.kostra.program.KotlinArguments
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * API controller
 */
@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
open class ApiController(
    private val gitProperties: GitProperties
) {
    @Post(
        value = "/kontroller-altinn-skjema",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    @Operation(
        summary = "Runs controls on supplied input parameters and returns a report"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "OK",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = AltinnRapport::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ApiError::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "System error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ApiError::class)
            )]
        )
    )
    @SingleResult
    fun kontrollerAltinnSkjema(
        @Body request: AltinnRequest
    ): Mono<HttpResponse<AltinnRapport>> {

        return Mono.fromCallable {
            if (request.base64KodedeData.trim().isBlank()) {
                throw HttpStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Filvedlegget kan ikke være tomt"
                )
            }

            // Decode Base64 file content
            val inputFileContent = String(
                Base64.getDecoder().decode(request.base64KodedeData),
                StandardCharsets.ISO_8859_1
            )

            HttpResponse.ok(
                ControlDispatcher.validate(
                    KotlinArguments(
                        aargang = request.respondent.aar.toString(),
                        kvartal = request.respondent.kvartal,
                        skjema = request.respondent.skjema,
                        region = request.respondent.region,
                        orgnr = request.respondent.orgnr,
                        inputFileContent = inputFileContent,


                        )
                ).toAltinnReport(gitProperties.tags)
            )
        }
    }
}