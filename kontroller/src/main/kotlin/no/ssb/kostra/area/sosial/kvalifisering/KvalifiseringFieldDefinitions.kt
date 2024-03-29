package no.ssb.kostra.area.sosial.kvalifisering

import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.sosial.SosialCommon.calendarMonths
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_AAP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ANNET_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ARBLONNSTILS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ARBMARK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_OK_AVKLAR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_ORDINAERTARB_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_SKOLE_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_UFORE_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_UKJENT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_UTEN_OK_AVKLAR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_VIKTIGSTE_INNTEKT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.DISTRIKTSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VEDTAK_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_FOLKETRYGDL_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_INDIVIDSTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_INTRO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.FULLFORT_PROGRAM
import no.ssb.kostra.program.Code
import no.ssb.kostra.program.DATE6_PATTERN
import no.ssb.kostra.program.DataType.DATE_TYPE
import no.ssb.kostra.program.DataType.STRING_TYPE
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.FieldDefinition.Companion.yesNoCodeList
import no.ssb.kostra.program.FieldDefinitions
import no.ssb.kostra.program.extension.buildFieldDefinitions

object KvalifiseringFieldDefinitions : FieldDefinitions {

    override val fieldDefinitions: List<FieldDefinition> = listOf(
        FieldDefinition(
            name = KOMMUNE_NR_COL_NAME,
            dataType = STRING_TYPE,
            size = 4,
            mandatory = true
        ),
        FieldDefinition(
            name = VERSION_COL_NAME,
            dataType = STRING_TYPE,
            size = 2,
            mandatory = true
        ),
        FieldDefinition(
            name = BYDELSNR_COL_NAME,
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            name = DISTRIKTSNR_COL_NAME,
            dataType = STRING_TYPE,
            size = 2
        ),
        FieldDefinition(
            name = PERSON_JOURNALNR_COL_NAME,
            dataType = STRING_TYPE,
            size = 8,
            mandatory = true
        ),
        FieldDefinition(
            name = PERSON_FODSELSNR_COL_NAME,
            dataType = STRING_TYPE,
            size = 11,
            mandatory = true
        ),
        FieldDefinition(
            name = KJONN_COL_NAME,
            codes = listOf(
                Code("1", "Mann"),
                Code("2", "Kvinne")
            ),
            mandatory = true
        ),
        FieldDefinition(
            name = EKTSTAT_COL_NAME,
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
            codes = yesNoCodeList,
            mandatory = true
        ),
        FieldDefinition(
            name = ANT_BARN_UNDER_18_COL_NAME,
            size = 2
        ),
        FieldDefinition(
            name = REG_DATO_COL_NAME,
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN,
            mandatory = true
        ),
        FieldDefinition(
            name = VEDTAK_DATO_COL_NAME,
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN,
            mandatory = true
        ),
        FieldDefinition(
            name = BEGYNT_DATO_COL_NAME,
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN,
            mandatory = true
        ),
        FieldDefinition(
            name = KVP_KOMM_COL_NAME,
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = KOMMNR_KVP_KOMM_COL_NAME,
            size = 4, // hentet fra https://www.ssb.no/klass/klassifikasjoner/131
            codes = listOf(
                Code(OSLO_MUNICIPALITY_ID, "Oslo"),
                Code("1101", "Eigersund"),
                Code("1103", "Stavanger"),
                Code("1106", "Haugesund"),
                Code("1108", "Sandnes"),
                Code("1111", "Sokndal"),
                Code("1112", "Lund"),
                Code("1114", "Bjerkreim"),
                Code("1119", "Hå"),
                Code("1120", "Klepp"),
                Code("1121", "Time"),
                Code("1122", "Gjesdal"),
                Code("1124", "Sola"),
                Code("1127", "Randaberg"),
                Code("1130", "Strand"),
                Code("1133", "Hjelmeland"),
                Code("1134", "Suldal"),
                Code("1135", "Sauda"),
                Code("1144", "Kvitsøy"),
                Code("1145", "Bokn"),
                Code("1146", "Tysvær"),
                Code("1149", "Karmøy"),
                Code("1151", "Utsira"),
                Code("1160", "Vindafjord"),
                Code("1505", "Kristiansund"),
                Code("1506", "Molde"),
                Code("1507", "Ålesund"),
                Code("1511", "Vanylven"),
                Code("1514", "Sande"),
                Code("1515", "Herøy (Møre og Romsdal)"),
                Code("1516", "Ulstein"),
                Code("1517", "Hareid"),
                Code("1520", "Ørsta"),
                Code("1525", "Stranda"),
                Code("1528", "Sykkylven"),
                Code("1531", "Sula"),
                Code("1532", "Giske"),
                Code("1535", "Vestnes"),
                Code("1539", "Rauma"),
                Code("1547", "Aukra"),
                Code("1554", "Averøy"),
                Code("1557", "Gjemnes"),
                Code("1560", "Tingvoll"),
                Code("1563", "Sunndal"),
                Code("1566", "Surnadal"),
                Code("1573", "Smøla"),
                Code("1576", "Aure"),
                Code("1577", "Volda"),
                Code("1578", "Fjord"),
                Code("1579", "Hustadvika"),
                Code("1804", "Bodø"),
                Code("1806", "Narvik"),
                Code("1811", "Bindal"),
                Code("1812", "Sømna"),
                Code("1813", "Brønnøy"),
                Code("1815", "Vega"),
                Code("1816", "Vevelstad"),
                Code("1818", "Herøy (Nordland)"),
                Code("1820", "Alstahaug"),
                Code("1822", "Leirfjord"),
                Code("1824", "Vefsn"),
                Code("1825", "Grane"),
                Code("1826", "Aarborte - Hattfjelldal"),
                Code("1827", "Dønna"),
                Code("1828", "Nesna"),
                Code("1832", "Hemnes"),
                Code("1833", "Rana"),
                Code("1834", "Lurøy"),
                Code("1835", "Træna"),
                Code("1836", "Rødøy"),
                Code("1837", "Meløy"),
                Code("1838", "Gildeskål"),
                Code("1839", "Beiarn"),
                Code("1840", "Saltdal"),
                Code("1841", "Fauske - Fuossko"),
                Code("1845", "Sørfold"),
                Code("1848", "Steigen"),
                Code("1851", "Lødingen"),
                Code("1853", "Evenes"),
                Code("1856", "Røst"),
                Code("1857", "Værøy"),
                Code("1859", "Flakstad"),
                Code("1860", "Vestvågøy"),
                Code("1865", "Vågan"),
                Code("1866", "Hadsel"),
                Code("1867", "Bø"),
                Code("1868", "Øksnes"),
                Code("1870", "Sortland"),
                Code("1871", "Andøy"),
                Code("1874", "Moskenes"),
                Code("1875", "Hamarøy"),
                Code("3001", "Halden"),
                Code("3002", "Moss"),
                Code("3003", "Sarpsborg"),
                Code("3004", "Fredrikstad"),
                Code("3005", "Drammen"),
                Code("3006", "Kongsberg"),
                Code("3007", "Ringerike"),
                Code("3011", "Hvaler"),
                Code("3012", "Aremark"),
                Code("3013", "Marker"),
                Code("3014", "Indre Østfold"),
                Code("3015", "Skiptvet"),
                Code("3016", "Rakkestad"),
                Code("3017", "Råde"),
                Code("3018", "Våler (Viken)"),
                Code("3019", "Vestby"),
                Code("3020", "Nordre Follo"),
                Code("3021", "Ås"),
                Code("3022", "Frogn"),
                Code("3023", "Nesodden"),
                Code("3024", "Bærum"),
                Code("3025", "Asker"),
                Code("3026", "Aurskog-Høland"),
                Code("3027", "Rælingen"),
                Code("3028", "Enebakk"),
                Code("3029", "Lørenskog"),
                Code("3030", "Lillestrøm"),
                Code("3031", "Nittedal"),
                Code("3032", "Gjerdrum"),
                Code("3033", "Ullensaker"),
                Code("3034", "Nes"),
                Code("3035", "Eidsvoll"),
                Code("3036", "Nannestad"),
                Code("3037", "Hurdal"),
                Code("3038", "Hole"),
                Code("3039", "Flå"),
                Code("3040", "Nesbyen"),
                Code("3041", "Gol"),
                Code("3042", "Hemsedal"),
                Code("3043", "Ål"),
                Code("3044", "Hol"),
                Code("3045", "Sigdal"),
                Code("3046", "Krødsherad"),
                Code("3047", "Modum"),
                Code("3048", "Øvre Eiker"),
                Code("3049", "Lier"),
                Code("3050", "Flesberg"),
                Code("3051", "Rollag"),
                Code("3052", "Nore og Uvdal"),
                Code("3053", "Jevnaker"),
                Code("3054", "Lunner"),
                Code("3401", "Kongsvinger"),
                Code("3403", "Hamar"),
                Code("3405", "Lillehammer"),
                Code("3407", "Gjøvik"),
                Code("3411", "Ringsaker"),
                Code("3412", "Løten"),
                Code("3413", "Stange"),
                Code("3414", "Nord-Odal"),
                Code("3415", "Sør-Odal"),
                Code("3416", "Eidskog"),
                Code("3417", "Grue"),
                Code("3418", "Åsnes"),
                Code("3419", "Våler (Innlandet)"),
                Code("3420", "Elverum"),
                Code("3421", "Trysil"),
                Code("3422", "Åmot"),
                Code("3423", "Stor-Elvdal"),
                Code("3424", "Rendalen"),
                Code("3425", "Engerdal"),
                Code("3426", "Tolga"),
                Code("3427", "Tynset"),
                Code("3428", "Alvdal"),
                Code("3429", "Folldal"),
                Code("3430", "Os"),
                Code("3431", "Dovre"),
                Code("3432", "Lesja"),
                Code("3433", "Skjåk"),
                Code("3434", "Lom"),
                Code("3435", "Vågå"),
                Code("3436", "Nord-Fron"),
                Code("3437", "Sel"),
                Code("3438", "Sør-Fron"),
                Code("3439", "Ringebu"),
                Code("3440", "Øyer"),
                Code("3441", "Gausdal"),
                Code("3442", "Østre Toten"),
                Code("3443", "Vestre Toten"),
                Code("3446", "Gran"),
                Code("3447", "Søndre Land"),
                Code("3448", "Nordre Land"),
                Code("3449", "Sør-Aurdal"),
                Code("3450", "Etnedal"),
                Code("3451", "Nord-Aurdal"),
                Code("3452", "Vestre Slidre"),
                Code("3453", "Øystre Slidre"),
                Code("3454", "Vang"),
                Code("3801", "Horten"),
                Code("3802", "Holmestrand"),
                Code("3803", "Tønsberg"),
                Code("3804", "Sandefjord"),
                Code("3805", "Larvik"),
                Code("3806", "Porsgrunn"),
                Code("3807", "Skien"),
                Code("3808", "Notodden"),
                Code("3811", "Færder"),
                Code("3812", "Siljan"),
                Code("3813", "Bamble"),
                Code("3814", "Kragerø"),
                Code("3815", "Drangedal"),
                Code("3816", "Nome"),
                Code("3817", "Midt-Telemark"),
                Code("3818", "Tinn"),
                Code("3819", "Hjartdal"),
                Code("3820", "Seljord"),
                Code("3821", "Kviteseid"),
                Code("3822", "Nissedal"),
                Code("3823", "Fyresdal"),
                Code("3824", "Tokke"),
                Code("3825", "Vinje"),
                Code("4201", "Risør"),
                Code("4202", "Grimstad"),
                Code("4203", "Arendal"),
                Code("4204", "Kristiansand"),
                Code("4205", "Lindesnes"),
                Code("4206", "Farsund"),
                Code("4207", "Flekkefjord"),
                Code("4211", "Gjerstad"),
                Code("4212", "Vegårshei"),
                Code("4213", "Tvedestrand"),
                Code("4214", "Froland"),
                Code("4215", "Lillesand"),
                Code("4216", "Birkenes"),
                Code("4217", "Åmli"),
                Code("4218", "Iveland"),
                Code("4219", "Evje og Hornnes"),
                Code("4220", "Bygland"),
                Code("4221", "Valle"),
                Code("4222", "Bykle"),
                Code("4223", "Vennesla"),
                Code("4224", "Åseral"),
                Code("4225", "Lyngdal"),
                Code("4226", "Hægebostad"),
                Code("4227", "Kvinesdal"),
                Code("4228", "Sirdal"),
                Code("4601", "Bergen"),
                Code("4602", "Kinn"),
                Code("4611", "Etne"),
                Code("4612", "Sveio"),
                Code("4613", "Bømlo"),
                Code("4614", "Stord"),
                Code("4615", "Fitjar"),
                Code("4616", "Tysnes"),
                Code("4617", "Kvinnherad"),
                Code("4618", "Ullensvang"),
                Code("4619", "Eidfjord"),
                Code("4620", "Ulvik"),
                Code("4621", "Voss"),
                Code("4622", "Kvam"),
                Code("4623", "Samnanger"),
                Code("4624", "Bjørnafjorden"),
                Code("4625", "Austevoll"),
                Code("4626", "Øygarden"),
                Code("4627", "Askøy"),
                Code("4628", "Vaksdal"),
                Code("4629", "Modalen"),
                Code("4630", "Osterøy"),
                Code("4631", "Alver"),
                Code("4632", "Austrheim"),
                Code("4633", "Fedje"),
                Code("4634", "Masfjorden"),
                Code("4635", "Gulen"),
                Code("4636", "Solund"),
                Code("4637", "Hyllestad"),
                Code("4638", "Høyanger"),
                Code("4639", "Vik"),
                Code("4640", "Sogndal"),
                Code("4641", "Aurland"),
                Code("4642", "Lærdal"),
                Code("4643", "Årdal"),
                Code("4644", "Luster"),
                Code("4645", "Askvoll"),
                Code("4646", "Fjaler"),
                Code("4647", "Sunnfjord"),
                Code("4648", "Bremanger"),
                Code("4649", "Stad"),
                Code("4650", "Gloppen"),
                Code("4651", "Stryn"),
                Code("5001", "Trondheim"),
                Code("5006", "Steinkjer"),
                Code("5007", "Namsos"),
                Code("5014", "Frøya"),
                Code("5020", "Osen"),
                Code("5021", "Oppdal"),
                Code("5022", "Rennebu"),
                Code("5025", "Røros"),
                Code("5026", "Holtålen"),
                Code("5027", "Midtre Gauldal"),
                Code("5028", "Melhus"),
                Code("5029", "Skaun"),
                Code("5031", "Malvik"),
                Code("5032", "Selbu"),
                Code("5033", "Tydal"),
                Code("5034", "Meråker"),
                Code("5035", "Stjørdal"),
                Code("5036", "Frosta"),
                Code("5037", "Levanger"),
                Code("5038", "Verdal"),
                Code("5041", "Snåase - Snåsa"),
                Code("5042", "Lierne"),
                Code("5043", "Raarvihke - Røyrvik"),
                Code("5044", "Namsskogan"),
                Code("5045", "Grong"),
                Code("5046", "Høylandet"),
                Code("5047", "Overhalla"),
                Code("5049", "Flatanger"),
                Code("5052", "Leka"),
                Code("5053", "Inderøy"),
                Code("5054", "Indre Fosen"),
                Code("5055", "Heim"),
                Code("5056", "Hitra"),
                Code("5057", "Ørland"),
                Code("5058", "Åfjord"),
                Code("5059", "Orkland"),
                Code("5060", "Nærøysund"),
                Code("5061", "Rindal"),
                Code("5401", "Tromsø"),
                Code("5402", "Harstad"),
                Code("5403", "Alta"),
                Code("5404", "Vardø"),
                Code("5405", "Vadsø"),
                Code("5406", "Hammerfest"),
                Code("5411", "Kvæfjord"),
                Code("5412", "Tjeldsund"),
                Code("5413", "Ibestad"),
                Code("5414", "Gratangen"),
                Code("5415", "Loabák - Lavangen"),
                Code("5416", "Bardu"),
                Code("5417", "Salangen"),
                Code("5418", "Målselv"),
                Code("5419", "Sørreisa"),
                Code("5420", "Dyrøy"),
                Code("5421", "Senja"),
                Code("5422", "Balsfjord"),
                Code("5423", "Karlsøy"),
                Code("5424", "Lyngen"),
                Code("5425", "Storfjord - Omasvuotna - Omasvuono"),
                Code("5426", "Gáivuotna - Kåfjord - Kaivuono"),
                Code("5427", "Skjervøy"),
                Code("5428", "Nordreisa"),
                Code("5429", "Kvænangen"),
                Code("5430", "Guovdageaidnu - Kautokeino"),
                Code("5432", "Loppa"),
                Code("5433", "Hasvik"),
                Code("5434", "Måsøy"),
                Code("5435", "Nordkapp"),
                Code("5436", "Porsanger - Porsáŋgu - Porsanki"),
                Code("5437", "Kárášjohka - Karasjok"),
                Code("5438", "Lebesby"),
                Code("5439", "Gamvik"),
                Code("5440", "Berlevåg"),
                Code("5441", "Deatnu - Tana"),
                Code("5442", "Unjárga - Nesseby"),
                Code("5443", "Båtsfjord"),
                Code("5444", "Sør-Varanger")
            )
        ),
        FieldDefinition(
            name = YTELSE_SOSHJELP_COL_NAME,
            code = Code("1", "Sosialhjelp")
        ),
        FieldDefinition(
            name = YTELSE_TYPE_SOSHJ_COL_NAME,
            codes = listOf(
                Code("2", "Sosialhjelp som viktigste kilde til livsopphold"),
                Code("3", "Supplerende sosialhjelp")
            )
        ),
        FieldDefinition(
            name = YTELSE_INTRO_COL_NAME,
            code = Code("4", "Introduksjonsstønad")
        ),
        FieldDefinition(
            name = YTELSE_INDIVIDSTONAD_COL_NAME,
            code = Code("5", "Individstønad - stønad til livsopphold etter forskrift om arbeidsmarkedstiltak")
        ),
        FieldDefinition(
            name = YTELSE_FOLKETRYGDL_COL_NAME,
            code = Code("6", "Livsoppholdsytelse etter folketrygdloven - jf veiledningen")
        ),
        FieldDefinition(
            name = KVP_MED_ASTONAD_COL_NAME,
            codes = yesNoCodeList
        ),
        FieldDefinition(
            name = KVP_MED_KOMMBOS_COL_NAME,
            code = Code("4", "Kommunal bostøtte")
        ),
        FieldDefinition(
            name = KVP_MED_HUSBANK_COL_NAME,
            code = Code("5", "Husbankens bostøtte")
        ),
        FieldDefinition(
            name = KVP_MED_SOSHJ_ENGANG_COL_NAME,
            code = Code("9", "Mottok økonomisk sosialhjelp som engangsstønad")
        ),
        FieldDefinition(
            name = KVP_MED_SOSHJ_PGM_COL_NAME,
            code = Code(
                "8",
                "Mottok økonomisk sosialhjelp til dekking av særskilte utgifter knyttet til deltakelsen i programmet"
            )
        ),
        FieldDefinition(
            name = KVP_MED_SOSHJ_SUP_COL_NAME,
            code = Code(
                "7",
                "Mottok økonomisk sosialhjelp som fast supplement til dekking av løpende livsholdsutgifter"
            )
        ),

        *calendarMonths,

        FieldDefinition(
            name = KVP_STONAD_COL_NAME,
            size = 7
        ),
        FieldDefinition(
            name = STATUS_COL_NAME,
            codes = listOf(
                Code("1", "Deltakeren er fortsatt i program (skjema er ferdig utfylt)"),
                Code("2", "Deltakeren er i permisjon fra program (skjemaet er ferdig utfylt)"),
                Code(
                    FULLFORT_PROGRAM,
                    "Deltakeren har fullført program eller avsluttet program etter avtale (gjelder ikke flytting)"
                ),
                Code("4", "Deltakerens program er varig avbrutt på grunn av uteblivelse (gjelder ikke flytting)"),
                Code("5", "Deltakerens program ble avbrutt på grunn av flytting til annen kommune"),
                Code("6", "Kun for Oslos bydeler: Deltakeren flyttet til annen bydel før programperioden var over"),
                Code("7", "Deltakerens program er avsluttet etter avbrudd")
            ),
            mandatory = true
        ),
        FieldDefinition(
            name = AVSL_DATO_COL_NAME,
            dataType = DATE_TYPE,
            size = 6,
            datePattern = DATE6_PATTERN
        ),
        FieldDefinition(
            name = AVSL_ORDINAERTARB_COL_NAME,
            size = 2,
            code = Code("01", "Ordinært arbeid (heltid/deltid)")
        ),
        FieldDefinition(
            name = AVSL_ARBLONNSTILS_COL_NAME,
            size = 2,
            code = Code(
                "11",
                "Ordinært arbeid (heltid/deltid) med midlertidig lønnstilskudd (jamfør tiltaksforskriften)"
            )
        ),
        FieldDefinition(
            name = AVSL_ARBMARK_COL_NAME,
            size = 2,
            code = Code("03", " Andre arbeidsmarkedstiltak i statlig regi (jamfør tiltaksforskriften)")
        ),
        FieldDefinition(
            name = AVSL_SKOLE_COL_NAME,
            size = 2,
            code = Code("04", "Skole/utdanning")
        ),
        FieldDefinition(
            name = AVSL_UFORE_COL_NAME,
            size = 2,
            code = Code("13", "Uføretrygd")
        ),
        FieldDefinition(
            name = AVSL_AAP_COL_NAME,
            size = 2,
            code = Code("14", "Arbeidsavklaringspenger")
        ),
        FieldDefinition(
            name = AVSL_OK_AVKLAR_COL_NAME,
            size = 2,
            code = Code("15", "Økonomisk sosialhjelp i påvente av avklaring av uføretrygd/AAP")
        ),
        FieldDefinition(
            name = AVSL_UTEN_OK_AVKLAR_COL_NAME,
            size = 2,
            code = Code("16", "Økonomisk sosialhjelp uten slik avklaring")
        ),
        FieldDefinition(
            name = AVSL_ANNET_COL_NAME,
            size = 2,
            code = Code("10", "Annet")
        ),
        FieldDefinition(
            name = AVSL_UKJENT_COL_NAME,
            size = 2,
            code = Code("17", "Ukjent")
        ),
        FieldDefinition(
            name = AVSL_VIKTIGSTE_INNTEKT_COL_NAME,
            size = 2,
            codes = listOf(
                Code("01", "Ordinært arbeid (heltid/deltid)"),
                Code(
                    "11",
                    "Ordinært arbeid (heltid/deltid) med midlertidig lønnstilskudd (jamfør tiltaksforskriften)"
                ),
                Code(
                    "03",
                    "Andre arbeidsmarkedstiltak i statlig regi (jamfør tiltaksfororskriften) (tiltakspenger)"
                ),
                Code("04", "Skole/utdanning (studiestønad)"),
                Code("13", "Uføretrygd"),
                Code("14", "Arbeidsavklaringspenger"),
                Code("15", "Økonomisk sosialhjelp i påvente av avklaring av uføretrygd/AAP"),
                Code("16", "Økonomisk sosialhjelp uten slik avklaring"),
                Code("10", "Annet"),
                Code("17", "Ukjent")
            )
        ),
        FieldDefinition(
            name = SAKSBEHANDLER_COL_NAME,
            dataType = STRING_TYPE,
            size = 10
        )
    ).buildFieldDefinitions()
}