package no.ssb.kostra.program

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(
    name = "kostra-kontrollprogram-konsoll", description = ["..."],
    mixinStandardHelpOptions = true
)
class KostraKontrollprogramCommand : Runnable {

    @Inject
    lateinit var validator: Validator

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose: Boolean = false

    override fun run() {
        // business logic here
        if (verbose) {
            println("Hi!")

            val validationErrors = validator.validate(MyTestDto(name = "", aar = 2021))
            println(validationErrors)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(KostraKontrollprogramCommand::class.java, *args)
        }
    }

    @Introspected
    private data class MyTestDto(
        @field:NotBlank val
        name: String,

        @field:Min(value = 2022, message = "År kan ikke være mindre enn {value}")
        val aar: Int
    )
}
