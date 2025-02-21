package no.ssb.kostra.area.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
import no.ssb.kostra.area.regnskap.RegnskapValidator
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.rule.regnskap.kirkekostra.*
import no.ssb.kostra.validation.rule.regnskap.kostra.*

class KirkeKostraMain(
    arguments: KotlinArguments
) : RegnskapValidator(arguments) {

    private val funksjoner = listOf(
        //@formatter:off
        "041", "042", "043", "044", "045",
        "089"
        // @formatter:on
    )

    override val funksjonList: List<String> =
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == ACCOUNTING_TYPE_BEVILGNING
            }
        ) emptyList()
        else funksjoner.map { it.padEnd(4, ' ') }.sorted()

    private val kapitler = listOf(
        "10", "11", "12", "13", "18",
        "21", "22", "24", "27",
        "31", "32",
        "41", "43", "45",
        "51", "53", "55", "56", "580", "581", "5900", "5950", "5960", "5970", "5990",
        "9100", "9200", "9999"
    )

    // Kapitler
    override val kapittelList: List<String> =
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BALANSE
            }
        ) emptyList()
        else kapitler.map { it.padEnd(4, ' ') }.sorted()


    // Arter
    private val arter = listOf(
        // @formatter:off
        "010", "020", "030", "040", "050", "060", "080", "090", "095", "099",
        "100", "110", "120", "130", "140", "150", "155", "160", "165", "170", "180", "185", "190", "195",
        "200", "210", "220", "230", "240", "250", "260", "265", "270", "280", "285",
        "300", "305", "330", "340", "350", "370", "380", "390",
        "400", "405", "429", "430", "440", "450", "465", "470",
        "500", "510", "520", "530", "540", "550", "570", "580", "590",
        "600", "610", "620", "630", "650", "660", "670",
        "700", "705", "710", "729", "730", "740", "750", "770", "780", "790",
        "800", "805", "830", "840", "850", "860", "865", "870",
        "900", "905", "910", "920", "930", "940", "950", "970", "980", "990"
        // @formatter:on
    )

    override val artList: List<String> = if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
            it in setOf(
                ACCOUNTING_TYPE_BEVILGNING,
                ACCOUNTING_TYPE_REGIONALE
            )
        }
    ) emptyList()
    else arter.sorted()

    override val sektorList: List<String> =
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BALANSE
            }
        ) emptyList()
        else listOf("   ") // Sektorer

    // Kun gyldig i investering og skal fjernes fra drift
    private fun getInvalidDriftArtList() = listOf("280", "285", "670", "910", "970")

    // Kun gyldig i drift og skal fjernes fra investering
    private fun getInvalidInvesteringArtList() = listOf("570", "590", "990")

    private fun getValidFunksjon089ArtList() = artList
        .filter { art ->
            art.toInt() in (500..580).plus(830).plus(900..980)
        }


    override val validationRules = commonValidationRules()
        .plus(
            listOf(
                Rule025KombinasjonDriftKontoklasseArt(invalidDriftArtList = getInvalidDriftArtList()),
                Rule050KombinasjonInvesteringKontoklasseArt(invalidInvesteringArtList = getInvalidInvesteringArtList()),
                Rule095SummeringInvesteringDifferanse(),
                Rule100SummeringDriftUtgiftsposteringer(),
                Rule105SummeringDriftInntektsposteringer(),
                Rule110SummeringDriftDifferanse(),
                Rule113SummeringTilskudd(),
                Rule115SummeringBalanseAktiva(),
                Rule120SummeringBalansePassiva(),
                Rule125SummeringBalanseDifferanse(),
                Rule140OverforingerDriftInvestering(),
                Rule143Avskrivninger(),
                Rule200Funksjon089Finansieringstransaksjoner(getValidFunksjon089ArtList()),
                Rule210InterneOverforingerKjopOgSalg(),
                Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekter(),
                Rule220InterneOverforingerMidler(),
            )
        )
}
