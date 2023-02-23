package no.ssb.kostra.web.controller

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@MicronautTest
class ViewsControllerTest(@Client("/") client: HttpClient) : BehaviorSpec({

    Given("index") {
        val request: HttpRequest<Any> = HttpRequest.GET("/")

        When("request") {
            val response = withContext(Dispatchers.IO) {
                client.toBlocking().exchange(request, Argument.of(Object::class.java))
            }

            Then("response should be OK") {
                response.status shouldBe HttpStatus.OK
            }
        }
    }
})
