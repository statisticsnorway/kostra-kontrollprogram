package no.ssb.kostra.area.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BALANSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_BYDEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_FYLKE
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_KOMMUNE
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_LANEFOND
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_OSLO
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_SAERBEDRIFT
import no.ssb.kostra.area.regnskap.RegnskapConstants.REGION_SVALBARD
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.TITLE_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldLength
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.getFieldDefinitionsMergedWithKotlinArguments
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.regnskap.*
import no.ssb.kostra.validation.rule.regnskap.kostra.*


class KommuneKostra(
    private val arguments: KotlinArguments
) {
    private val osloKommuner = listOf(
        // @formatter:off
        "030100",
        "030101", "030102", "030103", "030104", "030105",
        "030106", "030107", "030108", "030109", "030110",
        "030111", "030112", "030113", "030114", "030115"
        // @formatter:on
    )

    private val osloBydeler = listOf(
        // @formatter:off
        "030101", "030102", "030103", "030104", "030105",
        "030106", "030107", "030108", "030109", "030110",
        "030111", "030112", "030113", "030114", "030115"
        // @formatter:on
    )

    private val svalbard = listOf("211100")

    private val orgnrSpesial = listOf(
        "817920632",  // Trøndelag
        "921234554",  // Drammen
        "958935420",  // Oslo
        "964338531", // Bergen
    )

    fun getRegionType(): List<String> {
        val regionTypelist: MutableList<String> = mutableListOf()

        if (arguments.orgnr.trim().isNotEmpty()) {
            if (arguments.orgnr in orgnrSpesial)
                regionTypelist.add(REGION_LANEFOND)
            else
                regionTypelist.add(REGION_SAERBEDRIFT)
        } else {

            if (arguments.region.substring(3, 2) == "00")
                regionTypelist.add(REGION_FYLKE)
            else if (arguments.region.substring(5, 2) != "00")
                regionTypelist.add(REGION_BYDEL)
            else
                regionTypelist.add(REGION_KOMMUNE)

            if (arguments.region == "030100") {
                regionTypelist.add(REGION_FYLKE)
                regionTypelist.add(REGION_KOMMUNE)
                regionTypelist.add(REGION_OSLO)
            }

            if (arguments.region in svalbard) {
                regionTypelist.add(REGION_SVALBARD)
            }

        }

        return regionTypelist.distinct().toList()
    }

    private val kommunenummerSpesial = listOf(
        "030100",  // Oslo
        "300500",  // Drammen
        "460100",  // Bergen
        "500000",  // Trøndelag
    )

    private val kommunaleFunksjoner = listOf(
        //@formatter:off
        "100", "110", "120", "121", "130",
        "170", "171", "172", "173", "180",
        "201", "202", "211", "213", "215", "221", "222", "223", "231", "232", "233", "234", "241", "242", "243", "244",
        "251", "252", "253", "254", "256", "261", "265", "273", "275", "276", "281", "283", "285", "290",
        "301", "302", "303", "315", "320", "321", "322", "325", "329", "330", "332", "335", "338", "339", "340", "345",
        "350", "353", "354", "355", "360", "365", "370", "373", "375", "377", "380", "381", "383", "385", "386", "390", "392", "393"
        // @formatter:on
    )

    private val fylkeskommunaleFunksjoner = listOf(
        // @formatter:off
        "400", "410", "420", "421", "430",
        "460", "465", "470", "471", "472", "473", "480",
        "510", "515", "520", "521", "522", "523", "524", "525", "526", "527", "528", "529", "530", "531", "532", "533", "534", "535", "536", "537",
        "554", "559", "561", "562", "570", "581", "590",
        "660",
        "665",
        "701", "710", "711", "715", "716", "722", "730", "731", "732", "733", "734", "735", "740",
        "750", "760", "771", "772", "775", "790"
        // @formatter:on
    )

    private val osloFunksjoner = listOf(
        "691", "692", "693", "694", "696"
    )

    private val kommuneFinansielleFunksjoner = listOf(
        "800", "840", "841", "850", "860", "870", "880", "899"
    )

    private val fylkeFinansielleFunksjoner = listOf(
        "800", "840", "841", "860", "870", "880", "899"
    )

    private val fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner = listOf(
        "841", "860", "870", "880", "899"
    )

    private val kommunaleSbdrFinansielleFunksjoner = listOf(
        "841", "850", "860", "870", "880", "899"
    )

    private fun getFunksjonAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    ACCOUNTING_TYPE_BEVILGNING,
                    ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        val result = ArrayList<String>()
        when (arguments.skjema) {
            "0A", "0M" -> {
                if (arguments.region in osloKommuner) {
                    result.addAll(osloFunksjoner)
                    result.addAll(fylkeskommunaleFunksjoner)
                }
                result.addAll(kommunaleFunksjoner)
                result.addAll(kommuneFinansielleFunksjoner)
            }

            "0C", "0P" -> {
                result.addAll(fylkeskommunaleFunksjoner)
                result.addAll(fylkeFinansielleFunksjoner)
            }

            "0I" -> {
                if (arguments.region in osloKommuner) {
                    result.addAll(osloFunksjoner)
                    result.addAll(fylkeskommunaleFunksjoner)
                }
                result.addAll(kommunaleFunksjoner)
                if (arguments.orgnr in orgnrSpesial) {
                    result.addAll(fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner)
                } else {
                    result.addAll(kommunaleSbdrFinansielleFunksjoner)
                }
            }

            "0K" -> {
                result.addAll(fylkeskommunaleFunksjoner)
                result.addAll(fylkeskommunaleSbdrOgLaanefondFinansielleFunksjoner)
            }

            else -> result.add("!!!!")
        }
        return result.map { it.padEnd(4, ' ') }.sorted().toList()
    }

    private fun getInvalidDriftFunksjonList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    ACCOUNTING_TYPE_BEVILGNING,
                    ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        // Kun gyldig i investering og skal fjernes fra drift
        return listOf("841 ")
    }

    private fun getInvalidInvesteringFunksjonAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    ACCOUNTING_TYPE_BEVILGNING,
                    ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        // Kun gyldig i drift og skal fjernes fra investering
        return listOf("800 ", "840 ", "850 ", "860 ")
    }

    private fun getIllogicalInvesteringFunksjonAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    ACCOUNTING_TYPE_BEVILGNING,
                    ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        // Anses som ulogisk i investering
        return listOf("100 ", "110 ", "121 ", "170 ", "171 ", "400 ", "410 ", "421 ", "470 ", "471 ")
    }


    // Kapitler
    private fun getKapittelAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == ACCOUNTING_TYPE_BALANSE
            }
        )
            return emptyList()

        val result = mutableListOf(
            // @formatter:off
            "10", "11", "12", "13", "14", "15", "16", "18", "19",
            "20", "21", "22", "23", "24", "27", "28", "29",
            "31", "32", "33", "34", "35", "39",
            "40", "41", "42", "43", "45", "47",
            "51", "53", "55", "56", "580", "581", "5900", "5970", "5990",
            "9100", "9110", "9200", "9999"
            // @formatter:on
        )

        if (arguments.skjema in listOf("0J", "0L")) {
            result.addAll(listOf("17", "46"))
        }

        return result.map { it.padEnd(4, ' ') }.sorted().toList()
    }


    // Arter
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
        "800", "810", "830", "850", "870", "890",
        "900", "901", "905", "909", "910", "911", "912", "920", "921", "922", "929", "940", "950", "970", "980", "989", "990"
        // @formatter:on
    )

    private val konserninterneArter = listOf(
        "380", "480", "780", "880"
    )

    private val osloArter = listOf(
        "298", "379", "798"
    )

    private val kommunaleArter = listOf(
        "874", "875", "877"
    )

    private val fylkeskommunaleArter = listOf(
        "877"
    )

    private fun getArtAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    ACCOUNTING_TYPE_BEVILGNING,
                    ACCOUNTING_TYPE_REGIONALE
                )
            }
        )
            return emptyList()

        val result = ArrayList<String>(basisArter)
        when (arguments.skjema) {
            "0A", "0M" -> {
                result.addAll(konserninterneArter)
                result.addAll(kommunaleArter)

                if (arguments.region in osloKommuner) {
                    result.addAll(osloArter)
                }
            }

            "0C", "0P" -> {
                result.addAll(konserninterneArter)
                result.addAll(fylkeskommunaleArter)
            }

            "0I", "0K" -> {
                result.addAll(konserninterneArter)
            }
        }

        return result.sorted().toList()

    }

    private fun getSektorAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == ACCOUNTING_TYPE_BALANSE
            }
        )
            return emptyList()

        // Sektorer
        return listOf(
            // @formatter:off
            "000", "070", "080",
            "110", "151", "152",
            "200", "320",
            "355", "395",
            "430", "450", "499",
            "550", "570",
            "610", "640", "650",
            "890", "900"
            // @formatter:on
        )
    }

    // Kun gyldig i investering og skal fjernes fra drift
    fun getInvalidDriftArtList() =
        if ((arguments.skjema in listOf("0I", "0K") && arguments.orgnr in orgnrSpesial)
            || (arguments.skjema in listOf("0M", "0P") && arguments.region in kommunenummerSpesial)
        ) {
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "922", "929", "970")
        } else {
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "921", "922", "929", "970")
        }

    // Kun gyldig i drift og skal fjernes fra investering
    fun getInvalidInvesteringArtList() = listOf(
        // @formatter:off
        "070", "080",
        "110", "114",
        "240",
        "509", "570", "590",
        "600", "629", "630", "640",
        "800", "870", "874", "875", "877",
        "909", "990"
        // @formatter:on
    )

    private fun mappingDuplicates(): Pair<List<String>, List<String>> =
        when (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema)) {
            listOf(ACCOUNTING_TYPE_BEVILGNING, ACCOUNTING_TYPE_REGIONALE) ->
                listOf(
                    FIELD_KONTOKLASSE, FIELD_FUNKSJON, FIELD_ART
                ) to listOf(
                    TITLE_KONTOKLASSE, TITLE_FUNKSJON, TITLE_ART
                )

            listOf(ACCOUNTING_TYPE_BALANSE) ->
                listOf(
                    FIELD_KONTOKLASSE, FIELD_KAPITTEL, FIELD_SEKTOR
                ) to listOf(
                    TITLE_KONTOKLASSE, TITLE_KAPITTEL, TITLE_SEKTOR
                )

            else -> emptyList<String>() to emptyList()
        }

    private val fatalRules = listOf(
        Rule001RecordLength(fieldLength)
    )

    private val validationRules = listOf(
        Rule003Skjema(arguments = arguments),
        Rule004Aargang(arguments = arguments),
        Rule005Kvartal(arguments = arguments),
        Rule006Region(arguments = arguments),
        Rule007Organisasjonsnummer(arguments = arguments),
        Rule008Foretaksnummer(arguments = arguments),
        Rule009Kontoklasse(kontoklasseList = RegnskapConstants.getKontoklasseBySkjema(arguments.skjema)),
        Rule010Funksjon(funksjonList = getFunksjonAsList()),
        Rule011Kapittel(kapittelList = getKapittelAsList()),
        Rule012Art(artList = getArtAsList()),
        Rule013Sektor(sektorList = getSektorAsList()),
        Rule014Belop(),
        Rule015Duplicates(mappingDuplicates()),
        Rule020KombinasjonDriftKontoklasseFunksjon(invalidDriftFunksjonList = getInvalidDriftFunksjonList()),
        Rule025KombinasjonDriftKontoklasseArt(invalidDriftArtList = getInvalidDriftArtList()),
        Rule030KombinasjonDriftKontoklasseArt(illogicalDriftArtList = listOf("285", "660")),
        Rule035KombinasjonDriftKontoklasseArt(illogicalDriftArtList = listOf("520", "920")),
        Rule040KombinasjonInvesteringKontoklasseFunksjon(invalidInvesteringFunksjonList = getInvalidInvesteringFunksjonAsList()),
        Rule045KombinasjonInvesteringKontoklasseFunksjon(illogicalInvesteringFunksjonArtList = getIllogicalInvesteringFunksjonAsList()),
        Rule050KombinasjonInvesteringKontoklasseArt(invalidInvesteringArtList = getInvalidInvesteringArtList()),
        Rule055KombinasjonInvesteringKontoklasseArt(illogicalInvesteringArtList = listOf("620", "650", "900")),
        Rule060KombinasjonInvesteringKontoklasseFunksjonArt(),
        Rule065KombinasjonBevilgningFunksjonArt(),
        Rule070KombinasjonBevilgningFunksjonArt(),
        Rule075KombinasjonBevilgningFunksjonArt(),
        Rule080KombinasjonBevilgningFunksjonArt(),
        Rule085SummeringInvesteringUtgiftsposteringer(),
        Rule090SummeringInvesteringInntektsposteringer(),
        Rule095SummeringInvesteringDifferanse(),
        Rule100SummeringDriftUtgiftsposteringer(),
        Rule105SummeringDriftInntektsposteringer(),
        Rule110SummeringDriftDifferanse(),
        Rule115SummeringBalanseAktiva(),
        Rule120SummeringBalansePassiva(),
        Rule125SummeringBalanseDifferanse(),
        Rule126SummeringDriftOsloInternDifferanse(),
        Rule127SummeringInvesteringOsloInternDifferanse(),
        Rule130SkatteInntekter(),
        Rule135Rammetilskudd(),
        Rule140OverforingerDriftInvestering(),
        Rule145AvskrivningerMotpostAvskrivninger(),
        Rule150Avskrivninger(),
        Rule155AvskrivningerDifferanse(),
        Rule160AvskrivningerAndreFunksjoner(),
        Rule165AvskrivningerMotpostAvskrivningerAndreFunksjoner(),
        Rule170Funksjon290Investering(),
        Rule175Funksjon290Drift(),
        Rule180Funksjon465Investering(),
        Rule185Funksjon465Drift(),
        Rule190Memoriakonti(),
    )

    fun validate(): List<ValidationReportEntry> {
        val fatalValidationReportEntries: List<ValidationReportEntry> = fatalRules
            .mapNotNull { it.validate(arguments.getInputContentAsStringList()) }
            .flatten()

        val fatalSeverity: Severity = fatalValidationReportEntries
            .map { it.severity }
            .maxByOrNull { it.ordinal } ?: Severity.OK

        if (fatalSeverity == Severity.FATAL)
            return fatalValidationReportEntries

        val derivedKostraRecords = arguments
            .getInputContentAsStringList()
            .withIndex()
            .map { (index, recordString) ->
                recordString.toKostraRecord(
                    index = index + 1,
                    fieldDefinitions = getFieldDefinitionsMergedWithKotlinArguments(arguments)
                )
            }

        return validationRules
            .mapNotNull { it.validate(derivedKostraRecords) }
            .flatten()
    }
}
