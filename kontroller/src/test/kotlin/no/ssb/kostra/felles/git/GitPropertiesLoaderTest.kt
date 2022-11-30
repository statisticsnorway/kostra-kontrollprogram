package no.ssb.kostra.felles.git

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.ssb.kostra.felles.git.GitPropertiesLoader.DEFAULT_GIT_PROPERTIES_FILENAME
import no.ssb.kostra.felles.git.GitPropertiesLoader.NOT_AVAILABLE_VALUE
import no.ssb.kostra.felles.git.GitPropertiesLoader.loadGitProperties

class GitPropertiesLoaderTest : BehaviorSpec({

    given("initGitProperties") {

        `when`("non-existing gitPropertiesFilename") {

            val result = loadGitProperties("non-existing.properties")

            then("result should be fallback") {
                result shouldBe GitProperties(
                    tags = NOT_AVAILABLE_VALUE
                )
            }
        }

        `when`("existing gitPropertiesFilename") {

            val result = loadGitProperties(DEFAULT_GIT_PROPERTIES_FILENAME)

            then("result should not be fallback") {
                result shouldNotBe GitProperties(
                    tags = NOT_AVAILABLE_VALUE
                )
            }
        }
    }
})
