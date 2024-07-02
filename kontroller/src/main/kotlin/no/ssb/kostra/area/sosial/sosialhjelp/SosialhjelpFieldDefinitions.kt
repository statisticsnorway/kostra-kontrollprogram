package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileDescriptionLoader
import no.ssb.kostra.program.extension.buildFieldDefinitions

object SosialhjelpFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileDescriptionLoader
            .getResourceAsFieldDefinitionList("file_description_11F.yaml")
            .buildFieldDefinitions()
}