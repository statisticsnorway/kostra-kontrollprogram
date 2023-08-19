package no.ssb.kostra.program

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.validation.validator.Validator
import jakarta.inject.Inject
import no.ssb.kostra.common.extensions.toKostraArguments
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.common.viewmodel.CompanyIdVm
import no.ssb.kostra.common.viewmodel.KostraFormVm
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.time.Year

@Command(
    name = "kostra-kontrollprogram-konsoll", description = ["..."],
    mixinStandardHelpOptions = true
)
class KostraKontrollprogramCommand : Runnable {

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose: Boolean = false

    @Inject
    lateinit var validator: Validator

    @Inject
    lateinit var gitProperties: GitProperties // inneholder versjonsinformasjon

    override fun run() {
        // business logic here
        if (verbose) {
            println("Hi!")
        }

        println(gitProperties)

        /** TODO Jon Ole: Putt inn verdier fra argumentene her  */
        val kostraForm = KostraFormVm( // will pass validation
            aar = Year.now().value,
            skjema = "0G",
            region = "667600",
            orgnrForetak = "987654321",
            orgnrVirksomhet = listOf(CompanyIdVm("987654321")),
            filnavn = "0G.dat"
        )

        val validationErrors: Map<String, String> = validator.validate(kostraForm).iterator().asSequence().plus(
            /** temp-fix for issues with MN:4.x and validating collections */
            kostraForm.orgnrVirksomhet.flatMap { companyId ->
                validator.validate(companyId).iterator().asSequence()
            }
        ).associate { (it.propertyPath.lastOrNull()?.name ?: "MISSING") to it.message }

        if (validationErrors.any()) {
            /** TODO Jon Ole: Presenter validationErrors her  */
            println(validationErrors)
        } else {
            /** TODO #1 Jon Ole: Putt inn verdier fra argumentene her  */
            /** TODO #2 Jon Ole: Se på om toKostraArguments har nok kode */
            /** TODO #3 Jon Ole: Sjekk om fil er input stream */
            val errorReport: ErrorReport =
                ControlDispatcher.validate(kostraForm.toKostraArguments(PLAIN_TEXT_0G.byteInputStream()))

            /** TODO Jon Ole: Presenter ErrorReport her  */
            println(errorReport.generateReport())
        }
    }

    companion object {

        /** DUMMY DATA */
        private const val PLAIN_TEXT_0G =
            "0G2020 300500976989732         510  123      263\n0G2020 300500976989732         510           263"


        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(KostraKontrollprogramCommand::class.java, *args)
        }
    }
}
