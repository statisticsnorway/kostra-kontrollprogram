package no.ssb.kostra.area.famvern.famvern55

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileLoader
import no.ssb.kostra.program.extension.buildFieldDefinitions

object Familievern55FieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileLoader
            .getResourceAsFieldDefinitionList("file_description_55F.yaml")
            .buildFieldDefinitions()
}