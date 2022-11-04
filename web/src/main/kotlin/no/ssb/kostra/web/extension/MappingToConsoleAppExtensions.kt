package no.ssb.kostra.web.extension

import no.ssb.kostra.controlprogram.ArgumentConstants.NAME_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.REGION_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.SCHEMA_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.YEAR_ABBR
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.web.viewmodel.ReportRequestVm

fun ReportRequestVm.toKostraArguments(): Arguments = Arguments(
    arrayOf(
        SCHEMA_ABBR.toString(), this.skjema,
        YEAR_ABBR.toString(), this.aar.toString(),
        REGION_ABBR.toString(), this.region,
        NAME_ABBR.toString(), this.organisasjon ?: "UOPPGITT"
    ),
    this.contentAsInputStream)