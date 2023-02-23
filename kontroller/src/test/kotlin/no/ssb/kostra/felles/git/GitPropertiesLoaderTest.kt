package no.ssb.kostra.felles.git

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.ssb.kostra.felles.git.GitPropertiesLoader.DEFAULT_GIT_PROPERTIES_FILENAME
import no.ssb.kostra.felles.git.GitPropertiesLoader.NOT_AVAILABLE_VALUE
import no.ssb.kostra.felles.git.GitPropertiesLoader.loadGitProperties

class GitPropertiesLoaderTest : BehaviorSpec({

    Given("initGitProperties") {

        When("non-existing gitPropertiesFilename") {

            val result = loadGitProperties("non-existing.properties")

            Then("result should be fallback") {
                result shouldBe GitProperties(
                    tags = NOT_AVAILABLE_VALUE
                )
            }
        }

        When("existing gitPropertiesFilename") {

            val result = loadGitProperties(DEFAULT_GIT_PROPERTIES_FILENAME)

            Then("result should not be fallback") {
                result shouldNotBe GitProperties(
                    tags = NOT_AVAILABLE_VALUE
                )
            }
        }
    }
})
