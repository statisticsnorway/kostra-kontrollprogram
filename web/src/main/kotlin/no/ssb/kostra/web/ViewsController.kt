package no.ssb.kostra.web

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.View

@Controller("/")
class ViewsController {

    @View("index")
    @Get
    fun index(): HttpResponse<Any> = HttpResponse.ok()
}