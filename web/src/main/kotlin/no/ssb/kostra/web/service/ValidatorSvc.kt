package no.ssb.kostra.web.service

import jakarta.inject.Singleton
import no.ssb.kostra.controlprogram.ControlDispatcher
import no.ssb.kostra.web.extension.toErrorReportVm
import no.ssb.kostra.web.extension.toKostraArguments
import no.ssb.kostra.web.viewmodel.ErrorReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm

/** keep as singleton for testing purposes */
@Singleton
class ValidatorSvc {

    fun validateInput(request: KostraFormVm): ErrorReportVm =
        ControlDispatcher.doControls(request.toKostraArguments()).toErrorReportVm()
}