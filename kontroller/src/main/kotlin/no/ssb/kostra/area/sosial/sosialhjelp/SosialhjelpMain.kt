package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.codeExists
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.sosial.extensions.ageInYears
import no.ssb.kostra.validation.rule.sosial.extensions.hasFnr
import no.ssb.kostra.validation.rule.sosial.rule.*
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule.*

class SosialhjelpMain(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = SosialhjelpFieldDefinitions

    override val preValidationRules = listOf(
        Rule001RecordLength(SosialhjelpFieldDefinitions.fieldLength)
    )

    override val validationRules = listOf(
        Rule003Kommunenummer(),
        Rule003Bydelsnummer(),
        Rule004OppgaveAar(),
        Rule005Fodselsnummer(),
        Rule005aFoedselsnummerDubletter(),
        Rule005bJournalnummerDubletter(),
        Rule006AlderUnder18Aar(),
        Rule007AlderEr68AarEllerOver(),
        Rule008Kjonn(),
        Rule009Sivilstand(),
        Rule010HarBarnUnder18(),
        Rule011HarBarnUnder18MotAntallBarnUnder18(),
        Rule012AntallBarnUnder18MotHarBarnUnder18(),
        Rule013AntallBarnUnder18(),

        Rule014ViktigsteKildeTilLivsOppholdGyldigeVerdier(),
        Rule015ViktigsteKildeTilLivsOppholdKode1(),
        Rule016ViktigsteKildeTilLivsOppholdKode2(),
        Rule017ViktigsteKildeTilLivsOppholdKode4(),
        Rule018ViktigsteKildeTilLivsOppholdKode6(),
        Rule019ViktigsteKildeTilLivsOppholdKode8(),
        Rule020ViktigsteKildeTilLivsOppholdKode3(),
        Rule021ViktigsteKildeTilLivsOppholdKode5(),
        Rule022TilknytningTilTrygdesystemetOgAlder(),
        Rule023TilknytningTilTrygdesystemetOgBarn(),
        Rule024TilknytningTilTrygdesystemetOgArbeidssituasjon(),
        Rule024BTilknytningTilTrygdesystemetOgArbeidssituasjon(),
        Rule025ArbeidssituasjonGyldigeKoder(),
        Rule026Stonadsmaaneder(),
        Rule027StonadssumMangler(),
        Rule030StonadssumMaksimum(),
        Rule031StonadssumMinimum(),
        Rule032OkonomiskRaadgivningGyldigeKoder(),
        Rule033UtarbeidelseAvIndividuellPlanGyldigeKoder(),
        Rule035BoligsituasjonGyldigeKoder(),
        Rule036BidragSum(),
        Rule037LaanSum(),
        Rule038DufNummer(),
        Rule039Vilkar(),
        Rule040VilkarSamEkt(),
        Rule041DatoForUtbetalingsvedtak(),
        Rule042TilOgMedDatoForUtbetalingsvedtak(),
        Rule043UtfyltVilkar()
    )

    override fun createStats(kostraRecordList: List<KostraRecord>): List<StatsReportEntry> {
        val sumBidrag = kostraRecordList.sumOf { it.fieldAsIntOrDefault(BIDRAG_COL_NAME) }
        val sumLaan = kostraRecordList.sumOf { it.fieldAsIntOrDefault(LAAN_COL_NAME) }
        val sumStonad = sumBidrag + sumLaan
        val stonad = "STONAD"

        val ageList = kostraRecordList
            .filter { it.hasFnr() }
            .map { it.ageInYears(arguments) }

        val stonadstidList = kostraRecordList
            .map { kostraRecord ->
                (1..12)
                    .map {
                        "STMND_$it"
                    }.count { fieldName ->
                        kostraRecord.fieldDefinition(fieldName).codeExists(kostraRecord[fieldName])
                    }
            }

        val stonadList = kostraRecordList
            .map { it.fieldAsIntOrDefault(BIDRAG_COL_NAME) + it.fieldAsIntOrDefault(LAAN_COL_NAME) }

        return listOf(
            StatsReportEntry(
                content = "Sum",
                codeList = listOf(
                    Code(stonad, "Stønad"),
                    Code(BIDRAG_COL_NAME, "Bidrag"),
                    Code(LAAN_COL_NAME, "Lån")
                ),
                statsEntryList = listOf(
                    StatsEntry(stonad, "$sumStonad"),
                    StatsEntry(BIDRAG_COL_NAME, "$sumBidrag"),
                    StatsEntry(LAAN_COL_NAME, "$sumLaan")
                )
            ),
            StatsReportEntry(
                content = "Tilfeller",
                codeList = listOf(
                    Code("I_ALT", "I alt"),
                    Code("0_17", "Under 18"),
                    Code("18_24", "18 - 24"),
                    Code("25_44", "25 - 44"),
                    Code("45_66", "45 - 66"),
                    Code("67_999", "67 og over"),
                    Code("UGYLDIG_FNR", "Ugyldig fnr")
                ),
                statsEntryList = listOf(
                    StatsEntry("I_ALT", kostraRecordList.size.toString()),
                    StatsEntry("0_17", ageList.count { it in 0..17 }.toString()),
                    StatsEntry("18_24", ageList.count { it in 18..24 }.toString()),
                    StatsEntry("25_44", ageList.count { it in 25..44 }.toString()),
                    StatsEntry("45_66", ageList.count { it in 45..66 }.toString()),
                    StatsEntry("67_999", ageList.count { it in 67..999 }.toString()),
                    StatsEntry("UGYLDIG_FNR", kostraRecordList.count { !it.hasFnr() }.toString()),
                )
            ),
            StatsReportEntry(
                content = "Stønadstid",
                codeList = listOf(
                    Code("1", "1 måned"),
                    Code("2_3", "2 - 3 måneder"),
                    Code("4_6", "4 - 6 måneder"),
                    Code("7_9", "7 - 9 måneder"),
                    Code("10_11", "10 - 11 måneder"),
                    Code("12", "12 måneder"),
                    Code("UOPPGITT", "Uoppgitt")
                ),
                statsEntryList = listOf(
                    StatsEntry("1", stonadstidList.count { it == 1 }.toString()),
                    StatsEntry("2_3", stonadstidList.count { it in 2..3 }.toString()),
                    StatsEntry("4_6", stonadstidList.count { it in 4..6 }.toString()),
                    StatsEntry("7_9", stonadstidList.count { it in 7..9 }.toString()),
                    StatsEntry("10_11", stonadstidList.count { it in 10..11 }.toString()),
                    StatsEntry("12", stonadstidList.count { it == 12 }.toString()),
                    StatsEntry("UOPPGITT", stonadstidList.count { it == 0 }.toString()),
                )
            ),
            StatsReportEntry(
                content = "Stønad",
                codeList = listOf(
                    Code("1_9", "1 - 9999"),
                    Code("10_49", "10000 - 49999"),
                    Code("50_99", "50000 - 99999"),
                    Code("100_149", "100000 - 149999"),
                    Code("150_9999", "150000 og over"),
                    Code("0", "Uoppgitt")
                ),
                statsEntryList = listOf(
                    StatsEntry("1_9", stonadList.count { it in 1..9_999 }.toString()),
                    StatsEntry("10_49", stonadList.count { it in 10_000..49_999 }.toString()),
                    StatsEntry("50_99", stonadList.count { it in 50_000..99_999 }.toString()),
                    StatsEntry("100_149", stonadList.count { it in 100_000..149_000 }.toString()),
                    StatsEntry("150_9999", stonadList.count { it in 150_000..9_999_999 }.toString()),
                    StatsEntry("0", stonadList.count { it == 0 }.toString()),
                )
            )
        )
    }
}