package no.ssb.kostra.area.famvern.famvern53

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileLoader
import no.ssb.kostra.program.extension.buildFieldDefinitions

object Familievern53FieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileLoader
            .getResourceAsFieldDefinitionList("file_description_53F.yaml")
            .buildFieldDefinitions()
}