package no.ssb.kostra.web.validation

import javax.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class GyldigSkjema(
    val message: String = "Ugyldig skjema ({validatedValue})" //
)