package no.ssb.kostra.area.regnskap

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileLoader
import no.ssb.kostra.program.extension.buildFieldDefinitions

object RegnskapFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileLoader
            .getResourceAsFieldDefinitionList("file_description_Regnskap.yaml")
            .buildFieldDefinitions()
}