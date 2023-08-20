package no.ssb.kostra.web.extensions

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.InputStream

private const val SEPARATOR_CHAR = ","
internal const val NAME_FALLBACK_VALUE = "UOPPGITT"
private val FALLBACK_COMPANY_ID = " ".repeat(9)

fun KostraFormVm.toKostraArguments(
    inputStream: InputStream
) = KotlinArguments(
    skjema = skjema,
    aargang = aar.toString(),
    kvartal = " ",
    region = region,
    navn = navn ?: NAME_FALLBACK_VALUE,
    orgnr = unitOrgnr(),
    foretaknr = orgnrForetak(),
    harVedlegg = false,
    isRunAsExternalProcess = false,
    inputFileContent = inputStream.bufferedReader().use { it.readText() },
)

fun KostraFormVm.unitOrgnr() = this.orgnrForetak.let { orgnrForetak ->
    if (this.orgnrVirksomhet.any())
        this.orgnrVirksomhet.joinToString(separator = SEPARATOR_CHAR) { companyId -> companyId.orgnr }
    else
        orgnrForetak?.takeIf { it.isNotBlank() } ?: FALLBACK_COMPANY_ID
}

fun KostraFormVm.orgnrForetak() = this.orgnrForetak
    ?.takeIf { it.isNotBlank() && this.orgnrVirksomhet.any() }
    ?: FALLBACK_COMPANY_ID
