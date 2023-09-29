package no.ssb.kostra.area.regnskap

import no.ssb.kostra.program.KotlinArguments

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

    internal const val TITLE_ORGNR = "Organisasjonsnummer"
    internal const val TITLE_KONTOKLASSE = "Kontoklasse"
    internal const val TITLE_FUNKSJON = "Funksjon"
    internal const val TITLE_KAPITTEL = "Kapittel"
    internal const val TITLE_ART = "Art"
    internal const val TITLE_SEKTOR = "Sektor"

    const val ACCOUNTING_TYPE_BALANSE = "BAL"
    const val ACCOUNTING_TYPE_BEVILGNING = "BEV"
    const val ACCOUNTING_TYPE_REGIONALE = "REG"
    const val ACCOUNTING_TYPE_RESULTAT = "RES"

    private const val ACCOUNT_TYPE_BALANSE = "B"
    const val ACCOUNT_TYPE_DRIFT = "D"
    const val ACCOUNT_TYPE_INVESTERING = "I"
    private const val ACCOUNT_TYPE_RESULTAT = "R"

    val osloKommuner = listOf(
        // @formatter:off
        "030100",
        "030101", "030102", "030103", "030104", "030105",
        "030106", "030107", "030108", "030109", "030110",
        "030111", "030112", "030113", "030114", "030115"
        // @formatter:on
    )

    data class Mapping(
        val regnskapType: String,
        val skjema: String,
        val kontoType: String,
        val kontoklasse: String
    )

    private val mappingBasis: List<Mapping> = sequenceOf(
        // Helse
        Mapping(ACCOUNTING_TYPE_RESULTAT, "0X", ACCOUNT_TYPE_RESULTAT, " "),
        Mapping(ACCOUNTING_TYPE_BALANSE, "0Y", ACCOUNT_TYPE_BALANSE, " "),
    )
        // Kostra og Kirke
        .plus(
            listOf("0A", "0C")
                .flatMap { skjema ->
                    listOf(
                        Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_DRIFT, "1"),
                        Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_INVESTERING, "0"),
                        Mapping(ACCOUNTING_TYPE_REGIONALE, skjema, ACCOUNT_TYPE_DRIFT, "1"),
                        Mapping(ACCOUNTING_TYPE_REGIONALE, skjema, ACCOUNT_TYPE_INVESTERING, "0"),
                    )
                }
        ).plus(
            listOf("0F", "0I", "0K").flatMap { skjema ->
                listOf(
                    Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_DRIFT, "3"),
                    Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_INVESTERING, "4"),
                )
            }
        ).plus(
            listOf("0M", "0P").flatMap { skjema ->
                listOf(
                    Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_DRIFT, "3"),
                    Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_INVESTERING, "4"),
                    Mapping(ACCOUNTING_TYPE_REGIONALE, skjema, ACCOUNT_TYPE_DRIFT, "3"),
                    Mapping(ACCOUNTING_TYPE_REGIONALE, skjema, ACCOUNT_TYPE_INVESTERING, "4"),
                )
            }
        ).plus(
            listOf("0B", "0D").map { skjema ->
                Mapping(ACCOUNTING_TYPE_BALANSE, skjema, ACCOUNT_TYPE_BALANSE, "2")
            }
        ).plus(
            listOf("0G", "0J", "0L", "0N", "0Q").map { skjema ->
                Mapping(ACCOUNTING_TYPE_BALANSE, skjema, ACCOUNT_TYPE_BALANSE, "5")
            }
        )
        // Kvartal
        .plus(
            listOf(
                "0AK1", "0AK2", "0AK3", "0AK4",
                "0CK1", "0CK2", "0CK3", "0CK4",
            ).flatMap { skjema ->
                listOf(
                    Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_DRIFT, "1"),
                    Mapping(ACCOUNTING_TYPE_BEVILGNING, skjema, ACCOUNT_TYPE_INVESTERING, "0"),
                )
            }
        ).plus(
            listOf(
                "0BK1", "0BK2", "0BK3", "0BK4",
                "0DK1", "0DK2", "0DK3", "0DK4",
            ).flatMap { skjema ->
                listOf(
                    Mapping(ACCOUNTING_TYPE_BALANSE, skjema, ACCOUNT_TYPE_BALANSE, "2"),
                    Mapping(ACCOUNTING_TYPE_BALANSE, skjema, ACCOUNT_TYPE_BALANSE, "Z"),
                    Mapping(ACCOUNTING_TYPE_BALANSE, skjema, ACCOUNT_TYPE_BALANSE, "z"),
                    Mapping(ACCOUNTING_TYPE_BALANSE, skjema, ACCOUNT_TYPE_BALANSE, "~"),
                )
            }
        )
        .toList()


    fun getRegnskapTypeBySkjema(skjema: String): List<String> =
        mappingBasis
            .filter { it.skjema == skjema }
            .map { it.regnskapType }
            .distinct()


    fun getKontoklasseBySkjema(skjema: String): List<String> =
        mappingBasis
            .filter { it.skjema == skjema }
            .map { it.kontoklasse }
            .distinct()

    fun getKontoTypeBySkjemaAndKontoklasse(skjema: String, kontoklasse: String): String =
        mappingBasis
            .filter { it.skjema == skjema && it.kontoklasse == kontoklasse }
            .map { it.kontoType }
            .first()

    fun mappingDuplicates(arguments: KotlinArguments): Pair<List<String>, List<String>> =
        when (getRegnskapTypeBySkjema(arguments.skjema).firstOrNull()) {
            ACCOUNTING_TYPE_BEVILGNING, ACCOUNTING_TYPE_REGIONALE ->
                listOf(
                    FIELD_KONTOKLASSE,
                    FIELD_FUNKSJON,
                    FIELD_ART
                ) to listOf(
                    TITLE_KONTOKLASSE,
                    TITLE_FUNKSJON,
                    TITLE_ART
                )

            ACCOUNTING_TYPE_BALANSE ->
                listOf(
                    FIELD_KONTOKLASSE,
                    FIELD_KAPITTEL,
                    FIELD_SEKTOR
                ) to listOf(
                    TITLE_KONTOKLASSE,
                    TITLE_KAPITTEL,
                    TITLE_SEKTOR
                )

            ACCOUNTING_TYPE_RESULTAT ->
                listOf(
                    FIELD_ORGNR,
                    FIELD_KONTOKLASSE,
                    FIELD_KAPITTEL,
                    FIELD_SEKTOR
                ) to listOf(
                    TITLE_ORGNR,
                    TITLE_KONTOKLASSE,
                    TITLE_KAPITTEL,
                    TITLE_SEKTOR
                )

            else -> emptyList<String>() to emptyList()
        }

}

