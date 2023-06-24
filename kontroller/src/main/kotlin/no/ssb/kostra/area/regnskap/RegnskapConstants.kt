package no.ssb.kostra.area.regnskap

object RegnskapConstants {
    const val FIELD_SKJEMA = "skjema"
    const val FIELD_AARGANG = "aargang"
    const val FIELD_KVARTAL = "kvartal"
    const val FIELD_REGION = "region"
    const val FIELD_ORGNR = "orgnr"
    const val FIELD_FORETAKSNR = "foretaksnr"
    const val FIELD_KONTOKLASSE = "kontoklasse"
    const val FIELD_FUNKSJON_KAPITTEL = "funksjon_kapittel"
    const val FIELD_FUNKSJON = FIELD_FUNKSJON_KAPITTEL
    const val FIELD_KAPITTEL = FIELD_FUNKSJON_KAPITTEL
    const val FIELD_ART_SEKTOR = "art_sektor"
    const val FIELD_ART = FIELD_ART_SEKTOR
    const val FIELD_SEKTOR = FIELD_ART_SEKTOR
    const val FIELD_BELOP = "belop"

//    const val DERIVED_ACCOUNTING_TYPE = "utledet_regnskapstype"
//    const val DERIVED_KONTOKLASSE = "utledet_kontoklasse"

//    const val TITLE_SKJEMA = "Skjema"
//    const val TITLE_AARGANG = "Årgang"
//    const val TITLE_KVARTAL = "Kvartal"
//    const val TITLE_REGION = "Region"
//    const val TITLE_ORGNR = "Organisasjonsnummer"
//    const val TITLE_FORETAKSNR = "Foretaksnummer"
    const val TITLE_KONTOKLASSE = "Kontoklasse"
//    const val TITLE_FUNKSJON_KAPITTEL = "Funksjon/Kapittel"
    const val TITLE_FUNKSJON = "Funksjon"
    const val TITLE_KAPITTEL = "Kapittel"
//    const val TITLE_ART_SEKTOR = "Art/Sektor"
    const val TITLE_ART = "Art"
    const val TITLE_SEKTOR = "Sektor"
//    const val TITLE_BELOP = "belop"

//    const val TITLE_DERIVED_ACCOUNTING_TYPE = "Utledet regnskapstype"
//    const val TITLE_DERIVED_KONTOKLASSE = "Utledet kontoklasse"

    const val ACCOUNTING_TYPE_BALANSE = "BAL"
    const val ACCOUNTING_TYPE_BEVILGNING = "BEV"
    const val ACCOUNTING_TYPE_REGIONALE = "REG"
    const val ACCOUNTING_TYPE_RESULTAT = "RES"

    const val ACCOUNT_TYPE_BALANSE = "B"
    const val ACCOUNT_TYPE_DRIFT = "D"
    const val ACCOUNT_TYPE_INVESTERING = "I"
    const val ACCOUNT_TYPE_RESULTAT = "R"

    const val REGION_FYLKE = "FYLKE"
    const val REGION_KOMMUNE = "KOMM"
    const val REGION_OSLO = "OSLO"
    const val REGION_SVALBARD = "SVALBARD"
    const val REGION_BYDEL = "BYDEL"
    const val REGION_SAERBEDRIFT = "IKS"
    const val REGION_LANEFOND = "LANEFOND"

    data class Mapping(
        val regnskapType: String,
        val skjema: String,
        val kontoType: String,
        val kontoklasse: String
    )

    fun mappingBasis(): List<Mapping> = listOf(
        // Kostra
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0A", ACCOUNT_TYPE_DRIFT, "1"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0A", ACCOUNT_TYPE_INVESTERING, "0"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0A", ACCOUNT_TYPE_DRIFT, "1"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0A", ACCOUNT_TYPE_INVESTERING, "0"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0B", ACCOUNT_TYPE_BALANSE, "2"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0C", ACCOUNT_TYPE_DRIFT, "1"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0C", ACCOUNT_TYPE_INVESTERING, "0"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0C", ACCOUNT_TYPE_DRIFT, "1"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0C", ACCOUNT_TYPE_INVESTERING, "0"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0D", ACCOUNT_TYPE_BALANSE, "2"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0I", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0I", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0J", ACCOUNT_TYPE_BALANSE, "5"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0K", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0K", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0L", ACCOUNT_TYPE_BALANSE, "5"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0M", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0M", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0M", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0M", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0N", ACCOUNT_TYPE_BALANSE, "5"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0P", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0P", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0P", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_REGIONALE, "0P", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0Q", ACCOUNT_TYPE_BALANSE, "5"),
        // Kirke
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0F", ACCOUNT_TYPE_DRIFT, "3"),
        Mapping(ACCOUNTING_TYPE_BEVILGNING, "0F", ACCOUNT_TYPE_INVESTERING, "4"),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0G", ACCOUNT_TYPE_BALANSE, "5"),
        // Helse
        Mapping(ACCOUNTING_TYPE_RESULTAT, "0X", ACCOUNT_TYPE_RESULTAT, " "),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0Y", ACCOUNT_TYPE_BALANSE, " "),

        )

    fun getRegnskapTypeBySkjema(skjema: String): List<String> =
        mappingBasis()
            .filter { it.skjema == skjema }
            .map { it.regnskapType }


/*
    private fun getSkjemaListByRegnskapType(regnskapType: String): List<String> =
        mappingBasis()
            .filter { it.regnskapType == regnskapType }
            .map { it.skjema }
*/

    fun getKontoklasseBySkjema(skjema: String): List<String> =
        mappingBasis()
            .filter { it.skjema == skjema }
            .map { it.kontoklasse }

/*
    fun getDriftKontoklasse(skjema: String): String =
        mappingBasis()
            .filter { it.skjema == skjema && it.kontoType == ACCOUNT_TYPE_DRIFT }
            .map { it.kontoklasse }
            .first()
*/

/*
    fun getInvesteringKontoklasse(skjema: String): String =
        mappingBasis()
            .filter { it.skjema == skjema && it.kontoType == ACCOUNT_TYPE_INVESTERING }
            .map { it.kontoklasse }
            .first()
*/

/*
    fun getBalanseKontoklasse(skjema: String): String =
        mappingBasis()
            .filter { it.skjema == skjema && it.kontoType == ACCOUNT_TYPE_BALANSE }
            .map { it.kontoklasse }
            .first()
*/


/*
    private fun getKontoTypeToKontoklasseBySkjema(skjema: String): Map<String, String> =
        mappingBasis()
            .filter { it.skjema == skjema }
            .associate { it.kontoType to it.kontoklasse }
*/

    fun getKontoTypeBySkjemaAndKontoklasse(skjema: String, kontoklasse: String): String =
        mappingBasis()
            .filter { it.skjema == skjema && it.kontoklasse == kontoklasse }
            .map { it.kontoType }
            .first()
}

