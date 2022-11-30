package no.ssb.kostra.web.status

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.felles.git.GitPropertiesLoader.DEFAULT_GIT_PROPERTIES_FILENAME
import no.ssb.kostra.felles.git.GitPropertiesLoader.loadGitProperties

@Factory
class GitPropertiesFactory {

    /**
     * Expose GitProperties as singleton, as reading from git.properties
     * is expensive and will not change during runtime
     */
    @Singleton
    fun gitProperties(): GitProperties = loadGitProperties(DEFAULT_GIT_PROPERTIES_FILENAME)
}
