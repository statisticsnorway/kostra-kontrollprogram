package no.ssb.kostra.program

import java.io.InputStream

data class Arguments(
    val skjema: String = " ",
    val aargang: String = "    ",
    val kvartal: String = " ",
    val region: String = "      ",
    val navn: String = "Uoppgitt",
    val orgnr: String = "         ",
    val foretaknr: String = "         ",
    val harVedlegg: Boolean = true,
    val isRunAsExternalProcess: Boolean = false,
    val inputFileContent: String = " ",
    val inputFileStream: InputStream? = null
) {
    init {
        require(skjema.trim { it <= ' ' }.isNotEmpty()) {
            "parameter for skjema er ikke definert. Bruk -s SS. F.eks. -s 0A"
        }
        require(aargang.trim { it <= ' ' }.isNotEmpty()) {
            "parameter for årgang er ikke definert. Bruk -y YYYY. F.eks. -y 2023"
        }
        require(region.trim { it <= ' ' }.isNotEmpty()) {
            "parameter for region er ikke definert. Bruk -r RRRRRR. F.eks. -r 030100"
        }
    }

    fun getInputContentAsStringList(delimiter: String = "\n"): List<String> = inputFileContent.split(delimiter)

    fun getInputContentAsInputStream(): InputStream = inputFileContent.byteInputStream(Charsets.ISO_8859_1)
}