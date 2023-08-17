package no.ssb.kostra.program

import no.ssb.kostra.area.famvern.famvern52a.Familievern52aMain
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bMain
import no.ssb.kostra.area.famvern.famvern53.Familievern53Main
import no.ssb.kostra.area.famvern.famvern55.Familievern55Main
import no.ssb.kostra.area.regnskap.helseforetak.HelseForetakMain
import no.ssb.kostra.area.regnskap.kostra.KirkeKostraMain
import no.ssb.kostra.area.regnskap.kostra.KommuneKostraMain
import no.ssb.kostra.area.regnskap.kostra.KvartalKostraMain
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringMain
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpMain
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.felles.ErrorReport
import no.ssb.kostra.program.util.ConversionUtils
import no.ssb.kostra.program.util.ConversionUtils.fromArguments
import no.ssb.kostra.validation.ValidationResult
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.BarnevernValidator.validateBarnevern

object ControlDispatcher {
    fun validate(arguments: Arguments): ErrorReport {
        val validationResult = when (arguments.skjema) {
            in "0AK1", "0AK2", "0AK3", "0AK4",
            "0BK1", "0BK2", "0BK3", "0BK4",
            "0CK1", "0CK2", "0CK3", "0CK4",
            "0DK1", "0DK2", "0DK3", "0DK4" ->
                KvartalKostraMain(fromArguments(arguments, true)).validate()

            in "0A", "0B", "0C", "0D",
            "0I", "0J", "0K", "0L",
            "0M", "0N", "0P", "0Q" ->
                KommuneKostraMain(fromArguments(arguments, true)).validate()

            in "0F", "0G" ->
                KirkeKostraMain(fromArguments(arguments, true)).validate()

            in "0X", "0Y" ->
                HelseForetakMain(fromArguments(arguments, true)).validate()

            "11F" ->
                SosialhjelpMain(fromArguments(arguments, true)).validate()

            "11CF" ->
                KvalifiseringMain(fromArguments(arguments, true)).validate()

            "15F" ->
                validateBarnevern(fromArguments(arguments, false))

            "52AF" ->
                Familievern52aMain(fromArguments(arguments, true)).validate()

            "52BF" ->
                Familievern52bMain(fromArguments(arguments, true)).validate()

            "53F" ->
                Familievern53Main(fromArguments(arguments, true)).validate()

            "55F" ->
                Familievern55Main(fromArguments(arguments, true)).validate()

            else -> ValidationResult(
                reportEntries = listOf(
                    ValidationReportEntry(
                        severity = Severity.FATAL,
                        ruleName = "Ukjent skjema",
                        messageText = "Korrigér filutrekket. Forventet '${arguments.skjema}', " +
                                "men fant ikke noe. Avslutter...."


                    )
                ),
                numberOfControls = 1
            )
        }

        val errorReport = ErrorReport(arguments)
        validationResult.reportEntries
            .map {
                ConversionUtils.toErrorReportEntry(it)
            }
            .forEach {
                errorReport.addEntry(it)
            }

        errorReport.count = validationResult.numberOfControls.toLong()

        return errorReport
    }
}