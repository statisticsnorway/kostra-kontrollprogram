package no.ssb.kostra.area.sosial.kvalifisering

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileLoader
import no.ssb.kostra.program.extension.buildFieldDefinitions

object KvalifiseringFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileLoader
            .getResourceAsFieldDefinitionList("file_description_11CF.yaml")
            .buildFieldDefinitions()
}