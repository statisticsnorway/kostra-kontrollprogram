package no.ssb.kostra.area.famvern.famvern52b

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileLoader

object Familievern52bFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileLoader
            .getResourceAsFieldDefinitionList("file_description_52BF_2024.yaml")
}