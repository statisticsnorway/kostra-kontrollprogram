package no.ssb.kostra.validation.report

enum class Severity(val info: SeverityInfo) {
    OK(SeverityInfo("Suksess",0, "black")),
    INFO(SeverityInfo("Informasjon", 0, "blue")),
    WARNING(SeverityInfo("Advarsel", 1, "orange")),
    ERROR(SeverityInfo("Feil", 2, "red")),
    FATAL(SeverityInfo("Kritisk feil", 2, "red"))
}