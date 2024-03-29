package no.ssb.kostra.web.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm

@Context
@ConfigurationProperties("iu-innstillinger")
data class UiConfig(
    var aarganger: List<Int> = emptyList(),
    var skjematyper: List<KostraFormTypeVm> = emptyList()
)