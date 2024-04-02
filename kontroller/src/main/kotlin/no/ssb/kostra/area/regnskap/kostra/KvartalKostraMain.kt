package no.ssb.kostra.area.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants.osloKommuner
import no.ssb.kostra.area.regnskap.RegnskapValidator
import no.ssb.kostra.program.KotlinArguments

class KvartalKostraMain(
    arguments: KotlinArguments
) : RegnskapValidator(arguments) {
    private val bevilgningRegnskap = listOf("0AK1", "0AK2", "0AK3", "0AK4", "0CK1", "0CK2", "0CK3", "0CK4")
    private val balanseRegnskap = listOf("0BK1", "0BK2", "0BK3", "0BK4", "0DK1", "0DK2", "0DK3", "0DK4")

    @SuppressWarnings
    private val kommunaleFunksjoner = listOf(
        //@formatter:off
        "100", "110", "120", "121", "130",
        "170", "171", "172", "173", "180",
        "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
        "251", "252", "253", "256", "257", "258", "261", "265", "273", "275", "276", "281", "283", "285", "290",
        "301", "302", "303", "315", "320", "321", "322", "325", "329", "330", "332", "335", "338", "339", "340", "345",
        "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393"
        // @formatter:on
    )

    @SuppressWarnings
    private val fylkeskommunaleFunksjoner = listOf(
        // @formatter:off
        "400", "410", "420", "421", "430",
        "460", "465", "470", "471", "472", "473", "480",
        "510", "515", "520", "521", "522", "523", "525", "526", "527", "528", "529", "530", "531", "533", "534", "535", "536", "537",
        "554", "559", "561", "562", "570", "581", "590",
        "660",
        "665",
        "701", "710", "711", "713", "714", "716", "722", "730", "731", "732", "733", "734", "735", "740",
        "750", "760", "771", "772", "775", "790"
        // @formatter:on
    )

    @SuppressWarnings
    private val osloFunksjoner = listOf(
        "691", "692", "693", "694", "696"
    )

    @SuppressWarnings
    private val finansielleFunksjoner = listOf(
        "800", "840", "841", "850", "860", "870", "880", "899", "Z", "z", "~"
    )

    @SuppressWarnings
    override val funksjonList =
        if (arguments.skjema in bevilgningRegnskap) {
            val result = ArrayList<String>()
            result.addAll(finansielleFunksjoner)

            if (arguments.skjema in listOf("0AK1", "0AK2", "0AK3", "0AK4"))
                result.addAll(kommunaleFunksjoner)

            if (arguments.skjema in listOf("0CK1", "0CK2", "0CK3", "0CK4"))
                result.addAll(fylkeskommunaleFunksjoner)

            if (arguments.region in osloKommuner) {
                result.addAll(osloFunksjoner)
                result.addAll(fylkeskommunaleFunksjoner)
            }

            result.map { it.padEnd(fieldFunksjonKapittelLength, ' ') }.distinct().sorted()
        } else
            emptyList()

    @SuppressWarnings
    private val invalidDriftFunksjonList =
        if (arguments.skjema in bevilgningRegnskap)
        // Kun gyldig i investering og skal fjernes fra drift
            listOf("841 ")
        else
            emptyList()

    @SuppressWarnings
    private val invalidInvesteringFunksjonAsList =
        if (arguments.skjema in bevilgningRegnskap)
        // Kun gyldig i drift og skal fjernes fra investering
            listOf("800 ", "840 ", "860 ")
        else
            emptyList()

    @SuppressWarnings
    private val illogicalInvesteringFunksjonAsList: List<String> =
        if (arguments.skjema in bevilgningRegnskap)
        // Anses som ulogisk i investering
            listOf("100 ", "110 ", "121 ", "170 ", "171 ", "400 ", "410 ", "421 ", "470 ", "471 ")
        else
            emptyList()


    // Kapitler
    @SuppressWarnings
    override val kapittelList: List<String> =
        if (arguments.skjema in balanseRegnskap) {
            val result = mutableListOf(
                // @formatter:off
                "10", "11", "12", "13", "14", "15", "16", "18", "19",
                "20", "21", "22", "23", "24", "27", "28", "29",
                "31", "32", "33", "34", "35", "39",
                "40", "411", "412", "431", "451", "452", "453", "454", "47",
                "51", "53", "55", "56", "580", "581", "5900", "5970", "5990",
                "9100", "9110", "9200", "9999",
                "Z", "z", "~", ""
            // @formatter:on
            )

            result.map { it.padEnd(fieldFunksjonKapittelLength, ' ') }.distinct().sorted()
        } else
            emptyList()


    // Arter
    @SuppressWarnings
    private val basisArter = listOf(
        // @formatter:off
        "010", "020", "030", "040", "050", "070", "075", "080", "089", "090", "099",
        "100", "105", "110", "114", "115", "120", "130", "140", "150", "160", "165", "170", "180", "181", "182", "183", "184", "185", "190", "195",
        "200", "209", "210", "220", "230", "240", "250", "260", "270", "280", "285",
        "300", "330", "350", "370",
        "400", "429", "430", "450", "470",
        "500", "501", "509", "510", "511", "512", "520", "521", "522", "529", "530", "540", "550", "570", "589", "590",
        "600", "620", "629", "630", "640", "650", "660", "670",
        "700", "710", "729", "730", "750", "770",
        "800", "810", "830", "850", "870", "876", "877", "880", "890",
        "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "940", "950", "970", "980", "989", "990",
        "Z", "z", "~"
        // @formatter:on
    )

    @SuppressWarnings
    private val konserninterneArter = listOf(
        "380", "480", "780", "880"
    )

    @SuppressWarnings
    private val osloArter = listOf(
        "298", "379", "798"
    )

    @SuppressWarnings
    override val artList: List<String> =
        if (arguments.skjema in bevilgningRegnskap) {
            val result = ArrayList<String>(basisArter + konserninterneArter)

            if (arguments.region in osloKommuner) {
                result.addAll(osloArter)
            }
            result.map { it.padEnd(fieldArtSektorLength, ' ') }.distinct().sorted()
        } else
            emptyList()


    @SuppressWarnings
    override val sektorList: List<String> =
        if (arguments.skjema in balanseRegnskap)
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
            "Z", "z", "~"
            // @formatter:on
            ).map { it.padEnd(fieldArtSektorLength, ' ') }.distinct().sorted()
        else
            emptyList()


    // Kun gyldig i investering og skal fjernes fra drift
    @SuppressWarnings
    private val invalidDriftArtList = listOf(
        // @formatter:off
        "529",
        "670",
        "910", "911", "929", "970"
        // @formatter:on
    )


    // Kun gyldig i drift og skal fjernes fra investering
    @SuppressWarnings
    private val invalidInvesteringArtList = listOf(
        // @formatter:off
        "509", "570", "590",
        "800", "870", "871", "872", "873", "875", "876", "877",
        "909", "990"
        // @formatter:on
    )

    @SuppressWarnings("all")
    override val validationRules = commonValidationRules()
        .plus(
            commonKostraValidationRules(
                invalidDriftFunksjonList = invalidDriftFunksjonList,
                invalidDriftArtList = invalidDriftArtList,
                invalidInvesteringFunksjonList = invalidInvesteringFunksjonAsList,
                illogicalInvesteringFunksjonList = illogicalInvesteringFunksjonAsList,
                invalidInvesteringArtList = invalidInvesteringArtList
            )
        )
}
