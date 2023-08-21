package no.ssb.kostra.felles.git

import java.util.*

object GitPropertiesLoader {
    const val DEFAULT_GIT_PROPERTIES_FILENAME = "/git.properties"
    const val NOT_AVAILABLE_VALUE = "N/A"
    private const val GIT_TAGS_KEY = "git.tags"

    @JvmStatic
    fun loadGitProperties(gitPropertiesFilename: String): GitProperties =
        this::class.java.getResourceAsStream(gitPropertiesFilename)?.use {
            GitProperties(tags = Properties().apply { load(it) }.getProperty(GIT_TAGS_KEY, NOT_AVAILABLE_VALUE))
        } ?: GitProperties(tags = NOT_AVAILABLE_VALUE)
}