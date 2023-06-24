package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barn.xsd.KostraAvgiverType
import no.ssb.kostra.barn.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import java.io.InputStream

fun interface BarnevernStreamHandler {

    fun handleStream(
        fileStream: InputStream,
        arguments: KotlinArguments,
        avgiverCallbackFunc: (KostraAvgiverType) -> Unit,
        individCallbackFunc: (KostraIndividType) -> Unit
    ): List<ValidationReportEntry>
}