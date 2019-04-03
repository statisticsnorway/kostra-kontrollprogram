package no.ssb.kostra.utils;

public final class RegionerKvartal {
	  private static final String[][] regions = {
			  {"010100","Halden"},
			  {"010400","Moss"},
			  {"010500","Sarpsborg"},
			  {"010600","Fredrikstad"},
			  {"011100","Hvaler"},
			  {"011800","Aremark"},
			  {"011900","Marker"},
			  {"012100","Rømskog"},
			  {"012200","Trøgstad"},
			  {"012300","Spydeberg"},
			  {"012400","Askim"},
			  {"012500","Eidsberg"},
			  {"012700","Skiptvet"},
			  {"012800","Rakkestad"},
			  {"013500","Råde"},
			  {"013600","Rygge"},
			  {"013700","Våler"},
			  {"013800","Hobøl"},
			  {"021100","Vestby"},
			  {"021300","Ski"},
			  {"021400","Ås"},
			  {"021500","Frogn"},
			  {"021600","Nesodden"},
			  {"021700","Oppegård"},
			  {"021900","Bærum"},
			  {"022000","Asker"},
			  {"022100","Aurskog-Høland"},
			  {"022600","Sørum"},
			  {"022700","Fet"},
			  {"022800","Rælingen"},
			  {"022900","Enebakk"},
			  {"023000","Lørenskog"},
			  {"023100","Skedsmo"},
			  {"023300","Nittedal"},
			  {"023400","Gjerdrum"},
			  {"023500","Ullensaker"},
			  {"023600","Nes"},
			  {"023700","Eidsvoll"},
			  {"023800","Nannestad"},
			  {"023900","Hurdal"},
			  {"030100","Oslo"},
			  {"040200","Kongsvinger"},
			  {"040300","Hamar"},
			  {"041200","Ringsaker"},
			  {"041500","Løten"},
			  {"041700","Stange"},
			  {"041800","Nord-Odal"},
			  {"041900","Sør-Odal"},
			  {"042000","Eidskog"},
			  {"042300","Grue"},
			  {"042500","Åsnes"},
			  {"042600","Våler"},
			  {"042700","Elverum"},
			  {"042800","Trysil"},
			  {"042900","Åmot"},
			  {"043000","Stor-Elvdal"},
			  {"043200","Rendalen"},
			  {"043400","Engerdal"},
			  {"043600","Tolga"},
			  {"043700","Tynset"},
			  {"043800","Alvdal"},
			  {"043900","Folldal"},
			  {"044100","Os"},
			  {"050100","Lillehammer"},
			  {"050200","Gjøvik"},
			  {"051100","Dovre"},
			  {"051200","Lesja"},
			  {"051300","Skjåk"},
			  {"051400","Lom"},
			  {"051500","Vågå"},
			  {"051600","Nord-Fron"},
			  {"051700","Sel"},
			  {"051900","Sør-Fron"},
			  {"052000","Ringebu"},
			  {"052100","Øyer"},
			  {"052200","Gausdal"},
			  {"052800","Østre Toten"},
			  {"052900","Vestre Toten"},
			  {"053200","Jevnaker"},
			  {"053300","Lunner"},
			  {"053400","Gran"},
			  {"053600","Søndre Land"},
			  {"053800","Nordre Land"},
			  {"054000","Sør-Aurdal"},
			  {"054100","Etnedal"},
			  {"054200","Nord-Aurdal"},
			  {"054300","Vestre Slidre"},
			  {"054400","Øystre Slidre"},
			  {"054500","Vang"},
			  {"060200","Drammen"},
			  {"060400","Kongsberg"},
			  {"060500","Ringerike"},
			  {"061200","Hole"},
			  {"061500","Flå"},
			  {"061600","Nes"},
			  {"061700","Gol"},
			  {"061800","Hemsedal"},
			  {"061900","Ål"},
			  {"062000","Hol"},
			  {"062100","Sigdal"},
			  {"062200","Krødsherad"},
			  {"062300","Modum"},
			  {"062400","Øvre Eiker"},
			  {"062500","Nedre Eiker"},
			  {"062600","Lier"},
			  {"062700","Røyken"},
			  {"062800","Hurum"},
			  {"063100","Flesberg"},
			  {"063200","Rollag"},
			  {"063300","Nore og Uvdal"},
			  {"070100","Horten"},
			  {"070400","Tønsberg"},
			  {"071000","Sandefjord"},
			  {"071100","Svelvik"},
			  {"071200","Larvik"},
			  {"071300","Sande"},
			  {"071500","Holmestrand"},
			  {"071600","Re"},
			  {"072900","Færder"},
			  {"080500","Porsgrunn"},
			  {"080600","Skien"},
			  {"080700","Notodden"},
			  {"081100","Siljan"},
			  {"081400","Bamble"},
			  {"081500","Kragerø"},
			  {"081700","Drangedal"},
			  {"081900","Nome"},
			  {"082100","Bø"},
			  {"082200","Sauherad"},
			  {"082600","Tinn"},
			  {"082700","Hjartdal"},
			  {"082800","Seljord"},
			  {"082900","Kviteseid"},
			  {"083000","Nissedal"},
			  {"083100","Fyresdal"},
			  {"083300","Tokke"},
			  {"083400","Vinje"},
			  {"090100","Risør"},
			  {"090400","Grimstad"},
			  {"090600","Arendal"},
			  {"091100","Gjerstad"},
			  {"091200","Vegårshei"},
			  {"091400","Tvedestrand"},
			  {"091900","Froland"},
			  {"092600","Lillesand"},
			  {"092800","Birkenes"},
			  {"092900","Åmli"},
			  {"093500","Iveland"},
			  {"093700","Evje og Hornnes"},
			  {"093800","Bygland"},
			  {"094000","Valle"},
			  {"094100","Bykle"},
			  {"100100","Kristiansand"},
			  {"100200","Mandal"},
			  {"100300","Farsund"},
			  {"100400","Flekkefjord"},
			  {"101400","Vennesla"},
			  {"101700","Songdalen"},
			  {"101800","Søgne"},
			  {"102100","Marnardal"},
			  {"102600","Åseral"},
			  {"102700","Audnedal"},
			  {"102900","Lindesnes"},
			  {"103200","Lyngdal"},
			  {"103400","Hægebostad"},
			  {"103700","Kvinesdal"},
			  {"104600","Sirdal"},
			  {"110100","Eigersund"},
			  {"110200","Sandnes"},
			  {"110300","Stavanger"},
			  {"110600","Haugesund"},
			  {"111100","Sokndal"},
			  {"111200","Lund"},
			  {"111400","Bjerkreim"},
			  {"111900","Hå"},
			  {"112000","Klepp"},
			  {"112100","Time"},
			  {"112200","Gjesdal"},
			  {"112400","Sola"},
			  {"112700","Randaberg"},
			  {"112900","Forsand"},
			  {"113000","Strand"},
			  {"113300","Hjelmeland"},
			  {"113400","Suldal"},
			  {"113500","Sauda"},
			  {"114100","Finnøy"},
			  {"114200","Rennesøy"},
			  {"114400","Kvitsøy"},
			  {"114500","Bokn"},
			  {"114600","Tysvær"},
			  {"114900","Karmøy"},
			  {"115100","Utsira"},
			  {"116000","Vindafjord"},
			  {"120100","Bergen"},
			  {"121100","Etne"},
			  {"121600","Sveio"},
			  {"121900","Bømlo"},
			  {"122100","Stord"},
			  {"122200","Fitjar"},
			  {"122300","Tysnes"},
			  {"122400","Kvinnherad"},
			  {"122700","Jondal"},
			  {"122800","Odda"},
			  {"123100","Ullensvang"},
			  {"123200","Eidfjord"},
			  {"123300","Ulvik"},
			  {"123400","Granvin"},
			  {"123500","Voss"},
			  {"123800","Kvam"},
			  {"124100","Fusa"},
			  {"124200","Samnanger"},
			  {"124300","Os"},
			  {"124400","Austevoll"},
			  {"124500","Sund"},
			  {"124600","Fjell"},
			  {"124700","Askøy"},
			  {"125100","Vaksdal"},
			  {"125200","Modalen"},
			  {"125300","Osterøy"},
			  {"125600","Meland"},
			  {"125900","Øygarden"},
			  {"126000","Radøy"},
			  {"126300","Lindås"},
			  {"126400","Austrheim"},
			  {"126500","Fedje"},
			  {"126600","Masfjorden"},
			  {"140100","Flora"},
			  {"141100","Gulen"},
			  {"141200","Solund"},
			  {"141300","Hyllestad"},
			  {"141600","Høyanger"},
			  {"141700","Vik"},
			  {"141800","Balestrand"},
			  {"141900","Leikanger"},
			  {"142000","Sogndal"},
			  {"142100","Aurland"},
			  {"142200","Lærdal"},
			  {"142400","Årdal"},
			  {"142600","Luster"},
			  {"142800","Askvoll"},
			  {"142900","Fjaler"},
			  {"143000","Gaular"},
			  {"143100","Jølster"},
			  {"143200","Førde"},
			  {"143300","Naustdal"},
			  {"143800","Bremanger"},
			  {"143900","Vågsøy"},
			  {"144100","Selje"},
			  {"144300","Eid"},
			  {"144400","Hornindal"},
			  {"144500","Gloppen"},
			  {"144900","Stryn"},
			  {"150200","Molde"},
			  {"150400","Ålesund"},
			  {"150500","Kristiansund"},
			  {"151100","Vanylven"},
			  {"151400","Sande"},
			  {"151500","Herøy"},
			  {"151600","Ulstein"},
			  {"151700","Hareid"},
			  {"151900","Volda"},
			  {"152000","Ørsta"},
			  {"152300","Ørskog"},
			  {"152400","Norddal"},
			  {"152500","Stranda"},
			  {"152600","Stordal"},
			  {"152800","Sykkylven"},
			  {"152900","Skodje"},
			  {"153100","Sula"},
			  {"153200","Giske"},
			  {"153400","Haram"},
			  {"153500","Vestnes"},
			  {"153900","Rauma"},
			  {"154300","Nesset"},
			  {"154500","Midsund"},
			  {"154600","Sandøy"},
			  {"154700","Aukra"},
			  {"154800","Fræna"},
			  {"155100","Eide"},
			  {"155400","Averøy"},
			  {"155700","Gjemnes"},
			  {"156000","Tingvoll"},
			  {"156300","Sunndal"},
			  {"156600","Surnadal"},
			  {"157100","Halsa"},
			  {"157300","Smøla"},
			  {"157600","Aure"},
			  {"180400","Bodø"},
			  {"180500","Narvik"},
			  {"181100","Bindal"},
			  {"181200","Sømna"},
			  {"181300","Brønnøy"},
			  {"181500","Vega"},
			  {"181600","Vevelstad"},
			  {"181800","Herøy"},
			  {"182000","Alstahaug"},
			  {"182200","Leirfjord"},
			  {"182400","Vefsn"},
			  {"182500","Grane"},
			  {"182600","Hattfjelldal"},
			  {"182700","Dønna"},
			  {"182800","Nesna"},
			  {"183200","Hemnes"},
			  {"183300","Rana"},
			  {"183400","Lurøy"},
			  {"183500","Træna"},
			  {"183600","Rødøy"},
			  {"183700","Meløy"},
			  {"183800","Gildeskål"},
			  {"183900","Beiarn"},
			  {"184000","Saltdal"},
			  {"184100","Fauske - Fuosko"},
			  {"184500","Sørfold"},
			  {"184800","Steigen"},
			  {"184900","Hamarøy - Hábmer"},
			  {"185000","Divtasvuodna - Tysfjord"},
			  {"185100","Lødingen"},
			  {"185200","Tjeldsund"},
			  {"185300","Evenes"},
			  {"185400","Ballangen"},
			  {"185600","Røst"},
			  {"185700","Værøy"},
			  {"185900","Flakstad"},
			  {"186000","Vestvågøy"},
			  {"186500","Vågan"},
			  {"186600","Hadsel"},
			  {"186700","Bø"},
			  {"186800","Øksnes"},
			  {"187000","Sortland - Suortá"},
			  {"187100","Andøy"},
			  {"187400","Moskenes"},
			  {"190200","Tromsø"},
			  {"190300","Harstad - Hárstták"},
			  {"191100","Kvæfjord"},
			  {"191300","Skånland"},
			  {"191700","Ibestad"},
			  {"191900","Gratangen"},
			  {"192000","Loabák - Lavangen"},
			  {"192200","Bardu"},
			  {"192300","Salangen"},
			  {"192400","Målselv"},
			  {"192500","Sørreisa"},
			  {"192600","Dyrøy"},
			  {"192700","Tranøy"},
			  {"192800","Torsken"},
			  {"192900","Berg"},
			  {"193100","Lenvik"},
			  {"193300","Balsfjord"},
			  {"193600","Karlsøy"},
			  {"193800","Lyngen"},
			  {"193900","Storfjord - Omasvuotna - Omasvuono"},
			  {"194000","Gáivuotna - Kåfjord - Kaivuono"},
			  {"194100","Skjervøy"},
			  {"194200","Nordreisa"},
			  {"194300","Kvænangen"},
			  {"200200","Vardø"},
			  {"200300","Vadsø"},
			  {"200400","Hammerfest"},
			  {"201100","Guovdageaidnu - Kautokeino"},
			  {"201200","Alta"},
			  {"201400","Loppa"},
			  {"201500","Hasvik"},
			  {"201700","Kvalsund"},
			  {"201800","Måsøy"},
			  {"201900","Nordkapp"},
			  {"202000","Porsanger - Porsángu - Porsanki"},
			  {"202100","Kárá?johka - Karasjok"},
			  {"202200","Lebesby"},
			  {"202300","Gamvik"},
			  {"202400","Berlevåg"},
			  {"202500","Deatnu - Tana"},
			  {"202700","Unjárga - Nesseby"},
			  {"202800","Båtsfjord"},
			  {"203000","Sør-Varanger"},
			  {"500100","Trondheim"},
			  {"500400","Steinkjer"},
			  {"500500","Namsos"},
			  {"501100","Hemne"},
			  {"501200","Snillfjord"},
			  {"501300","Hitra"},
			  {"501400","Frøya"},
			  {"501500","Ørland"},
			  {"501600","Agdenes"},
			  {"501700","Bjugn"},
			  {"501800","Åfjord"},
			  {"501900","Roan"},
			  {"502000","Osen"},
			  {"502100","Oppdal"},
			  {"502200","Rennebu"},
			  {"502300","Meldal"},
			  {"502400","Orkdal"},
			  {"502500","Røros"},
			  {"502600","Holtålen"},
			  {"502700","Midtre Gauldal"},
			  {"502800","Melhus"},
			  {"502900","Skaun"},
			  {"503000","Klæbu"},
			  {"503100","Malvik"},
			  {"503200","Selbu"},
			  {"503300","Tydal"},
			  {"503400","Meråker"},
			  {"503500","Stjørdal"},
			  {"503600","Frosta"},
			  {"503700","Levanger"},
			  {"503800","Verdal"},
			  {"503900","Verran"},
			  {"504000","Namdalseid"},
			  {"504100","Snåase - Snåsa"},
			  {"504200","Lierne"},
			  {"504300","Raarvihke - Røyrvik"},
			  {"504400","Namsskogan"},
			  {"504500","Grong"},
			  {"504600","Høylandet"},
			  {"504700","Overhalla"},
			  {"504800","Fosnes"},
			  {"504900","Flatanger"},
			  {"505000","Vikna"},
			  {"505100","Nærøy"},
			  {"505200","Leka"},
			  {"505300","Inderøy"},
			  {"505400","Indre Fosen"},
			  {"506100","Rindal"},
			  {"010000","Østfold"},
			  {"020000","Akershus"},
			  {"040000","Hedmark"},
			  {"050000","Oppland"},
			  {"060000","Buskerud"},
			  {"070000","Vestfold"},
			  {"080000","Telemark"},
			  {"090000","Aust-Agder"},
			  {"100000","Vest-Agder"},
			  {"110000","Rogaland"},
			  {"120000","Hordaland"},
			  {"140000","Sogn og Fjordane"},
			  {"150000","Møre og Romsdal"},
			  {"180000","Nordland"},
			  {"190000","Troms Romsa"},
			  {"200000","Finnmark Finnmárku"},
			  {"500000","Trøndelag"}};

