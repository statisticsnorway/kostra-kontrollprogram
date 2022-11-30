package no.ssb.kostra.web.status

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.ssb.kostra.web.status.GitPropertiesLoader.Companion.GIT_PROPERTIES_FILENAME
import no.ssb.kostra.web.status.GitPropertiesLoader.Companion.NOT_AVAILABLE_VALUE
import no.ssb.kostra.web.status.GitPropertiesLoader.Companion.initGitProperties

class GitPropertiesLoaderTest : BehaviorSpec({

    given("initGitProperties") {

        `when`("non-existing gitPropertiesFilename") {

            val result = initGitProperties("non-existing.properties")

            then("result should be fallback") {
                result shouldBe GitProperties(
                    tags = NOT_AVAILABLE_VALUE
                )
            }
        }

        `when`("existing gitPropertiesFilename") {

            val result = initGitProperties(GIT_PROPERTIES_FILENAME)

            then("result should not be fallback") {
                result shouldNotBe GitProperties(
                    tags = NOT_AVAILABLE_VALUE
                )
            }
        }
    }
})
