package no.ssb.kostra.area.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.mappingDuplicates
import no.ssb.kostra.area.regnskap.RegnskapConstants.osloKommuner
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.Validator
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.regnskap.*
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule025KombinasjonDriftKontoklasseArt
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule040KombinasjonInvesteringKontoklasseFunksjon
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule050KombinasjonInvesteringKontoklasseArt

class KvartalKostraMain(
    arguments: KotlinArguments
) : Validator(arguments) {
    private val kommunaleFunksjoner = listOf(
        //@formatter:off
        "100", "110", "120", "121", "130",
        "170", "171", "172", "173", "180", "190",
        "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
        "251", "252", "253", "254", "256", "261", "265", "273", "275", "276", "281", "283", "285", "290",
        "301", "302", "303", "315", "320", "321", "322", "325", "329", "330", "332", "335", "338", "339", "340", "345",
        "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393"
        // @formatter:on
    )

    private val fylkeskommunaleFunksjoner = listOf(
        // @formatter:off
        "400", "410", "420", "421", "430",
        "460", "465", "470", "471", "472", "473", "480", "490",
        "510", "515", "520", "521", "522", "523", "524", "525", "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537",
        "554", "559", "561", "562", "570", "581", "590",
        "660",
        "665",
        "701", "710", "711", "713", "714", "716", "722", "730", "731", "732", "733", "734", "735", "740",
        "750", "760", "771", "772", "775", "790"
        // @formatter:on
    )

    private val osloFunksjoner = listOf(
        "691", "692", "693", "694", "696"
    )

    private val finansielleFunksjoner = listOf(
        "800", "840", "841", "850", "860", "870", "880", "899", "Z", "z", "~"
    )

    private fun getFunksjonAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING,
                    RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
                )
            }
        ) return emptyList()

        val result = ArrayList<String>()
        when (arguments.skjema) {
            "0A" -> {
                if (arguments.region in osloKommuner) {
                    result.addAll(osloFunksjoner)
                    result.addAll(fylkeskommunaleFunksjoner)
                }
                result.addAll(kommunaleFunksjoner)
                result.addAll(finansielleFunksjoner)
            }

            "0C" -> {
                result.addAll(fylkeskommunaleFunksjoner)
            }

            else -> result.add("!!!!")
        }
        return result.map { it.padEnd(4, ' ') }.sorted().toList()
    }

    private fun getInvalidInvesteringFunksjonAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING,
                    RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        // Kun gyldig i drift og skal fjernes fra investering
        return listOf("800 ", "840 ", "860 ")
    }


    // Kapitler
    private fun getKapittelAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BALANSE
            }
        )
            return emptyList()

        val result = mutableListOf(
            // @formatter:off
            "10", "11", "12", "13", "14", "15", "16", "18", "19", "20", "21", "22", "23", "24", "27", "28", "29",
            "31", "32", "33", "34", "35", "39", "40", "41", "42", "43", "45", "47", "51", "53", "55", "56", "580", "581",
            "5900", "5950", "5960", "5970", "5990",
            "9100", "9110", "9200", "9999",
            "Z", "z", "~", ""
            // @formatter:on
        )

        return result.map { it.padEnd(4, ' ') }.sorted().toList()
    }


    // Arter
    private val basisArter = listOf(
        // @formatter:off
        "010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
        "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
        "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
        "300", "330", "350", "370", "375",
        "400", "429", "430", "450", "470",
        "500", "501", "509", "510", "511", "512", "520", "521", "522", "529", "530", "540", "550", "570", "589", "590",
        "600", "620", "629", "630", "640", "650", "660", "670", "690",
        "700", "710", "729", "730", "750", "770",
        "800", "810", "830", "850", "870", "871", "872", "873", "875", "876", "877", "890",
        "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "940", "950", "970", "980", "989", "990",
        "Z", "z", "~"
        // @formatter:on
    )

    private val konserninterneArter = listOf(
        "380", "480", "780", "880"
    )

    private val osloArter = listOf(
        "298", "379", "798"
    )

    private fun getArtAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING,
                    RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        val result = ArrayList<String>(basisArter)
        when (arguments.skjema) {
            "0A" -> {
                result.addAll(konserninterneArter)

                if (arguments.region in osloKommuner) {
                    result.addAll(osloArter)
                }
            }

            "0C" -> {
                result.addAll(konserninterneArter)
            }
        }

        return result.sorted().toList()

    }

    private fun getSektorAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BALANSE
            }
        )
            return emptyList()

        // Sektorer
        return listOf(
            // @formatter:off
            "000", "070", "080",
            "110", "151", "152",
            "200",
            "320", "355", "395",
            "430", "450", "499",
            "550", "570",
            "610", "640", "650",
            "890",
            "900",
            "Z", "z", "~"
            // @formatter:on
        )
    }

    // Kun gyldig i investering og skal fjernes fra drift
    private fun getInvalidDriftArtList() = listOf(
        // @formatter:off
        "529",
        "670",
        "910", "911", "929", "970"
        // @formatter:on
    )


    // Kun gyldig i drift og skal fjernes fra investering
    private fun getInvalidInvesteringArtList() = listOf(
        // @formatter:off
        "509", "570", "590",
        "800", "870", "871", "872", "873", "875", "876", "877",
        "909", "990"
        // @formatter:on
    )

    override val fieldDefinitions = RegnskapFieldDefinitions

    override val fatalRules = listOf(
        Rule001RecordLength(RegnskapFieldDefinitions.fieldLength)
    )

    override val validationRules = listOf(
        Rule003Skjema(),
        Rule004Aargang(),
        Rule005Kvartal(),
        Rule006Region(),
        Rule007Organisasjonsnummer(),
        Rule008Foretaksnummer(),
        Rule009Kontoklasse(kontoklasseList = RegnskapConstants.getKontoklasseBySkjema(arguments.skjema)),
        Rule010Funksjon(funksjonList = getFunksjonAsList()),
        Rule011Kapittel(kapittelList = getKapittelAsList()),
        Rule012Art(artList = getArtAsList()),
        Rule013Sektor(sektorList = getSektorAsList()),
        Rule014Belop(),
        Rule015Duplicates(mappingDuplicates(arguments = arguments)),
        Rule025KombinasjonDriftKontoklasseArt(invalidDriftArtList = getInvalidDriftArtList()),
        Rule040KombinasjonInvesteringKontoklasseFunksjon(invalidInvesteringFunksjonList = getInvalidInvesteringFunksjonAsList()),
        Rule050KombinasjonInvesteringKontoklasseArt(invalidInvesteringArtList = getInvalidInvesteringArtList()),
    )
}
