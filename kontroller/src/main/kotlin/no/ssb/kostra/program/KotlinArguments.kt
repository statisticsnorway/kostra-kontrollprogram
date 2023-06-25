package no.ssb.kostra.program

import java.io.InputStream

data class KotlinArguments(
    val skjema: String,
    val aargang: String,
    val kvartal: String = BLANK_CHAR,
    val region: String,
    val navn: String = "Uoppgitt",
    val orgnr: String = BLANK_CHAR.repeat(9),
    val foretaknr: String = BLANK_CHAR.repeat(9),
    val harVedlegg: Boolean = true,
    val isRunAsExternalProcess: Boolean = false,
    val inputFileContent: String = BLANK_CHAR,
    val inputFileStream: InputStream? = null
) {
    init {
        require(skjema.isNotBlank()) {
            "parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A"
        }
        require(aargang.isNotBlank()) {
            "parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2023"
        }
        require(region.isNotBlank()) {
            "parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100"
        }
    }

    fun getInputContentAsStringList(delimiter: String = DEFAULT_LINEBREAK_CHAR): List<String> = inputFileContent.split(delimiter)

    fun getInputContentAsInputStream(): InputStream = inputFileContent.byteInputStream(Charsets.ISO_8859_1)

    companion object {
        private const val BLANK_CHAR = " "
        internal const val DEFAULT_LINEBREAK_CHAR = "\n"
    }
}