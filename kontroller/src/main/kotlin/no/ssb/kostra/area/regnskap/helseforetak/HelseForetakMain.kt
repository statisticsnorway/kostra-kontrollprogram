package no.ssb.kostra.area.regnskap.helseforetak

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.mappingDuplicates
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.regnskap.*
import no.ssb.kostra.validation.rule.regnskap.helseforetak.*

class HelseForetakMain(
    arguments: KotlinArguments,

    ) : PositionedFileValidator(arguments) {
    private val hfOrgnr = listOf(
        //@formatter:off
        "928033821",
        "991324968",
        "983658725",
        "983658776",
        "883658752",
        "911912759",
        "914637651",
        "814630722",
        "987601787",
        "916879067",
        "818711832",
        "918177833",
        "918695079",
        "922307814"
        // @formatter:on
    )

    private val funksjoner = listOf(
        //@formatter:off
        "400", "460", "600", "606", "620", "630", "636", "637", "641", "642", "651", "681", "840"
        // @formatter:on
    )

    private fun getFunksjonAsList(): List<String> = when (arguments.skjema) {
        "0X" -> funksjoner.map { it.padEnd(4, ' ') }.sorted()
        else -> emptyList()
    }


    // Kapitler
    private fun getKapittelAsList(): List<String> = listOf("    ")


    // Arter
    private val arter = listOf(
        //@formatter:off
        "300", "301", "306", "307", "308",
        "310", "311", "312", "313", "318",
        "320", "321", "322", "323", "324", "325", "326", "327", "328", "329",
        "330", "331", "332", "334", "335", "336", "337", "338", "339",
        "340",
        "350",
        "360", "361", "362",
        "380",
        "390", "399",
        "400", "401", "402", "403", "404", "405", "406", "407", "409",
        "410", "411", "412", "413", "418", "419",
        "420", "421", "429",
        "430", "436", "439",
        "450", "451", "452", "456", "457", "459",
        "460", "461", "462", "463", "464", "468", "469",
        "490", "499",
        "500", "501", "502", "503", "504", "505", "506", "509",
        "510", "511", "512", "513", "514", "515", "516", "519",
        "520", "521", "522", "523", "524", "528", "529",
        "530", "539",
        "540", "541", "542",
        "550",
        "560", "561", "562",
        "570", "571", "579",
        "580", "581", "583", "589",
        "590", "591", "592", "593", "594", "595", "596", "599",
        "600", "601", "602", "603", "604", "605", "606", "609",
        "610", "619",
        "630", "632", "634", "635", "636", "639",
        "640", "641", "642", "643", "644", "645", "649",
        "650", "651", "652", "653", "654", "655", "656", "657", "659",
        "660", "661", "662", "663", "664", "669",
        "670", "671", "672", "675", "679",
        "680", "682", "684", "686", "689",
        "690", "691",
        "700", "702", "704", "709",
        "710", "713", "714", "715", "716", "719",
        "730", "735",
        "740", "741",
        "750", "751",
        "760", "769",
        "770", "771", "772", "779",
        "780", "781", "782", "783",
        "790",
        "800", "801", "802", "803", "804",
        "810", "811", "812", "813", "814", "815", "816",
        "870", "871",
        "893", "895", "896"
        // @formatter:on
    )

    private fun getArtAsList(): List<String> = when (arguments.skjema) {
        "0X" -> arter
        else -> emptyList()
    }

    private val sektorer = listOf(
        //@formatter:off
        "100", "102", "103", "104", "105", "106", "107", "108",
        "110", "112", "113", "114", "115", "116", "119",
        "120", "121", "123", "124", "125", "126", "127", "128", "129",
        "131", "132", "135", "136", "138", "139",
        "140", "141", "142", "143",
        "150", "153", "155", "156", "157", "158",
        "163", "165", "167",
        "170", "171", "172", "175", "176", "177",
        "181", "182", "183", "184", "185", "186", "187", "188",
        "190", "192", "194", "195",
        "200", "202", "205", "209",
        "210", "216", "218",
        "220", "221", "222", "223", "224", "226", "227", "228", "229",
        "230", "232", "234", "238", "240", "246",
        "260", "261", "262", "263", "264", "265", "269",
        "270", "271", "272", "274", "275", "276", "277", "278", "279",
        "280", "281",
        "290", "291", "292", "293", "294", "295", "296", "297", "298", "299"
        // @formatter:on
    )

    private fun getSektorAsList(): List<String> = when (arguments.skjema) {
        "0Y" -> sektorer
        else -> emptyList()
    }

    private val art320Funksjoner = listOf("620", "630", "636", "637", "641", "642", "651", "681", "840")
        .map { it.padEnd(4, ' ') }.sorted()

    private val artPositiveBelop = listOf("190", "192", "194", "195")

    override val fieldDefinitions = RegnskapFieldDefinitions

    override val preValidationRules = listOf(
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

        Rule500Funksjon400(validOrgnrList = hfOrgnr),
        Rule510Art320(validFunksjonList = art320Funksjoner),
        Rule520Konti19XKunPositiveBelop(validArtList = artPositiveBelop),
        Rule530SummeringDifferanse(),
        Rule540EiendelerErLikEgenkaptialPlussGjeld(),
    )
}
