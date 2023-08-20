package no.ssb.kostra.web.viewmodel

import io.micronaut.core.annotation.Introspected

@Introspected
enum class KostraErrorCode {
    NO_ERROR,
    NORMAL_ERROR,
    CRITICAL_ERROR,
    SYSTEM_ERROR,
    PARAMETER_ERROR
}