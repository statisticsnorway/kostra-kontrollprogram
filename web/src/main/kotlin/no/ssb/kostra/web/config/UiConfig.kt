package no.ssb.kostra.web.config

import io.micronaut.context.annotation.ConfigurationProperties
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm

@ConfigurationProperties("iu-innstillinger")
class UiConfig {
    var aarganger: Collection<Int> = setOf()
    var skjematyper: Collection<KostraFormTypeVm> = setOf()
}