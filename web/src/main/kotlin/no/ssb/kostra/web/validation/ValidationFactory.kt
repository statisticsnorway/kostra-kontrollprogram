package no.ssb.kostra.web.validation

import io.micronaut.context.annotation.Factory
import io.micronaut.validation.validator.constraints.ConstraintValidator
import jakarta.inject.Singleton
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.viewmodel.KostraFormVm

@Factory
class ValidationFactory(private val uiConfig: UiConfig) {

    @Singleton
    fun gyldigSkjemaType(): ConstraintValidator<GyldigSkjemaType, String> = ConstraintValidator { value, _, context ->
        context.messageTemplate("Ugyldig skjematype ({validatedValue})")

        value.isNullOrBlank() || uiConfig.skjematyper.any { it.id == value }
    }

    @Singleton
    fun gyldigSkjema(): ConstraintValidator<GyldigSkjema, KostraFormVm> = ConstraintValidator { value, _, context ->

        if (value == null || value.skjema.isBlank()
            /** let dedicated validator handle invalid skjematype */
            || uiConfig.skjematyper.none { it.id == value.skjema }
        ) {
            return@ConstraintValidator true
        }

        val skjemaFraConfig = uiConfig.skjematyper.first { it.id == value.skjema }

        if (skjemaFraConfig.labelOrgnrVirksomhetene != null && value.orgnrVirksomhet == null) {
            context.messageTemplate("Skjema krever ett eller flere orgnr for virksomhet(er)")
            return@ConstraintValidator false
        }

        if (skjemaFraConfig.labelOrgnr != null && value.orgnrForetak == null) {
            context.messageTemplate("Skjema krever orgnr")
            return@ConstraintValidator false
        }

        true
    }
}