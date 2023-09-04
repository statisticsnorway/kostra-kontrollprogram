package no.ssb.kostra.area.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_AARGANG
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FORETAKSNR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KVARTAL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ORGNR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.extension.buildFieldDefinitions
import no.ssb.kostra.program.DataType.STRING_TYPE

object RegnskapFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            size = 2,
            name = FIELD_SKJEMA,
            dataType = STRING_TYPE,
            mandatory = true,
        ),
        FieldDefinition(
            size = 4,
            name = FIELD_AARGANG,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            name = FIELD_KVARTAL,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            size = 6,
            name = FIELD_REGION,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            size = 9,
            name = FIELD_ORGNR,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            size = 9,
            name = FIELD_FORETAKSNR,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            name = FIELD_KONTOKLASSE,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            size = 4,
            name = FIELD_FUNKSJON_KAPITTEL,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            size = 3,
            name = FIELD_ART_SEKTOR,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            size = 9,
            name = FIELD_BELOP,
            mandatory = true
        )
    ).mapIndexed { index, fieldDefinition ->
        fieldDefinition.copy(number = index + 1)
    }.buildFieldDefinitions()
}