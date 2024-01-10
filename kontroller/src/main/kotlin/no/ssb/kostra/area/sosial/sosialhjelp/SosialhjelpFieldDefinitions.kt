package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileDescriptionLoader

object SosialhjelpFieldDefinitions : FieldDefinitions {
    private const val NAME = "sosialhjelp_11_filedescription.yaml"
    override val fieldDefinitions: List<FieldDefinition> =
        FileDescriptionLoader.getResourceAsFileDescription(NAME)?.fields ?: emptyList()
}