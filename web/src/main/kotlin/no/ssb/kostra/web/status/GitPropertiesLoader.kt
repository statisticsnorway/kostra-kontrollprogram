package no.ssb.kostra.web.status

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.util.*

@Factory
class GitPropertiesLoader {

    /**
     * Expose GitProperties as singleton, as reading from git.properties
     * is expensive and will not change during runtime
     */
    @Singleton
    fun gitProperties(): GitProperties = initGitProperties(GIT_PROPERTIES_FILENAME)

    companion object {
        internal const val GIT_PROPERTIES_FILENAME = "/git.properties"
        private const val GIT_TAGS_KEY = "git.tags"
        private const val NOT_AVAILABLE_VALUE = "N/A"

        internal fun initGitProperties(gitPropertiesFilename: String): GitProperties {
            val gitProperties = this::class.java.getResourceAsStream(gitPropertiesFilename).use {
                GitProperties(tags = Properties().apply { load(it) }.getProperty(GIT_TAGS_KEY, NOT_AVAILABLE_VALUE))
            }

            return when {
                gitProperties.tags.isEmpty() -> GitProperties(tags = NOT_AVAILABLE_VALUE)
                else -> gitProperties
            }
        }
    }
}
