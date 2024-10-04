package no.ssb.kostra.area.famvern.famvern55

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileLoader

object Familievern55FieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileLoader
            .getResourceAsFieldDefinitionList("file_description_55F_2024.yaml")
}