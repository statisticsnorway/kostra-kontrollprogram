package no.ssb.kostra.validation.report

enum class Severity(val info: SeverityInfo) {
    OK(SeverityInfo(0, "black")),
    INFO(SeverityInfo(0, "blue")),
    WARNING(SeverityInfo(1, "orange")),
    ERROR(SeverityInfo(2, "red")),
    FATAL(SeverityInfo(2, "red"))
}