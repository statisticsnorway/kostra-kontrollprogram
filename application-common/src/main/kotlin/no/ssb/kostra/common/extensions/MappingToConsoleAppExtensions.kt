package no.ssb.kostra.common.extensions

import no.ssb.kostra.common.viewmodel.KostraFormVm
import no.ssb.kostra.controlprogram.ArgumentConstants.*
import no.ssb.kostra.controlprogram.Arguments
import java.io.InputStream

private const val SEPARATOR_CHAR = ","
internal const val NAME_FALLBACK_VALUE = "UOPPGITT"

fun KostraFormVm.toKostraArguments(
    inputStream: InputStream
) = Arguments(this.toArgumentList(), inputStream)

private fun KostraFormVm.toArgumentList() = mutableMapOf(
    SCHEMA_ABBR to this.skjema,
    YEAR_ABBR to this.aar.toString(),
    REGION_ABBR to this.region,
    NAME_ABBR to (this.navn ?: NAME_FALLBACK_VALUE)
).also { parameterMap ->
    if (!this.orgnrForetak.isNullOrEmpty()) {
        if (this.orgnrVirksomhet.none()) {
            parameterMap[UNIT_ORGNR_ABBR] = this.orgnrForetak
        } else {
            parameterMap[COMPANY_ORGNR_ABBR] = this.orgnrForetak
        }
    }
    if (this.orgnrVirksomhet.any()) {
        parameterMap[UNIT_ORGNR_ABBR] = this.orgnrVirksomhet.joinToString(separator = SEPARATOR_CHAR) { it.orgnr }
    }
}.entries.flatMap { (arg, argValue) -> listOf("-$arg", argValue) }.toTypedArray()
