package no.ssb.kostra.web.extension

import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.web.viewmodel.ReportRequestVm

fun ReportRequestVm.toKostraArguments(): Arguments {
    return Arguments(
        this.skjema,
        this.aar.toString(),
        "",
        this.region,
        this.organisasjon,
        "",
        "",
        false,
        false,
        listOf()
    )
}