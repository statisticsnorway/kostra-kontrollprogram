package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import java.io.InputStream

interface BarnevernStreamHandler {

    fun handleStream(
        fileStream: InputStream,
        arguments: KotlinArguments,
        individCallbackFunc: (KostraIndividType) -> Unit
    ): List<ValidationReportEntry>

    val seenAvgivere: Int
    val seenIndivider: Int
}