		  public static boolean regionNrIsValid (String regionNr)
		  {
		    for (int i=regions.length-1; i>=0; i--)
		    {
		      if (regionNr.equalsIgnoreCase(regions[i][0]))
		      {
		        return true;
		      }
		    }
		    return false;
		  }

		  public static boolean kommuneNrIsValid (String kommuneNr)
		  {
		    for (int i=regions.length-1; i>=0; i--)
		    {
		      if (kommuneNr.equalsIgnoreCase(regions[i][0]) &&
		          ! kommuneNr.substring(2,4).equalsIgnoreCase("00"))
		      {
		        return true;
		      }
		    }
		    return false;
		  }

		  public static boolean fylkeskommuneNrIsValid (String fylkeskommuneNr)
		  {
		    for (int i=regions.length-1; i>=0; i--)
		    {
		      if (fylkeskommuneNr.equalsIgnoreCase(regions[i][0]) &&
		          fylkeskommuneNr.substring(2,4).equalsIgnoreCase("00"))
		      {
		        return true;
		      }
		    }
		    return false;
		  }

		  public static String getRegionName (String regionNr)
		  {
		    for (int i=regions.length-1; i>=0; i--)
		    {
		      if (regionNr.equalsIgnoreCase(regions[i][0]))
		      {
		        return regions[i][1];
		      }
		    }
		    return "";
		  }

}

