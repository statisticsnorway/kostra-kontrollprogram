package no.ssb.kostra.area.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.osloKommuner
import no.ssb.kostra.area.regnskap.RegnskapValidator
import no.ssb.kostra.program.KotlinArguments

class KommuneKostraMain(
    arguments: KotlinArguments,
) : RegnskapValidator(arguments) {
    private val bevilgningRegnskap = listOf("0A", "0C", "0I", "0K", "0M", "0P")
    private val balanseRegnskap = listOf("0B", "0D", "0J", "0L", "0N", "0Q")

    private val orgnrSpesial =
        listOf(
            "817920632", // Trøndelag
            "921234554", // Drammen
            "958935420", // Oslo
            "964338531", // Bergen
        )

    private val kommunenummerSpesial =
        listOf(
            "030100", // Oslo
            "300500", // Drammen
            "460100", // Bergen
            "500000", // Trøndelag
        )

    // @formatter:off
    @SuppressWarnings("Duplicates")
    @Suppress("ktlint:standard:argument-list-wrapping")
    private val kommunaleFunksjoner =
        listOf(
            "100", "110", "120", "121", "130",
            "170", "171", "172", "173", "180",
            "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
            "251", "252", "253", "256", "257", "258", "261", "265", "273", "275", "276", "281", "283", "285", "290",
            "301", "302", "303", "315", "320", "321", "322", "325", "329", "330", "332", "335", "338", "339", "340", "345",
            "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393",
        )
    // @formatter:on

    // @formatter:off
    @SuppressWarnings("Duplicates")
    @Suppress("ktlint:standard:argument-list-wrapping")
    private val fylkeskommunaleFunksjoner =
        listOf(
            "400", "410", "420", "421", "430",
            "460", "465", "470", "471", "472", "473", "480",
            "511", "515", "520", "521", "522", "523", "525", "526", "527", "528", "529", "530", "531", "533", "534", "535", "536", "537",
            "553", "554", "559", "562", "563", "564", "570", "581", "590",
            "660", "665",
            "701", "710", "711", "713", "714", "716", "722", "730", "731", "732", "733", "734", "735", "740",
            "750", "760", "771", "772", "775", "790",
        )
    // @formatter:on

    private val osloFunksjoner =
        listOf("691", "692", "693", "694", "696")

    private val kommuneFinansielleFunksjoner =
        listOf("800", "840", "841", "850", "860", "870", "880", "899")

    private val fylkeFinansielleFunksjoner =
        listOf("800", "840", "841", "860", "870", "880", "899")

    private val fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner =
        listOf("841", "860", "870", "880", "899")

    private val kommunaleSbdrFinansielleFunksjoner =
        listOf("841", "850", "860", "870", "880", "899")

    override val funksjonList =
        if (arguments.skjema in bevilgningRegnskap) {
            val result = ArrayList<String>()

            if (arguments.skjema in listOf("0A", "0I", "0M")) {
                result.addAll(kommunaleFunksjoner)
            }

            if (arguments.skjema in listOf("0A", "0M")) {
                result.addAll(kommuneFinansielleFunksjoner)
            }

            if (arguments.skjema in listOf("0I")) {
                if (arguments.orgnr in orgnrSpesial) {
                    result.addAll(fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner)
                } else {
                    result.addAll(kommunaleSbdrFinansielleFunksjoner)
                }
            }

            if (arguments.skjema in listOf("0C", "0K", "0P")) {
                result.addAll(fylkeskommunaleFunksjoner)
            }

            if (arguments.skjema in listOf("0C", "0P")) {
                result.addAll(fylkeFinansielleFunksjoner)
            }

            if (arguments.skjema in listOf("0K")) {
                result.addAll(fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner)
            }

            if (arguments.region in osloKommuner) {
                result.addAll(osloFunksjoner)
                result.addAll(fylkeskommunaleFunksjoner)
            }

            result.map { it.padEnd(4, ' ') }.distinct().sorted()
        } else {
            emptyList()
        }

    private val invalidDriftFunksjonList =
        if (arguments.skjema in bevilgningRegnskap) {
            // Kun gyldig i investering og skal fjernes fra drift
            listOf("841 ")
        } else {
            emptyList()
        }

    private val invalidInvesteringFunksjonAsList: List<String> =
        if (arguments.skjema in bevilgningRegnskap) {
            // Kun gyldig i drift og skal fjernes fra investering
            listOf("800 ", "840 ", "850 ", "860 ")
        } else {
            emptyList()
        }

    private val illogicalInvesteringFunksjonAsList: List<String> =
        if (arguments.skjema in bevilgningRegnskap) {
            // Anses som ulogisk i investering
            listOf("100 ", "110 ", "121 ", "170 ", "171 ", "400 ", "410 ", "421 ", "470 ", "471 ")
        } else {
            emptyList()
        }

    // Kapitler
    // @formatter:off
    @Suppress("ktlint:standard:argument-list-wrapping")
    override val kapittelList =
        if (arguments.skjema in balanseRegnskap) {
            val result =
                mutableListOf(
                    // @formatter:off
                    "10", "11", "12", "13", "14", "15", "16", "18", "19",
                    "20", "21", "22", "23", "24", "27", "28", "29",
                    "31", "32", "33", "34", "35", "39",
                    "40", "411", "412", "431", "451", "452", "453", "454", "47",
                    "51", "53", "55", "56", "580", "581", "5900", "5970", "5990",
                    "9100", "9110", "9200", "9999",
                    // @formatter:on
                )

            if (arguments.skjema in listOf("0J", "0L")) {
                result.addAll(listOf("17", "46"))
            }

            result.map { it.padEnd(4, ' ') }.sorted()
        } else {
            emptyList()
        }
    // @formatter:on

    // Arter
    // @formatter:off
    @SuppressWarnings("Duplicates")
    @Suppress("ktlint:standard:argument-list-wrapping")
    private val basisArter =
        listOf(
            // @formatter:off
            "010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
            "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
            "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
            "300", "330", "350", "370", "380",
            "400", "429", "430", "450", "470", "480",
            "500", "501", "509", "510", "511", "512", "520", "521", "522", "529", "530", "540", "550", "570", "589", "590",
            "600", "620", "629", "630", "640", "650", "660", "670",
            "700", "710", "729", "730", "750", "770", "780",
            "800", "810", "830", "850", "870", "877", "880", "890",
            "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "940", "950", "970", "980", "989", "990",
        )
    // @formatter:on

    private val osloArter =
        listOf("298", "379", "798")

    private val kommunaleArter =
        listOf("871", "872", "873", "876", "878", "879")

    override val artList: List<String> =
        if (arguments.skjema in listOf("0A", "0C", "0I", "0K", "0M", "0P")) {
            ArrayList<String>(basisArter)
                .apply {
                    when (arguments.skjema) {
                        in listOf("0A", "0M") -> {
                            addAll(kommunaleArter)

                            if (arguments.region in osloKommuner) {
                                addAll(osloArter)
                            }
                        }
                    }
                }.sorted()
        } else {
            emptyList()
        }

    // @formatter:off
    @SuppressWarnings("Duplicates")
    @Suppress("ktlint:standard:argument-list-wrapping")
    override val sektorList =
        if (arguments.skjema in listOf("0B", "0D", "0J", "0L", "0N", "0Q")) {
            // Sektorer
            listOf(
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
                // @formatter:on
            )
        } else {
            emptyList()
        }
    // @formatter:on

    // Kun gyldig i investering og skal fjernes fra drift
    // @formatter:off
    @Suppress("ktlint:standard:argument-list-wrapping")
    private val invalidDriftArtList =
        if ((arguments.skjema in listOf("0I", "0K") && arguments.orgnr in orgnrSpesial) ||
            (arguments.skjema in listOf("0M", "0P") && arguments.region in kommunenummerSpesial)
        ) {
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "922", "929", "970")
        } else {
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "921", "922", "929", "970")
        }
    // @formatter:on

    // Kun gyldig i drift og skal fjernes fra investering
    // @formatter:off
    @Suppress("ktlint:standard:argument-list-wrapping")
    private val invalidInvesteringArtList =
        listOf(
            "070", "080",
            "110", "114",
            "240",
            "509", "570", "590",
            "600", "629", "630", "640",
            "800", "870", "871", "872", "873", "875", "876", "877",
            "909", "990",
        )
    // @formatter:on

    @SuppressWarnings("Duplicates")
    override val validationRules =
        commonValidationRules()
            .plus(
                commonKostraValidationRules(
                    invalidDriftFunksjonList = invalidDriftFunksjonList,
                    invalidDriftArtList = invalidDriftArtList,
                    invalidInvesteringFunksjonList = invalidInvesteringFunksjonAsList,
                    illogicalInvesteringFunksjonList = illogicalInvesteringFunksjonAsList,
                    invalidInvesteringArtList = invalidInvesteringArtList,
                ),
            ).plus(
                specificKostraValidationRules(),
            )
}
