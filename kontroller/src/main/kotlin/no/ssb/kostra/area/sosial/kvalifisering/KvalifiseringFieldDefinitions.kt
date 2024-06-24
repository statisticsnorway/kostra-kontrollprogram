package no.ssb.kostra.area.sosial.kvalifisering

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.FileDescriptionLoader
import no.ssb.kostra.program.extension.buildFieldDefinitions

object KvalifiseringFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        FileDescriptionLoader
            .getResourceAsFieldDefinitionList("filedescription_11CF.yaml")
            .buildFieldDefinitions()
}