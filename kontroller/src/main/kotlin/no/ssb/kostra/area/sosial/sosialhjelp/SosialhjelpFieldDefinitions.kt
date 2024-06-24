package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.extension.buildFieldDefinitions
import no.ssb.kostra.program.extension.toFileDescription

object SosialhjelpFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> =
        "11F".toFileDescription().fields.buildFieldDefinitions()
}