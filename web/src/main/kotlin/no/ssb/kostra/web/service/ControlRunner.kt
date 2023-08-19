package no.ssb.kostra.web.service

import jakarta.inject.Singleton
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraErrorCode
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.InputStream

/** keep as singleton for testing purposes */
@Singleton
class ControlRunner {

    fun runControls(
        kostraForm: KostraFormVm,
        inputStream: InputStream
    ): FileReportVm = FileReportVm(
        KostraFormVm(),
        42,
        KostraErrorCode.NORMAL_ERROR,
        feil = emptyList()
    )
        // FIX ME ControlDispatcher.validate(kostraForm.toKostraArguments(inputStream)).toErrorReportVm()
}