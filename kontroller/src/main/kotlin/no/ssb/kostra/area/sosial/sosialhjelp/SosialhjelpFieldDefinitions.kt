package no.ssb.kostra.area.sosial.sosialhjelp

import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.ARBSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_9_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BIDRAG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BOSIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.DISTRIKTSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.GITT_OKONOMIRAD_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_10_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_11_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_12_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_1_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_2_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_3_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_4_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_5_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_6_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_7_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_8_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_9_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.LAAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_DUF_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SANKSJONANDRE_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.SANKSJONRED_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.TRYGDESIT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.UTBETDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.UTBETTOMDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VEDTAKAKT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VEDTAKARB_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VEDTAKDATO_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VEDTAKGRUNN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VEDTAKHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARANNET_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARARBEID_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARDIGPLAN_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARHELSE_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARJOBBLOG_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARJOBBTILB_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARKURS_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARLIVSH_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKAROKRETT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSAMT_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARUTD_COL_NAME
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VKLO_COL_NAME
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.DATE6_PATTERN
import no.ssb.kostra.program.DataType.DATE_TYPE
import no.ssb.kostra.program.DataType.STRING_TYPE
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinition.Companion.yesNoCodeList
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.extension.buildFieldDefinitions

object SosialhjelpFieldDefinitions : FieldDefinitions {
    override val fieldDefinitions: List<FieldDefinition> = listOf(
        FieldDefinition(
            name = KOMMUNE_NR_COL_NAME,
            description = "Kommunenummer",
            dataType = STRING_TYPE,
            size = 4
        ),
        FieldDefinition(
            name = VERSION_COL_NAME,
            description = "Rapporteringsår / versjon / oppgaveår",
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            name = BYDELSNR_COL_NAME,
            description = "Bydelsnummer",
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            name = DISTRIKTSNR_COL_NAME,
            description = "Distriktsnummer",
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            name = PERSON_JOURNALNR_COL_NAME,
            description = "Journalnummer",
            dataType = STRING_TYPE,
            size = 8
        ),
        FieldDefinition(
            name = PERSON_FODSELSNR_COL_NAME,
            description = "Hva er mottakerens fødselsnummer?",
            dataType = STRING_TYPE,
            size = 11
        ),
        FieldDefinition(
            name = PERSON_DUF_COL_NAME,
            description = "Hva er mottakerens DUF-nummer?",
            dataType = STRING_TYPE,
            size = 12
        ),
        FieldDefinition(
            name = KJONN_COL_NAME,
            description = "Hva er mottakerens kjønn?",
            codes = listOf(
                Code("1", "Mann"),
                Code("2", "Kvinne")
            )
        ),
        FieldDefinition(
            name = EKTSTAT_COL_NAME,
            description = "Hva er mottakerens sivilstand/sivilstatus ved siste kontakt?",
            codes = listOf(
                Code("1", "Ugift"),
                Code("2", "Gift"),
                Code("3", "Samboer"),
                Code("4", "Skilt/separert"),
                Code("5", "Enke/enkemann")
            )
        ),
        FieldDefinition(
            name = HAR_BARN_UNDER_18_COL_NAME,
            description = "Har mottakeren barn under 18 år som mottakeren (eventuelt ektefelle/samboer) har forsørgerplikt for og som bor i husholdningen?",
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = ANT_BARN_UNDER_18_COL_NAME,
            description = "Hvis ja, hvor mange?",
            size = 2
        ),
        FieldDefinition(
            name = VKLO_COL_NAME,
            description = "Hva er mottakerens viktigste kilde til livsopphold ved siste kontakt?",
            codes = listOf(
                Code("1", "Arbeidsinntekt"),
                Code("2", "Kursstønad/lønn i arbeidsmarkedstiltak"),
                Code("3", "Trygd/pensjon"),
                Code("4", "Stipend/lån"),
                Code("5", "Sosialhjelp"),
                Code("6", "Introduksjonsstøtte"),
                Code("7", "Ektefelle/samboers arbeidsinntekt"),
                Code("8", "Kvalifiseringsstønad"),
                Code("9", "Annen inntekt")
            )
        ),
        FieldDefinition(
            name = TRYGDESIT_COL_NAME,
            description = "Oppgi trygd/pensjon som utgjør størst økonomisk verdi ved siste kontakt",
            size = 2,
            codes = listOf(
                Code("01", "Sykepenger"),
                Code("02", "Dagpenger"),
                Code("04", "Uføretrygd"),
                Code("05", "Overgangsstønad"),
                Code("06", "Etterlattepensjon"),
                Code("07", "Alderspensjon"),
                Code("09", "Supplerende stønad (kort botid)"),
                Code("10", "Annen trygd"),
                Code("11", "Arbeidsavklaringspenger"),
                Code("12", "Har ingen trygd/pensjon")
            )
        ),
        FieldDefinition(
            name = ARBSIT_COL_NAME,
            description = "Hva er mottakerens viktigste arbeidssituasjon/livssituasjon ved siste kontakt?",
            size = 2,
            codes = listOf(
                Code("01", "Arbeid, heltid"),
                Code("02", "Arbeid, deltid"),
                Code("03", "Under utdanning"),
                Code("04", "Ikke arbeidssøker"),
                Code("05", "Arbeidsmarkedstiltak (statlig)"),
                Code("06", "Kommunalt tiltak"),
                Code("07", "Registrert arbeidsledig"),
                Code("08", "Arbeidsledig, men ikke registrert hos NAV"),
                Code("09", "Introduksjonsordning"),
                Code("10", "Kvalifiseringsprogram")
            )
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_1_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i januar?",
            size = 2,
            code = Code("01", "Januar")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_2_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i februar?",
            size = 2,
            code = Code("02", "Februar")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_3_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i mars?",
            size = 2,
            code = Code("03", "Mars")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_4_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i april?",
            size = 2,
            code = Code("04", "April")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_5_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i mai?",
            size = 2,
            code = Code("05", "Mai")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_6_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i juni?",
            size = 2,
            code = Code("06", "Juni")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_7_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i juli?",
            size = 2,
            code = Code("07", "Juli")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_8_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i august?",
            size = 2,
            code = Code("08", "August")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_9_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i september?",
            size = 2,
            code = Code("09", "September")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_10_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i oktober?",
            size = 2,
            code = Code("10", "Oktober")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_11_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i november?",
            size = 2,
            code = Code("11", "November")
        ),
        FieldDefinition(
            name = SosialhjelpColumnNames.STMND_12_COL_NAME,
            description = "Har mottakeren fått økonomisk stønad i desember?",
            size = 2,
            code = Code("12", "Desember")
        ),
        FieldDefinition(
            name = BIDRAG_COL_NAME,
            description = "Bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_COL_NAME,
            description = "Lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_1_COL_NAME,
            description = "Hva fikk mottakeren utbetalt i den enkelte måned? Januar, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_1_COL_NAME,
            description = "Januar, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_2_COL_NAME,
            description = "Februar, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_2_COL_NAME,
            description = "Februar, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_3_COL_NAME,
            description = "Mars, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_3_COL_NAME,
            description = "Mars, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_4_COL_NAME,
            description = "April, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_4_COL_NAME,
            description = "April, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_5_COL_NAME,
            description = "Mai, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_5_COL_NAME,
            description = "Mai, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_6_COL_NAME,
            description = "Juni, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_6_COL_NAME,
            description = "Juni, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_7_COL_NAME,
            description = "Juli, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_7_COL_NAME,
            description = "Juli, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_8_COL_NAME,
            description = "August, bidrag",
            size = 7
        ),
        FieldDefinition(
            description = "August, lån",
            name = LAAN_8_COL_NAME,
            size = 7
        ),
        FieldDefinition(
            description = "September, bidrag",
            name = BIDRAG_9_COL_NAME,
            size = 7
        ),
        FieldDefinition(
            name = LAAN_9_COL_NAME,
            description = "September, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_10_COL_NAME,
            description = "Oktober, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_10_COL_NAME,
            description = "Oktober, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_11_COL_NAME,
            description = "November, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_11_COL_NAME,
            description = "November, lån",
            size = 7
        ),
        FieldDefinition(
            name = BIDRAG_12_COL_NAME,
            description = "Desember, bidrag",
            size = 7
        ),
        FieldDefinition(
            name = LAAN_12_COL_NAME,
            description = "Desember, lån",
            size = 7
        ),
        FieldDefinition(
            name = GITT_OKONOMIRAD_COL_NAME,
            description = "Er det gitt økonomisk rådgivning på nivå II (midlertidig betalingsudyktighet) eller III (varig betalingsudyktighet) i forbindelse med utbetaling? (Se NAVs veileder til bruk ved økonomisk rådgivning)",
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = FAAT_INDIVIDUELL_PLAN_COL_NAME,
            description = "Har mottakeren fått utarbeidet individuell plan (lov om sosiale tjenester i NAV § 28)?",
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = SAKSBEHANDLER_COL_NAME,
            description = "Saksbehandlernummer",
            dataType = STRING_TYPE,
            size = 10
        ),
        FieldDefinition(
            name = BOSIT_COL_NAME,
            description = "Hva er mottakerens bosituasjon ved siste kontakt?",
            codes = listOf(
                Code("1", "Bor i leid privat bolig"),
                Code("2", "Bor i leid kommunal bolig"),
                Code("3", "Bor i eid bolig"),
                Code("4", "Er uten bolig"),
                Code("5", "Annet"),
                Code("6", "Bor i institusjon")
            ),
        ),
        FieldDefinition(
            name = VILKARSOSLOV_COL_NAME,
            description = "Stilles det vilkår til mottakeren etter sosialtjenesteloven?",
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = VILKARSAMEKT_COL_NAME,
            description = "Stilles det vilkår til søkerens samboer/ektefelle etter sosialtjenesteloven?",
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = UTBETDATO_COL_NAME,
            description = "Oppgi utbetalingsvedtakets dato (DDMMÅÅ)",
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN,
        ),
        FieldDefinition(
            name = UTBETTOMDATO_COL_NAME,
            description = "Oppgi utbetalingsvedtakets til og med dato (DDMMÅÅ)",
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN
        ),
        FieldDefinition(
            name = VILKARARBEID_COL_NAME,
            description = "Oppgi hvilke vilkår det stilles til mottakeren. Flere kryss mulig",
            size = 2,
            code = Code("16", "Delta på arbeidstrening/arbeidspraksis")
        ),
        FieldDefinition(
            name = VILKARKURS_COL_NAME,
            size = 2,
            code = Code("17", "Delta på arbeidsrettede kurs, opplæring eller jobbsøkingskurs")
        ),
        FieldDefinition(
            name = VILKARUTD_COL_NAME,
            size = 2,
            code = Code("04", "Benytte rett til skole")
        ),
        FieldDefinition(
            name = VILKARJOBBLOG_COL_NAME,
            size = 2,
            code = Code("06", "Registrere seg som arbeidssøker/føre jobblogg")
        ),
        FieldDefinition(
            name = VILKARJOBBTILB_COL_NAME,
            size = 2,
            code = Code("07", "Ta imot et konkret jobbtilbud")
        ),
        FieldDefinition(
            name = VILKARSAMT_COL_NAME,
            size = 2,
            code = Code("08", "Møte til veiledningssamtaler")
        ),
        FieldDefinition(
            name = VILKAROKRETT_COL_NAME,
            size = 2,
            code = Code("10", "Gjøre gjeldende andre økonomiske rettigheter")
        ),
        FieldDefinition(
            name = VILKARLIVSH_COL_NAME,
            size = 2,
            code = Code("18", "Realisere formuesgoder/ redusere boutgifter")
        ),
        FieldDefinition(
            name = VILKARHELSE_COL_NAME,
            size = 2,
            code = Code("14", "Oppsøke helsetjenester/ lege")
        ),
        FieldDefinition(
            name = VILKARANNET_COL_NAME,
            size = 2,
            code = Code("15", "Annet")
        ),
        FieldDefinition(
            name = VILKARDIGPLAN_COL_NAME,
            description = "Bruke og følge opp digital aktivitetsplan",
            size = 2,
            code = Code("19", "Bruke og følge opp digital aktivitetsplan")
        ),
        FieldDefinition(
            name = VEDTAKDATO_COL_NAME,
            description = "Unntak for mottaker under 30 år. Begrunnelse for første unntak fra sosialtjenestelovens § 20a for mottaker under 30 år. Oppgi vedtaksdato (DDMMÅÅ)",
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN,
        ),
        FieldDefinition(
            name = VEDTAKARB_COL_NAME,
            description = "Begrunnelse for første unntak fra sosialtjenestelovens § 20a",
            size = 2,
            code = Code("01", "Mottaker er i arbeid")
        ),
        FieldDefinition(
            name = VEDTAKAKT_COL_NAME,
            size = 2,
            code = Code(
                "02",
                "Mottaker er allerede i aktivitet knyttet til mottak av statlig eller annen kommunal ytelse"
            )
        ),
        FieldDefinition(
            name = VEDTAKHELSE_COL_NAME,
            size = 2,
            code = Code("03", "Mottakers helsemessige eller sosiale situasjon hindrer deltakelse i aktivitet")
        ),
        FieldDefinition(
            name = VEDTAKGRUNN_COL_NAME,
            size = 2,
            code = Code("04", "Andre tungtveiende grunner taler mot at det stilles vilkår om aktivitet")
        ),
        FieldDefinition(
            name = SANKSJONRED_COL_NAME,
            description = "Sanksjonering av mottaker. Sanksjon i løpet av året som følge av brudd på vilkår etter sosialtjenestelovens § 20a",
            size = 2,
            code = Code("01", "Redusert stønad")
        ),
        FieldDefinition(
            name = SANKSJONANDRE_COL_NAME,
            size = 2,
            code = Code("02", "Andre konsekvenser")
        )
    ).buildFieldDefinitions()
}