package no.ssb.kostra.program

import no.ssb.kostra.controlprogram.ArgumentConstants
import no.ssb.kostra.controlprogram.GetOpt
import no.ssb.kostra.controlprogram.GetOptDesc
import no.ssb.kostra.felles.Constants
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class CLI {
    private var skjema = " "
    private var aargang = "    "
    private var kvartal = " "
    private var region = "      "
    private var navn = "Uoppgitt"
    private var orgnr = "         "
    private var foretaknr = "         "
    private var harVedlegg = true
    private var isRunAsExternalProcess = false
    private var inputFileContent: String = " "
    fun main(args: Array<String>) {
        var errorTypeFound: Int
        try {
            val options = arrayOf(
                GetOptDesc(ArgumentConstants.SCHEMA_ABBR, "schema", true),
                GetOptDesc(ArgumentConstants.YEAR_ABBR, "year", true),
                GetOptDesc(ArgumentConstants.QUARTER_ABBR, "quarter", true),
                GetOptDesc(ArgumentConstants.REGION_ABBR, "region", true),
                GetOptDesc(ArgumentConstants.NAME_ABBR, "name", true),
                GetOptDesc(ArgumentConstants.UNIT_ORGNR_ABBR, "unit-orgnr", true),
                GetOptDesc(ArgumentConstants.COMPANY_ORGNR_ABBR, "company-orgnr", true),
                GetOptDesc(ArgumentConstants.ATTACHMENT_ABBR, "attachment", true),
                GetOptDesc(ArgumentConstants.EXTERNAL_PROCESS_ABBR, "external-process", true)
            )

            val parser = GetOpt(options)
            val optionsFound: Map<*, *> = parser.parseArguments(args)
            for (o in optionsFound.keys) {
                val key = o as String
                when (val c = key[0]) {
                    ArgumentConstants.SCHEMA_ABBR -> skjema = (optionsFound[key] as String?).toString()
                    ArgumentConstants.YEAR_ABBR -> aargang = (optionsFound[key] as String?).toString()
                    ArgumentConstants.QUARTER_ABBR -> kvartal = (optionsFound[key] as String?).toString()
                    ArgumentConstants.REGION_ABBR -> region = (optionsFound[key] as String?).toString()
                    ArgumentConstants.NAME_ABBR -> navn = (optionsFound[key] as String?).toString()
                    ArgumentConstants.UNIT_ORGNR_ABBR -> orgnr = (optionsFound[key] as String?).toString()
                    ArgumentConstants.COMPANY_ORGNR_ABBR -> foretaknr = (optionsFound[key] as String?).toString()
                    ArgumentConstants.ATTACHMENT_ABBR -> {
                        val vedlegg = optionsFound[key] as String?
                        harVedlegg = vedlegg.equals("1", ignoreCase = true)
                    }

                    ArgumentConstants.EXTERNAL_PROCESS_ABBR -> {
                        val process = optionsFound[key] as String?
                        isRunAsExternalProcess = process.equals("1", ignoreCase = true)
                    }

                    else -> {
                        System.err.println("Unexpected option character: $c")
                        System.err.println("Usage: GetOptDemo [-n][-o file][file...]")
                    }
                }
            }


            val arguments = Arguments(
                skjema,
                aargang,
                kvartal,
                region,
                navn,
                orgnr,
                foretaknr,
                harVedlegg,
                isRunAsExternalProcess,
                inputFileContent
            )

            val report = ControlDispatcher.doControls(arguments)
            errorTypeFound = report.severity.ordinal
            val printStream = PrintStream(System.out, true, StandardCharsets.ISO_8859_1)
            printStream.print(report.generateReport())
        } catch (e: IllegalArgumentException) {
            println(e.message)
            errorTypeFound = Constants.PARAMETER_ERROR
        } catch (e: NullPointerException) {
            println(e.message)
            errorTypeFound = Constants.CRITICAL_ERROR
        } catch (e: Exception) {
            println(e.message)
            errorTypeFound = Constants.SYSTEM_ERROR
        }
        exitProcess(errorTypeFound)
    }
}