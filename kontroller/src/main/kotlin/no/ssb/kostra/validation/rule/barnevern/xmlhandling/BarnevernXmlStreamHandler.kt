package no.ssb.kostra.validation.rule.barnevern.xmlhandling

import no.ssb.kostra.barnevern.xsd.KostraAvgiverType
import no.ssb.kostra.barnevern.xsd.KostraIndividType
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.ValidationReportEntry
import java.io.InputStream

fun interface BarnevernXmlStreamHandler {

    fun handleStream(
        fileStream: InputStream,
        arguments: KotlinArguments,
        avgiverCallbackFunc: (KostraAvgiverType) -> Unit,
        individCallbackFunc: (KostraIndividType) -> Unit
    ): List<ValidationReportEntry>
}