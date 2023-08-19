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
import no.ssb.kostra.validation.ValidationResult
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.barnevern.BarnevernValidator

object ControlDispatcher {
    fun validate(kotlinArguments: KotlinArguments): ErrorReport {
        val validationResult = when (kotlinArguments.skjema) {
            in "0AK1", "0AK2", "0AK3", "0AK4",
            "0BK1", "0BK2", "0BK3", "0BK4",
            "0CK1", "0CK2", "0CK3", "0CK4",
            "0DK1", "0DK2", "0DK3", "0DK4" ->
                KvartalKostraMain(kotlinArguments).validate()

            in "0A", "0B", "0C", "0D",
            "0I", "0J", "0K", "0L",
            "0M", "0N", "0P", "0Q" ->
                KommuneKostraMain(kotlinArguments).validate()

            in "0F", "0G" ->
                KirkeKostraMain(kotlinArguments).validate()

            in "0X", "0Y" ->
                HelseForetakMain(kotlinArguments).validate()

            "11F" ->
                SosialhjelpMain(kotlinArguments).validate()

            "11CF" ->
                KvalifiseringMain(kotlinArguments).validate()

            "15F" ->
                BarnevernValidator(kotlinArguments).validate()

            "52AF" ->
                Familievern52aMain(kotlinArguments).validate()

            "52BF" ->
                Familievern52bMain(kotlinArguments).validate()

            "53F" ->
                Familievern53Main(kotlinArguments).validate()

            "55F" ->
                Familievern55Main(kotlinArguments).validate()

            else -> ValidationResult(
                reportEntries = listOf(
                    ValidationReportEntry(
                        severity = Severity.FATAL,
                        ruleName = "Ukjent skjema",
                        messageText = "Korrigér filutrekket. Forventet '${kotlinArguments.skjema}', " +
                                "men fant ikke noe. Avslutter...."


                    )
                ),
                numberOfControls = 1
            )
        }

        val errorReport = ErrorReport(
            Arguments(
                kotlinArguments.skjema,
                kotlinArguments.aargang,
                kotlinArguments.kvartal,
                kotlinArguments.region,
                kotlinArguments.navn,
                kotlinArguments.orgnr,
                kotlinArguments.foretaknr,
                kotlinArguments.harVedlegg,
                kotlinArguments.isRunAsExternalProcess,
                mutableListOf("")
            )
        )
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