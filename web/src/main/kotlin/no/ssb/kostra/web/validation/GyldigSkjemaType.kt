package no.ssb.kostra.web.validation

import javax.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class GyldigSkjemaType(
    val message: String = "Ugyldig skjematype ({validatedValue})" //
)