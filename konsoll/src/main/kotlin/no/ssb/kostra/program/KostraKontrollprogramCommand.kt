package no.ssb.kostra.program

import no.ssb.kostra.validation.report.StructuredValidationReport
import no.ssb.kostra.validation.report.ValidationReport
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.ParseResult
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec
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
    private var companyId: String = BLANK_CHAR.repeat(9)

    @Option(names = ["-a", "--attachment"], description = ["..."])
    private var hasAttachment: String = "1"

    @Option(names = ["-f", "--output-format"], defaultValue = "HTML", description = ["..."])
    private var outputFormat: String = "HTML"

    @Option(names = ["-c", "--output-charset"], description = ["..."])
    private var outputCharset: String = "ISO-8859-1"

    @Option(names = ["-e", "--external-process"], description = ["..."])
    private var isRunAsExternalProcess: Boolean = false

    @Option(names = ["-v", "--verbose"], description = ["..."])
    private var verbose: Boolean = false

    @Spec
    lateinit var spec: CommandSpec

    override fun call(): Int {
        if (verbose) dumpParamsToOutput()

        when (outputCharset) {
            "ISO-8859-1", "LATIN1" -> outputCharset = StandardCharsets.ISO_8859_1.name()
            "UTF-8", "UTF8" -> outputCharset = StandardCharsets.UTF_8.name()
            else -> {
                System.err.println("Ukjent tegnsett for output: $outputCharset. Bruk ISO-8859-1 eller UTF-8.")
                return -1
            }
        }

        ControlDispatcher.validate(
            KotlinArguments(
                skjema = schema,
                aargang = year,
                kvartal = quarter,
                region = region,
                navn = name,
                orgnr = companyId,
                harVedlegg = (hasAttachment == "1"),
                outputFormat = outputFormat,
                isRunAsExternalProcess = isRunAsExternalProcess,
                inputFileContent =
                    if (schema.isNotBlank() && hasAttachment == "1")
                        System.`in`.bufferedReader().use(BufferedReader::readText)
                    else
                        BLANK_CHAR
            )
        ).let { validationReportArguments ->
            if (outputFormat == "HTML") {
                PrintStream(System.out, true, outputCharset).use { printStream ->
                    printStream.print(ValidationReport(validationReportArguments))
                }
                return validationReportArguments.validationResult.severity.info.returnCode
            } else if (outputFormat == "JSON") {
                PrintStream(System.out, true, outputCharset).use { printStream ->
                    printStream.print(StructuredValidationReport(validationReportArguments))
                }
                return validationReportArguments.validationResult.severity.info.returnCode

            } else {
                System.err.println("Ukjent output format: $outputFormat. Bruk HTML eller JSON.")
                -1
            }
        }
        return 0
    }

    private fun dumpParamsToOutput() {
        val parseResult: ParseResult = spec.commandLine().parseResult

        for (option in spec.options()) {
            val name: String = option.longestName()
            println("$name was specified: ${parseResult.hasMatchedOption(option)}")
            println(
                "$name=${parseResult.matchedOptionValue(name, -1)} " +
                        "(-1 means this option was not matched on command line)"
            )
            System.out.printf("%s=%s (arg value or default)%n", name, option.getValue())
            println()
        }
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
