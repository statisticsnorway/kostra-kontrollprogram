package no.ssb.kostra.program

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReportEntry
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

object ConversionUtils {

    private fun Severity.toInt() = when (this) {
        Severity.WARNING -> 1
        Severity.ERROR -> 2
        else -> 0
    }

    @JvmStatic
    fun toErrorReportEntry(reportEntry: ValidationReportEntry) = ErrorReportEntry(
        " ",
        reportEntry.journalId,
        " ",
        " ",
        "Kontrollprogrammet",
        reportEntry.messageText,
        reportEntry.severity.toInt()
    )

    @JvmStatic
    fun fromArguments(args: Arguments) = KotlinArguments(
        skjema = args.skjema,
        aargang = args.aargang,
        kvartal = args.kvartal,
        region = args.region,
        navn = args.navn,
        orgnr = args.orgnr,
        foretaknr = args.foretaknr,
        inputFileStream = args.inputContentAsInputStream
    )
}