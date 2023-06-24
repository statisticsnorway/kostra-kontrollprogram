package no.ssb.kostra.area.regnskap

import no.ssb.kostra.area.FieldDefinitions
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
import no.ssb.kostra.program.*

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
            dataType = INTEGER_TYPE,
            mandatory = true
        )
    )

    fun getFieldDefinitionsMergedWithKotlinArguments(args: KotlinArguments): List<FieldDefinition> =
        fieldDefinitions
            .map { fieldDefinition ->
                when (fieldDefinition.name.lowercase()) {
                    "skjema" -> fieldDefinition.codeList = listOf(Code(code = args.skjema, "Skjematype"))
                    "aargang" -> fieldDefinition.codeList = listOf(Code(code = args.aargang, "Årgang"))
                    "region" -> fieldDefinition.codeList = listOf(Code(code = args.region, "Region"))
                    "orgnr" -> fieldDefinition.codeList = args.orgnr.split(",".toRegex())
                        .map { code: String -> Code(code, "Organisasjonsnummer") }
                        .ifEmpty { emptyList() }
                    "foretaksnr" -> fieldDefinition.codeList = args.orgnr.split(",".toRegex())
                        .map { code: String -> Code(code, "Foretaksnummer") }
                        .ifEmpty { emptyList() }

                    else -> fieldDefinition
                }

            }.toList() as List<FieldDefinition> // looks suspicious
}