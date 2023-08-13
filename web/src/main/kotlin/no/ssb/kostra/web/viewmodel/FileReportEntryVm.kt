package no.ssb.kostra.web.viewmodel

data class FileReportEntryVm(
    val journalnummer: String = "",
    val saksbehandler: String = "",
    val kontrollnummer: String = "",
    val kontrolltekst: String = "",
    val feilkode: KostraErrorCode = KostraErrorCode.NO_ERROR,
)
