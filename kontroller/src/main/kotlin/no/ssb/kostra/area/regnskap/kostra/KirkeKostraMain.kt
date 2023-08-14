package no.ssb.kostra.area.regnskap.kostra

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.mappingDuplicates
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.Validator
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.regnskap.*
import no.ssb.kostra.validation.rule.regnskap.kirkekostra.*
import no.ssb.kostra.validation.rule.regnskap.kostra.*

class KirkeKostraMain(
    arguments: KotlinArguments
) : Validator(arguments) {

    private val funksjoner = listOf(
        //@formatter:off
        "041", "042", "043", "044", "045",
        "089"
        // @formatter:on
    )

    private fun getFunksjonAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
            }
        ) return emptyList()

        return funksjoner.map { it.padEnd(4, ' ') }.sorted()
    }

    private val kapitler = listOf(
        "10", "11", "12", "13", "18",
        "21", "22", "24", "27",
        "31", "32",
        "41", "43", "45",
        "51", "53", "55", "56", "580", "581", "5900", "5950", "5960", "5970", "5990",
        "9100", "9200", "9999"
    )

    // Kapitler
    private fun getKapittelAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BALANSE
            }
        ) return emptyList()

        return kapitler.map { it.padEnd(4, ' ') }.sorted()
    }


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

    private fun getArtAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it in listOf(
                    RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING,
                    RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
                )
            }
        ) return emptyList()

        return arter.sorted()
    }

    private fun getSektorAsList(): List<String> {
        if (RegnskapConstants.getRegnskapTypeBySkjema(arguments.skjema).none {
                it == RegnskapConstants.ACCOUNTING_TYPE_BALANSE
            }
        )
            return emptyList()

        // Sektorer
        return listOf("   ")
    }

    // Kun gyldig i investering og skal fjernes fra drift
    private fun getInvalidDriftArtList() = listOf("280", "285", "670", "910", "970")

    // Kun gyldig i drift og skal fjernes fra investering
    private fun getInvalidInvesteringArtList() = listOf("570", "590", "990")

    override val fieldDefinitions: FieldDefinitions = RegnskapFieldDefinitions

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
        Rule200Funksjon089Finansieringstransaksjoner(),
        Rule210InterneOverforingerKjopOgSalg(),
        Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekter(),
        Rule220InterneOverforingerMidler(),
    )
}
