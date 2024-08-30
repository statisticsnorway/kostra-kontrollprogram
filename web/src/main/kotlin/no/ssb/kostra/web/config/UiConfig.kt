package no.ssb.kostra.web.config

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm
import java.time.LocalDate

@Context
@ConfigurationProperties("iu-innstillinger")
data class UiConfig(
    var aarganger: List<Int> = listOf(LocalDate.now().minusYears(1).year, LocalDate.now().year),
    var skjematyper: List<KostraFormTypeVm> = emptyList()
)