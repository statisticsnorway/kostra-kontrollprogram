package no.ssb.kostra.web.extension

import no.ssb.kostra.controlprogram.ArgumentConstants.NAME_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.REGION_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.SCHEMA_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.YEAR_ABBR
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.web.viewmodel.ReportRequestVm
import java.io.ByteArrayInputStream
import java.util.*

fun ReportRequestVm.toKostraArguments(): Arguments = Arguments(
        mapOf(
            SCHEMA_ABBR to this.skjema,
            YEAR_ABBR to this.aar.toString(),
            REGION_ABBR to this.region,
            NAME_ABBR to (this.organisasjon ?: "UOPPGITT")
        ).entries.flatMap { (arg, argValue) ->
            listOf("-$arg", argValue)
        }.toTypedArray(),
        ByteArrayInputStream(Base64.getDecoder().decode(this.base64EncodedContent))
    )
