package no.ssb.kostra.program.util

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReportEntry
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry

object ConversionUtils {

    internal fun Severity.toInt() = when (this) {
        Severity.WARNING -> 1
        Severity.ERROR -> 2
        else -> 0
    }

    @JvmStatic
    fun toErrorReportEntry(reportEntry: ValidationReportEntry) = ErrorReportEntry(
        reportEntry.caseworker,
        reportEntry.journalId,
        reportEntry.individId,
        " ",
        reportEntry.ruleName,
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