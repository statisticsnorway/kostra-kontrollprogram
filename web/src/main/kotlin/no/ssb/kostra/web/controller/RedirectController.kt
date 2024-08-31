package no.ssb.kostra.web.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces


//@Controller
//class RedirectController {
//    /**
//     * Handles requests that are supposed to be handled by the frontend.
//     * For example, http://localhost:8080/user1.
//     *
//     * @return forward to index.html
//     */
//    @Get("/{path:[^.]*}")
//    @Produces(MediaType.TEXT_HTML)
//    fun redirect(path: String): String {
//        return "forward:/"
//    }
//}