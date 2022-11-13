package no.ssb.kostra.web.extension

import no.ssb.kostra.controlprogram.ArgumentConstants.COMPANY_ORGNR_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.NAME_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.REGION_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.SCHEMA_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.UNIT_ORGNR_ABBR
import no.ssb.kostra.controlprogram.ArgumentConstants.YEAR_ABBR
import no.ssb.kostra.controlprogram.Arguments
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.ByteArrayInputStream
import java.util.*

fun KostraFormVm.toKostraArguments(): Arguments = Arguments(
    mapOf(
        SCHEMA_ABBR to this.skjema,
        YEAR_ABBR to this.aar.toString(),
        REGION_ABBR to this.region,
        NAME_ABBR to (this.navn ?: "UOPPGITT"),
        COMPANY_ORGNR_ABBR to (this.orgnrForetak ?: ""),
        UNIT_ORGNR_ABBR to (this.orgnrVirksomhet?.joinToString(separator = ",") ?: "")
    ).entries.flatMap { (arg, argValue) -> listOf("-$arg", argValue) }.toTypedArray(),
    ByteArrayInputStream(Base64.getDecoder().decode(this.base64EncodedContent))
)
