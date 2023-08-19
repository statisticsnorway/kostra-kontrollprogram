package no.ssb.kostra.common

import io.micronaut.context.ApplicationContextBuilder
import io.micronaut.context.ApplicationContextConfigurer
import io.micronaut.context.annotation.ContextConfigurer
import io.micronaut.context.env.Environment.CLI
import io.micronaut.core.annotation.NonNull

@Suppress("unused")
@ContextConfigurer
class DefaultEnvironmentConfigurer : ApplicationContextConfigurer {
    override fun configure(@NonNull builder: ApplicationContextBuilder) {
        builder.defaultEnvironments(CLI)
    }
}
