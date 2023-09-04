package no.ssb.kostra.web.service

import jakarta.inject.Singleton
import no.ssb.kostra.program.ControlDispatcher
import no.ssb.kostra.web.extensions.toErrorReportVm
import no.ssb.kostra.web.extensions.toKostraArguments
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.InputStream

/** keep as singleton for testing purposes */
@Singleton
class ControlRunner {

    fun runControls(
        kostraForm: KostraFormVm,
        inputStream: InputStream
    ): FileReportVm = ControlDispatcher.validate(
        kotlinArguments = kostraForm.toKostraArguments(
            inputStream = inputStream
        )
    ).toErrorReportVm(kostraForm)
}