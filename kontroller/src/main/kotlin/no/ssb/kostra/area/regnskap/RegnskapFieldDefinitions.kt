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
import no.ssb.kostra.program.STRING_TYPE
import no.ssb.kostra.program.extension.buildFieldDefinitions

object RegnskapFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            number = 1,
            size = 2,
            name = FIELD_SKJEMA,
            dataType = STRING_TYPE,
            mandatory = true,
        ),
        FieldDefinition(
            number = 2,
            size = 4,
            name = FIELD_AARGANG,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            number = 3,
            name = FIELD_KVARTAL,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            number = 4,
            size = 6,
            name = FIELD_REGION,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            number = 5,
            size = 9,
            name = FIELD_ORGNR,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            number = 6,
            size = 9,
            name = FIELD_FORETAKSNR,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            number = 7,
            name = FIELD_KONTOKLASSE,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            number = 8,
            size = 4,
            name = FIELD_FUNKSJON_KAPITTEL,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            number = 9,
            size = 3,
            name = FIELD_ART_SEKTOR,
            dataType = STRING_TYPE
        ),
        FieldDefinition(
            number = 10,
            size = 9,
            name = FIELD_BELOP,
            mandatory = true
        )
    ).buildFieldDefinitions()
}