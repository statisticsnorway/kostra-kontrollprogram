package no.ssb.kostra.web.extensions

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.io.BufferedReader
import java.io.InputStream

private const val SEPARATOR_CHAR = ","
internal const val QUARTER_FALLBACK_VALUE = " "
internal const val NAME_FALLBACK_VALUE = "UOPPGITT"
private val FALLBACK_COMPANY_ID = " ".repeat(9)

fun KostraFormVm.toKostraArguments(
    inputStream: InputStream,
    kvartal: String?
) = KotlinArguments(
    skjema = skjema,
    aargang = aar.toString(),
    kvartal = kvartal ?: QUARTER_FALLBACK_VALUE,
    region = region,
    navn = navn ?: NAME_FALLBACK_VALUE,
    orgnr = unitOrgnr(),
    foretaknr = orgnrForetak(),
    harVedlegg = false,
    isRunAsExternalProcess = false,
    inputFileContent = inputStream.bufferedReader().use(BufferedReader::readText)
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
