package no.ssb.kostra.web.extension

import no.ssb.kostra.controlprogram.ArgumentConstants.COMPANY_ORGNR_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.NAME_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.REGION_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.SCHEMA_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.UNIT_ORGNR_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.YEAR_ABBR
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.InputStream

fun KostraFormVm.toKostraArguments(inputStream: InputStream) = Arguments(
    mapOf(
        SCHEMA_ABBR to this.skjema,
        YEAR_ABBR to this.aar.toString(),
        REGION_ABBR to this.region,
        NAME_ABBR to (this.navn ?: "UOPPGITT"),
        COMPANY_ORGNR_ABBR to (this.orgnrForetak ?: ""),
        UNIT_ORGNR_ABBR to (this.orgnrVirksomhet?.joinToString(separator = ",") { it.orgnr } ?: "")
    ).entries.flatMap { (arg, argValue) -> listOf("-$arg", argValue) }.toTypedArray(),
    inputStream
)
