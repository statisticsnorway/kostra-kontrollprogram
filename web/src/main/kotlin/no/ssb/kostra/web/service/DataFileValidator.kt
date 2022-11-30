package no.ssb.kostra.web.service

import jakarta.inject.Singleton
import no.ssb.kostra.controlprogram.ControlDispatcher
import no.ssb.kostra.web.extension.toErrorReportVm
import no.ssb.kostra.web.extension.toKostraArguments
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.InputStream

/** keep as singleton for testing purposes */
@Singleton
class DataFileValidator {

    fun validateDataFile(
        kostraForm: KostraFormVm,
        inputStream: InputStream
    ): FileReportVm =
        ControlDispatcher.doControls(kostraForm.toKostraArguments(inputStream)).toErrorReportVm()
}