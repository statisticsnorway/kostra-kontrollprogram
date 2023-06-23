package no.ssb.kostra.program

import no.ssb.kostra.validation.report.ValidationReportEntry

class ControlDispatcher {
    companion object {
        fun doControls(arguments: KotlinArguments): List<ValidationReportEntry> {
/*
            if (Comparator.isCodeInCodeList(
                    arguments.skjema, listOf(
                        "0AK1", "0AK2", "0AK3", "0AK4",
                        "0BK1", "0BK2", "0BK3", "0BK4",
                        "0CK1", "0CK2", "0CK3", "0CK4",
                        "0DK1", "0DK2", "0DK3", "0DK4"
                    )
                )
            ) {
                errorReport = Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(
                    arguments.skjema, listOf(
                        "0A", "0B", "0C", "0D",
                        "0I", "0J", "0K", "0L",
                        "0M", "0N", "0P", "0Q"
                    )
                )
            ) {
                errorReport = no.ssb.kostra.control.regnskap.kostra.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("0F", "0G"))) {
                errorReport = no.ssb.kostra.control.regnskap.kirkekostra.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("0X", "0Y"))) {
                errorReport = no.ssb.kostra.control.regnskap.helseforetak.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("11F"))) {
                errorReport = no.ssb.kostra.control.sosial.s11_sosialhjelp.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("11CF"))) {
                errorReport = no.ssb.kostra.control.sosial.s11c_kvalifiseringsstonad.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("15F"))) {
                errorReport = no.ssb.kostra.control.barnevern.s15.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("52AF"))) {
                errorReport = no.ssb.kostra.control.famvern.s52a.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("52BF"))) {
                errorReport = no.ssb.kostra.control.famvern.s52b.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("53F"))) {
                errorReport = no.ssb.kostra.control.famvern.s53.Main.doControls(arguments)
            } else if (Comparator.isCodeInCodeList(arguments.skjema, listOf("55F"))) {
                errorReport = no.ssb.kostra.control.famvern.s55.Main.doControls(arguments)
            } else {
                errorReport = ErrorReport(arguments)
                errorReport.addEntry(
                    ErrorReportEntry(
                        " ", " ", " ", " ", "Ukjent skjema", "Korrigér filutrekket. Forventet '"
                                + arguments.skjema
                                + "', men fant ikke noe. Avslutter....", Constants.CRITICAL_ERROR
                    )
                )
            }
*/
            return emptyList()
        }
    }
}