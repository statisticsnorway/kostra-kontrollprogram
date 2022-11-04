package no.ssb.kostra.web.service

import jakarta.inject.Singleton
import no.ssb.kostra.controlprogram.ControlDispatcher
import no.ssb.kostra.web.extension.toErrorReportVm
import no.ssb.kostra.web.extension.toKostraArguments
import no.ssb.kostra.web.viewmodel.ErrorReportVm
import no.ssb.kostra.web.viewmodel.ReportRequestVm

/** keep as singleton for testing purposes */
@Singleton
class ValidatorSvc {

    fun validateInput(request: ReportRequestVm): ErrorReportVm =
        ControlDispatcher.doControls(request.toKostraArguments()).toErrorReportVm()
}