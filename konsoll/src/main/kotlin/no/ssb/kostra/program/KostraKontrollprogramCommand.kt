package no.ssb.kostra.program

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
    private var schema: String = "  "

    @Option(names = ["-y", "--year"], defaultValue = "    ", description = ["..."])
    private var year: String = " ".repeat(4)

    @Option(names = ["-q", "--quarter"], defaultValue = " ", description = ["..."])
    private var quarter: String = " "

    @Option(names = ["-r", "--region"], defaultValue = "      ", description = ["..."])
    private var region: String = " ".repeat(6)

    @Option(names = ["-n", "--name"], defaultValue = "Uoppgitt", description = ["..."])
    private var name: String = "Uoppgitt"

    @Option(names = ["-u", "--unit-orgnr"], defaultValue = "         ", description = ["..."])
    private var unitId: String = " ".repeat(9)

    @Option(names = ["-c", "--company-orgnr"], defaultValue = "         ", description = ["..."])
    private var companyId: String = " ".repeat(9)

    @Option(names = ["-a", "--attachment"], description = ["..."])
    private var hasAttachment: String = "1"

    @Option(names = ["-e", "--external-process"], description = ["..."])
    private var isRunAsExternalProcess: Boolean = false

    private var inputFileContent: String = " "

    override fun call(): Int {
        if (schema.isNotBlank() && hasAttachment == "1")
            inputFileContent = System.`in`.bufferedReader().use(BufferedReader::readText).trim { it <= ' ' }

        val kotlinArguments = KotlinArguments(
            skjema = schema,
            aargang = year,
            kvartal = quarter,
            region = region,
            navn = name,
            orgnr = unitId,
            foretaknr = companyId,
            harVedlegg = (hasAttachment == "1"),
            isRunAsExternalProcess = isRunAsExternalProcess,
            inputFileContent = inputFileContent,
        )
        val report = ControlDispatcher.validate(kotlinArguments)
        val errorTypeFound = report.errorType
        val printStream = PrintStream(System.out, true, StandardCharsets.ISO_8859_1)
        printStream.print(report.generateReport())
        return errorTypeFound
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val exitCode: Int = CommandLine(KostraKontrollprogramCommand()).execute(*args)
            exitProcess(exitCode)
        }
    }
}