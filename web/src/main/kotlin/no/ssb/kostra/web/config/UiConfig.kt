package no.ssb.kostra.web.config

import io.micronaut.context.annotation.ConfigurationProperties
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm

@ConfigurationProperties("iu-innstillinger")
class UiConfig {
    var skjematyper: Collection<KostraFormTypeVm> = setOf()
}