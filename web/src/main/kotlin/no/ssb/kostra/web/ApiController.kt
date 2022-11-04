package no.ssb.kostra.web

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import no.ssb.kostra.web.service.ValidatorSvc
import no.ssb.kostra.web.viewmodel.ReportRequestVm
import javax.validation.Valid

/**
 * API controller
 */
@Controller("/api")
open class ApiController(private val validatorSvc: ValidatorSvc) {

    @Post(
        value = "/validate",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    open fun validateInput(@Valid reportRequest: ReportRequestVm): HttpResponse<Any> =
        HttpResponse.ok(validatorSvc.validateInput(reportRequest))
}