package no.ssb.kostra.common.validation

import io.micronaut.context.annotation.Factory
import io.micronaut.validation.validator.constraints.ConstraintValidator
import jakarta.inject.Singleton
import no.ssb.kostra.common.config.UiConfig
import no.ssb.kostra.common.viewmodel.KostraFormVm

@Factory
class ValidatorFactory(private val uiConfig: UiConfig) {

    @Singleton
    fun validFormType(): ConstraintValidator<ValidFormType, String> = ConstraintValidator { value, _, context ->
        uiConfig.skjematyper.any { it.id == value }.also {
            if (!it) context.messageTemplate("Ugyldig skjematype ({validatedValue})")
        }
    }

    @Singleton
    fun validForm(): ConstraintValidator<ValidForm, KostraFormVm> = ConstraintValidator { value, _, context ->
        uiConfig.skjematyper.firstOrNull { it.id == value.skjema }?.let { formTypeFromConfig ->
            when {
                formTypeFromConfig.labelOrgnrVirksomhetene != null && value.orgnrVirksomhet.isEmpty() -> {
                    context.messageTemplate("Skjema krever ett eller flere orgnr for virksomhet(er)")
                    false
                }

                formTypeFromConfig.labelOrgnr != null && value.orgnrForetak.isNullOrEmpty() -> {
                    context.messageTemplate("Skjema krever orgnr")
                    false
                }

                else -> true
            }
        } ?: true // leave validation of invalid form type to dedicated validators
    }
}