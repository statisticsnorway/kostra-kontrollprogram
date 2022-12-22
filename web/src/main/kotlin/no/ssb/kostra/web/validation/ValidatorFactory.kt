package no.ssb.kostra.web.validation

import io.micronaut.context.annotation.Factory
import io.micronaut.validation.validator.constraints.ConstraintValidator
import jakarta.inject.Singleton
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.viewmodel.KostraFormVm

@Factory
class ValidatorFactory(private val uiConfig: UiConfig) {

    @Singleton
    fun validFormType(): ConstraintValidator<ValidFormType, String> = ConstraintValidator { value, _, context ->
        context.messageTemplate("Ugyldig skjematype ({validatedValue})")
        value.isNullOrBlank() || uiConfig.skjematyper.any { it.id == value }
    }

    @Singleton
    fun validForm(): ConstraintValidator<ValidForm, KostraFormVm> = ConstraintValidator { value, _, context ->

        /** leave validation to dedicated validators */
        if (value == null
            || value.skjema.isNullOrBlank()
            || uiConfig.skjematyper.none { it.id == value.skjema }
        ) return@ConstraintValidator true

        val formTypeFromConfig = uiConfig.skjematyper.first { it.id == value.skjema }

        if (formTypeFromConfig.labelOrgnrVirksomhetene != null && value.orgnrVirksomhet?.isEmpty() != false) {
            context.messageTemplate("Skjema krever ett eller flere orgnr for virksomhet(er)")
            return@ConstraintValidator false
        }

        if (formTypeFromConfig.labelOrgnr != null && value.orgnrForetak == null) {
            context.messageTemplate("Skjema krever orgnr")
            return@ConstraintValidator false
        }

        true
    }
}