package no.ssb.kostra

import io.micronaut.context.ApplicationContextBuilder
import io.micronaut.context.ApplicationContextConfigurer
import io.micronaut.context.annotation.ContextConfigurer
import io.micronaut.core.annotation.NonNull
import no.ssb.kostra.CustomEnvironment.DEFAULT

@Suppress("unused")
@ContextConfigurer
class DefaultEnvironmentConfigurer : ApplicationContextConfigurer {
    override fun configure(@NonNull builder: ApplicationContextBuilder) {
        builder.defaultEnvironments(DEFAULT)
    }
}
