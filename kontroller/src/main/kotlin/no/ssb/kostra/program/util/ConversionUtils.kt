package no.ssb.kostra.program.util

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReportEntry
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toInt
import no.ssb.kostra.validation.report.ValidationReportEntry

object ConversionUtils {

    @JvmStatic
    fun toErrorReportEntry(reportEntry: ValidationReportEntry): ErrorReportEntry = ErrorReportEntry(
        reportEntry.caseworker,
        reportEntry.journalId,
        reportEntry.individId,
        " ",
        reportEntry.ruleName,
        when (reportEntry.lineNumbers.any()) {
            true -> "${reportEntry.messageText} Gjelder linjenr: ${reportEntry.lineNumbers.joinToString(", ")}"
            else -> reportEntry.messageText
        },
        reportEntry.severity.toInt()
    )

    @JvmStatic
    fun fromArguments(
        arguments: Arguments,
        contentAsString: Boolean
    ) = KotlinArguments(
        skjema = arguments.skjema,
        aargang = arguments.aargang,
        kvartal = arguments.kvartal,
        region = arguments.region,
        navn = arguments.navn,
        orgnr = arguments.orgnr,
        foretaknr = arguments.foretaknr,
        harVedlegg = arguments.harVedlegg(),
        isRunAsExternalProcess = arguments.isRunAsExternalProcess,
        inputFileContent = if (contentAsString) arguments.inputContentAsStringList.joinToString("\n") else ""
    )
}
