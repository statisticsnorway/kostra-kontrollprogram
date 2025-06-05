package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.PositionedFileValidator
import no.ssb.kostra.validation.report.StatsEntry
import no.ssb.kostra.validation.report.StatsEntryHeading
import no.ssb.kostra.validation.report.StatsReportEntry
import no.ssb.kostra.validation.rule.Rule000HasAttachment
import no.ssb.kostra.validation.rule.Rule001RecordLength
import no.ssb.kostra.validation.rule.sosial.extensions.varighetAsStatsEntries
import no.ssb.kostra.validation.rule.sosial.rule.*
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.extensions.deltakereByAlderAsStatsEntries
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.extensions.stonadAsStatsEntries
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule.*

class SosialhjelpMain(arguments: KotlinArguments) : PositionedFileValidator(arguments) {
    override val fieldDefinitions = SosialhjelpFieldDefinitions

    override val preValidationRules = listOf(
        Rule000HasAttachment(),
        Rule001RecordLength(SosialhjelpFieldDefinitions.fieldLength)
    )

    override val validationRules = listOf(
        Rule003Kommunenummer(),
        Rule003Bydelsnummer(),
        Rule004OppgaveAar(),
        Rule004AFodselsDato(),
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
        Rule020AViktigsteKildeTilLivsOppholdKode3(),
        Rule020BViktigsteKildeTilLivsOppholdKode3(),
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

    override fun createStats(kostraRecordList: List<KostraRecord>): List<StatsReportEntry> = Pair(
        kostraRecordList.sumOf { it.fieldAsIntOrDefault(BIDRAG_COL_NAME) },
        kostraRecordList.sumOf { it.fieldAsIntOrDefault(LAAN_COL_NAME) }
    ).let { (sumBidrag, sumLaan) ->
        listOf(
            StatsReportEntry(
                heading = StatsEntryHeading("Stønad", "Sum"),
                entries = listOf(
                    StatsEntry("I alt", "${sumBidrag + sumLaan}"),
                    StatsEntry("Bidrag", "$sumBidrag"),
                    StatsEntry("Lån", "$sumLaan")
                )
            ),
            StatsReportEntry(
                heading = StatsEntryHeading("Alder", "Deltakere"),
                entries = kostraRecordList.deltakereByAlderAsStatsEntries(arguments)
            ),
            StatsReportEntry(
                heading = StatsEntryHeading("Stønadsvarighet", "Deltakere"),
                entries = kostraRecordList.varighetAsStatsEntries()
            ),
            StatsReportEntry(
                heading = StatsEntryHeading("Stønad", "Deltakere"),
                entries = kostraRecordList.stonadAsStatsEntries()
            )
        )
    }
}