package no.ssb.kostra.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View
import io.swagger.v3.oas.annotations.Hidden

/**
 * Controller for serving React index.html
 */
@Hidden
@Controller("/")
class ViewsController {

    @View("index")
    @Get
    fun index(): HttpResponse<Any> = HttpResponse.ok()
}