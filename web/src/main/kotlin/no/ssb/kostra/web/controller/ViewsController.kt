package no.ssb.kostra.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View

/**
 * Controller for serving React index.html
 */
@Controller("/")
class ViewsController {

    @View("index")
    @Get
    fun index(): HttpResponse<Any> = HttpResponse.ok()
}