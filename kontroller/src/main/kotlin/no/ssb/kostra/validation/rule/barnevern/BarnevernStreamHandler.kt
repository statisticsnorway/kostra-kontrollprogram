package no.ssb.kostra.validation.rule.barnevern

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import java.io.InputStream

interface BarnevernStreamHandler {

    fun handleStream(
        fileStream: InputStream,
        arguments: KotlinArguments,
        incrementAvgiverCount: () -> Unit,
        incrementIndividCount: () -> Unit,
        fodselsnummerAndJournalIdFunc: (String, String) -> Unit
    ): List<ValidationReportEntry>
}