package no.ssb.kostra.area.sosial.kvalifisering

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.extension.buildFieldDefinitions
import no.ssb.kostra.program.extension.toFileDescription

object KvalifiseringFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        "11CF".toFileDescription().fields.buildFieldDefinitions()
}