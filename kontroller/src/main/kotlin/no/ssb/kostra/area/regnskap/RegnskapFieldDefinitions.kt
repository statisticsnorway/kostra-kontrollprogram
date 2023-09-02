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

object RegnskapFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions = listOf(
        FieldDefinition(
            number = 1,
            from = 1,
            to = 2,
            name = FIELD_SKJEMA,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            number = 2,
            from = 3,
            to = 6,
            name = FIELD_AARGANG,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            number = 3,
            from = 7,
            to = 7,
            name = FIELD_KVARTAL,
            dataType = STRING_TYPE,
            mandatory = false
        ),
        FieldDefinition(
            number = 4,
            from = 8,
            to = 13,
            name = FIELD_REGION,
            dataType = STRING_TYPE,
            mandatory = true
        ),
        FieldDefinition(
            number = 5,
            from = 14,
            to = 22,
            name = FIELD_ORGNR,
            dataType = STRING_TYPE,
            mandatory = false
        ),
        FieldDefinition(
            number = 6,
            from = 23,
            to = 31,
            name = FIELD_FORETAKSNR,
            dataType = STRING_TYPE,
            mandatory = false
        ),
        FieldDefinition(
            number = 7,
            from = 32,
            to = 32,
            name = FIELD_KONTOKLASSE,
            dataType = STRING_TYPE,
            mandatory = false
        ),
        FieldDefinition(
            number = 8,
            from = 33,
            to = 36,
            name = FIELD_FUNKSJON_KAPITTEL,
            dataType = STRING_TYPE,
            mandatory = false
        ),
        FieldDefinition(
            number = 9,
            from = 37,
            to = 39,
            name = FIELD_ART_SEKTOR,
            dataType = STRING_TYPE,
            mandatory = false
        ),
        FieldDefinition(
            number = 10,
            from = 40,
            to = 48,
            name = FIELD_BELOP,
            mandatory = true
        )
    )
}