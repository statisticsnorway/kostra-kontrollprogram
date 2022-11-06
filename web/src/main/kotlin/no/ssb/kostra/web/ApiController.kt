package no.ssb.kostra.web

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.service.ValidatorSvc
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import javax.validation.Valid

/**
 * API controller
 */
@Controller("/api")
open class ApiController(
    private val uiConfig: UiConfig,
    private val validatorSvc: ValidatorSvc
) {

    @Get(
        value = "/skjematyper",
        produces = [MediaType.APPLICATION_JSON]
    )
    fun skjematyper(): Collection<KostraFormTypeVm> = uiConfig.skjematyper

    @Post(
        value = "/kontroller-skjema",
        consumes = [MediaType.APPLICATION_JSON],
        produces = [MediaType.APPLICATION_JSON]
    )
    open fun kontrollerSkjema(@Valid kostraForm: KostraFormVm) = validatorSvc.validateInput(kostraForm)
}