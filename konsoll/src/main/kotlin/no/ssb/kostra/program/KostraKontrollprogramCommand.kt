package no.ssb.kostra.program

import no.ssb.kostra.validation.report.ValidationReport
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.BufferedReader
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(
    name = "kostra-kontrollprogram-konsoll", description = ["..."],
    mixinStandardHelpOptions = true
)
class KostraKontrollprogramCommand : Callable<Int> {

    @Option(names = ["-s", "--schema"], defaultValue = "  ", description = ["..."])
    private var schema: String = BLANK_CHAR.repeat(2)

    @Option(names = ["-y", "--year"], defaultValue = "    ", description = ["..."])
    private var year: String = BLANK_CHAR.repeat(4)

    @Option(names = ["-q", "--quarter"], defaultValue = " ", description = ["..."])
    private var quarter: String = BLANK_CHAR

    @Option(names = ["-r", "--region"], defaultValue = "      ", description = ["..."])
    private var region: String = BLANK_CHAR.repeat(6)

    @Option(names = ["-n", "--name"], defaultValue = "Uoppgitt", description = ["..."])
    private var name: String = "Uoppgitt"

    @Option(names = ["-u", "--unit-orgnr"], defaultValue = "         ", description = ["..."])
    private var unitId: String = BLANK_CHAR.repeat(9)

    @Option(names = ["-c", "--company-orgnr"], defaultValue = "         ", description = ["..."])
    private var companyId: String = BLANK_CHAR.repeat(9)

    @Option(names = ["-a", "--attachment"], description = ["..."])
    private var hasAttachment: String = "1"

    @Option(names = ["-e", "--external-process"], description = ["..."])
    private var isRunAsExternalProcess: Boolean = false

    override fun call(): Int = ControlDispatcher.validate(
        KotlinArguments(
            skjema = schema,
            aargang = year,
            kvartal = quarter,
            region = region,
            navn = name,
            orgnr = unitId,
            foretaknr = companyId,
            harVedlegg = (hasAttachment == "1"),
            isRunAsExternalProcess = isRunAsExternalProcess,
            inputFileContent =
                if (schema.isNotBlank() && hasAttachment == "1")
                    System.`in`.bufferedReader().use(BufferedReader::readText)
                else
                    BLANK_CHAR
        )
    ).let { validationReportArguments ->
        PrintStream(System.out, true, StandardCharsets.ISO_8859_1).use { printStream ->
            printStream.print(ValidationReport(validationReportArguments))
        }
        validationReportArguments.validationResult.severity.info.returnCode
    }

    companion object {
        private const val BLANK_CHAR = " "

        @JvmStatic
        fun main(args: Array<String>) {
            val exitCode: Int = CommandLine(KostraKontrollprogramCommand()).execute(*args)
            exitProcess(exitCode)
        }
    }
}