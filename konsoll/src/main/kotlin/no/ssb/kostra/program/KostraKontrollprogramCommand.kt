package no.ssb.kostra.program

import io.micronaut.configuration.picocli.PicocliRunner

import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "kostra-kontrollprogram-konsoll", description = ["..."],
        mixinStandardHelpOptions = true)
class KostraKontrollprogramCommand : Runnable {

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose : Boolean = false

    override fun run() {
        // business logic here
        if (verbose) {
            println("Hi!")
        }
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.run(KostraKontrollprogramCommand::class.java, *args)
        }
    }
}
