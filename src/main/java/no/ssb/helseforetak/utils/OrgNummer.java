package no.ssb.helseforetak.utils;

/**
// Denne lista blir brukt fra og med innrapporteringsåret 2011.
// Listen som er kommentert bort under ble brukt til og med innrapporteringsåret 2010.
// Struktur på orgBedrNr: Organisasjonsnummeret er det første tallet i arrayet, mens bedriftsnummeret er den siste.
// Bedriftsnummerlisten kan være en eller flere
 *
 *
 */
public final class OrgNummer
{
/**
* Vi trenger en liste over bare HF i forbindelse med kontroller i blant annet skjema 0X og 0Y
*/
	  private static final String[][][] orgBedrNr =
	  {
		{{"983658725", "HELSE VEST RHF"}, 												{"983658725"}},
		{{"983974724", "Helse Bergen HF"},												{"983974724"}},
		{{"983974732", "Helse Førde HF"},												{"983974732"}},
		{{"983974678", "Helse Stavanger HF"},											{"983974678"}},
		{{"983974694", "Helse Fonna HF"},												{"983974694"}},
		{{"983658776", "HELSE MIDT-NORGE RHF"},											{"985831580", "996246507", "918098275" }},
		{{"883974832", "St Olavs Hospital HF"},											{"883974832"}},
		{{"997005562", "HELSE MØRE OG ROMSDAL HF"}, 									{"997005562"}},
		{{"983974791", "Helse Nord Trøndelag HF"},										{"983974791"}},
		{{"883658752", "HELSE NORD RHF"},												{"883658752"}},
		{{"983974910", "Nordlandssykehuset HF"},										{"983974910"}},
		{{"983974929", "Helgelandssykehuset HF"},										{"983974929"}},
		{{"983974880", "Helse Finnmark HF"},											{"983974880"}},
		{{"983974899", "Universitetssykehuset Nord-Norge HF"},							{"974726807"}},
		{{"991324968", "HELSE SØR-ØST RHF"},											{"991324968"}},
		{{"983975267", "Sykehuset Telemark HF"},										{"983975267"}},
		{{"883971752", "Sunnaas sykehus HF"},											{"883971752"}},
		{{"983971636", "Akershus universitetssykehus HF"},								{"983971636"}},
		{{"983975240", "Sørlandet sykehus HF"},											{"983975240"}},
		{{"983975259", "Sykehuset i Vestfold HF"},										{"983975259"}},
		{{"983971709", "Sykehuset Innlandet HF"},										{"983971709"}},
		{{"983971768", "Sykehuset Østfold HF"},											{"983971768"}},
		{{"894166762", "Vestre Viken HF"},												{"984166762"}},
		{{"993467049", "Oslo universitetssykehus HF"},									{"993467049"}},
		{{"911912759", "Helsetjenestens deriftsorganisasjon for nødnett HF"},			{"911912759"}},
		{{"913454405", "Nasjonal IKT HF"},	     										{"913454405"}},
		{{"914637651", "SYKEHUSPARTNER HF"},	     									{"914637651"}},
		{{"814630722", "SYKEHUSBYGG HF"},	     									    {"814630722"}},
		{{"987601787", "HELSE VEST IKT AS"},	     									{"987601787"}},
		{{"915536255", "HELSE VEST INNKJØP HF"},	     								{"915536255"}},
		{{"916879067", "Sykehusinnkjøp HF"},	     								    {"916879067"}},
		{{"818711832", "Luftambulansetjenesten HF "},                                   {"818711832"}},
		{{"918177833", "Helse Nord IKT HF "},                                           {"918177833"}},
		{{"918695079", "Pasientreiser HF "},                                            {"918695079"}}
//		    {{"983658725", "HELSE VEST RHF"}, 												{"983658725"}},
//			{{"983974724", "Helse Bergen HF"},												{"983974724"}},
//			{{"983974732", "Helse Førde HF"},												{"983974732"}},
//			{{"983974678", "Helse Stavanger HF"},											{"983974678"}},
//			{{"983974694", "Helse Fonna HF"},												{"983974694"}},
//			{{"983658776", "HELSE MIDT-NORGE RHF"},											{"985831580", "996246507", "979335210" }},
//			{{"883974832", "St Olavs Hospital HF"},											{"883974832"}},
//			{{"997005562", "HELSE MØRE OG ROMSDAL HF"}, 									{"997005562"}}, 			//NY FOR 2011-INNRAPPORTERING
//			{{"983974791", "Helse Nord Trøndelag HF"},										{"983974791"}},
////			{{"986523065", "Rusbehandling Midt-Norge HF"},									{"986523065"}},
////			{{"998038615", "Ambulanse Midt-Norge HF"},		    							{"998038615"}},				//NY FOR 2013-INNRAPPORTERING
//			{{"883658752", "HELSE NORD RHF"},												{"883658752","989000012"}},
//			{{"983974910", "Nordlandssykehuset HF"},										{"983974910"}},
//			{{"983974929", "Helgelandssykehuset HF"},										{"983974929"}},
//			{{"983974880", "Helse Finnmark HF"},											{"983974880"}},
//			{{"983974899", "Universitetssykehuset Nord-Norge HF"},							{"974726807"}},
//			{{"991324968", "HELSE SØR-ØST RHF"},											{"991324968","986112928"}},
//			{{"983975267", "Sykehuset Telemark HF"},										{"983975267"}},
////			{{"983975305", "Psykiatrien i  Vestfold HF"},									{"983975305"}},  			//Tatt bort for 2013-innrapportering
//			{{"883971752", "Sunnaas sykehus HF"},											{"883971752"}},
//			{{"983971636", "Akershus universitetssykehus HF"},								{"983971636"}},
//			{{"983975240", "Sørlandet sykehus HF"},											{"983975240"}},
//			{{"983975259", "Sykehuset i Vestfold HF"},										{"983975259"}},
//			{{"983971709", "Sykehuset Innlandet HF"},										{"983971709"}},
//			{{"983971768", "Sykehuset Østfold HF"},											{"983971768"}},
//			{{"894166762", "Vestre Viken HF"},												{"984166762"}},
//			{{"993467049", "Oslo universitetssykehus HF"},									{"993467049"}},
////			{{"998308615", "AMBULANSE MIDT-NORGE HF"},	     								{"998308615"}},  			//NY FOR 2012-INNRAPPORTERING
//			{{"911912759", "Helsetjenestens deriftsorganisasjon for nødnett HF"},			{"911912759"}},
//			{{"913454405", "Nasjonal IKT HF"},	     										{"913454405"}},				//NY FOR 2012-INNRAPPORTERING
//			{{"914637651", "SYKEHUSPARTNER HF"},	     									{"914637651"}},				//Nytt for 2015-innrapp
//			{{"814630722", "SYKEHUSBYGG HF"},	     									    {"814630722"}},				//Nytt for 2015-innrapp
//			{{"987601787", "HELSE VEST IKT AS"},	     									{"987601787"}},				//Nytt for 2015-innrapp
//			{{"915536255", "HELSE VEST INNKJØP HF"},	     								{"915536255"}},				//Nytt for 2015-innrapp
//			{{"916879067", "Sykehusinnkjøp HF"},	     								    {"916879067"}}				//Nytt for 2016-innrapp
	};
/**
* Vi trenger en liste over bare HF i forbindelse med kontroller i blant annet skjema 0S, 0T og 0W
*/
	  private static final String[][][] orgNrHF = 
	  {
			{{"983974678", "Helse Stavanger HF"},					{"983974678"}},
			{{"983974694", "Helse Fonna HF"},						{"983974694"}},
			{{"983974724", "Helse Bergen HF"},						{"983974724"}},
			{{"983974732", "Helse Førde HF"},						{"983974732"}},
			{{"883974832", "St Olavs Hospital HF"},					{"883974832"}},
			{{"983974791", "Helse Nord Trøndelag HF"},				{"983974791"}},
			{{"986523065", "Rusbehandling Midt-Norge HF"},			{"986523065"}},
			{{"983974880", "Helse Finnmark HF"},					{"983974880"}},
			{{"983974899", "Universitetssykehuset Nord-Norge HF"},	{"974726807"}},
			{{"983974910", "Nordlandssykehuset HF"},				{"983974910"}},
			{{"983974929", "Helgelandssykehuset HF"},				{"983974929"}},
			{{"883971752", "Sunnaas sykehus HF"},					{"883971752"}},
			{{"894166762", "Vestre Viken HF"},						{"984166762"}},
			{{"983971636", "Akershus universitetssykehus HF"},		{"983971636"}},
			{{"983971709", "Sykehuset Innlandet HF"},				{"983971709"}},
			{{"983971768", "Sykehuset Østfold HF"},					{"983971768"}},
			{{"983975240", "Sørlandet sykehus HF"},					{"983975240"}},
			{{"983975259", "Sykehuset i Vestfold HF"},				{"983975259"}},
			{{"983975267", "Sykehuset Telemark HF"},				{"983975267"}},
			{{"983975305", "Psykiatrien i  Vestfold HF"},			{"983975305"}},
			{{"997005562", "HELSE MØRE OG ROMSDAL HF"}, 			{"997005562"}}, //NY FOR 2011-INNRAPPORTERING
			{{"993467049", "Oslo universitetssykehus HF"},			{"993467049"}},
			{{"998308615", "AMBULANSE MIDT-NORGE HF"},	     		{"998308615"}}  //NY FOR 2012-INNRAPPORTERING
	};
/**
 * Vi trenger ei liste over bare RHF if forbindelse med kontroller i blant annet skjema 0U og 0V	  
 */
	  private static final String[][][] orgNrRHF = 
	  {
		    {{"983658725", "HELSE VEST RHF"}, 						{"983658725"}},
			{{"983974724", "Helse Bergen HF"},						{"983974724"}},
			{{"983974732", "Helse Førde HF"},						{"983974732"}},
			{{"983974678", "Helse Stavanger HF"},					{"983974678"}},
			{{"983974694", "Helse Fonna HF"},						{"983974694"}},
			{{"983658776", "HELSE MIDT-NORGE RHF"},					{"985831580", "996246507", "979335210" }},
			{{"883974832", "St Olavs Hospital HF"},					{"883974832"}},
			{{"997005562", "HELSE MØRE OG ROMSDAL HF"}, 			{"997005562"}}, //NY FOR 2011-INNRAPPORTERING
			{{"983974791", "Helse Nord Trøndelag HF"},				{"983974791"}},
			{{"986523065", "Rusbehandling Midt-Norge HF"},			{"986523065"}},
			{{"883658752", "HELSE NORD RHF"},						{"883658752","989000012"}},
			{{"983974910", "Nordlandssykehuset HF"},				{"983974910"}},
			{{"983974929", "Helgelandssykehuset HF"},				{"983974929"}},
			{{"983974880", "Helse Finnmark HF"},					{"983974880"}},
			{{"983974899", "Universitetssykehuset Nord-Norge HF"},	{"974726807"}},
			{{"991324968", "HELSE SØR-ØST RHF"},					{"991324968","986112928"}},
			{{"983975267", "Sykehuset Telemark HF"},				{"983975267"}},
			{{"983975305", "Psykiatrien i  Vestfold HF"},			{"983975305"}},
			{{"883971752", "Sunnaas sykehus HF"},					{"883971752"}},
			{{"983971636", "Akershus universitetssykehus HF"},		{"983971636"}},
			{{"983975240", "Sørlandet sykehus HF"},					{"983975240"}},
			{{"983975259", "Sykehuset i Vestfold HF"},				{"983975259"}},
			{{"983971709", "Sykehuset Innlandet HF"},				{"983971709"}},
			{{"983971768", "Sykehuset Østfold HF"},					{"983971768"}},
			{{"894166762", "Vestre Viken HF"},						{"984166762"}},
			{{"993467049", "Oslo universitetssykehus HF"},			{"993467049"}},
			{{"998308615", "AMBULANSE MIDT-NORGE HF"},	     		{"998308615"}}  //NY FOR 2012-INNRAPPORTERING
	};
	  
  
  public static boolean orgNrIsValid (String orgNr)
  {
    for (int i=orgBedrNr.length-1; i>=0; i--)
    {
      if (orgBedrNr[i][0][0].equalsIgnoreCase(orgNr))
      {
        return true;
      }
    }
    return false;
  }

  public static String getNameCorrespondingToOrgNr (String orgNr)
  {
    for (int i=orgBedrNr.length-1; i>=0; i--)
    {
      if (orgBedrNr[i][0][0].equalsIgnoreCase(orgNr))
      {
        return orgBedrNr[i][0][1];
      }
    }
    return "";
  }

  public static String[] getBedrNrCorrespondingToOrgNr (String orgNr)
  {
    for (int i=orgBedrNr.length-1; i>=0; i--)
    {
      if (orgBedrNr[i][0][0].equalsIgnoreCase(orgNr))
      {
        return orgBedrNr[i][1];
      }
    }
    return null;
  }

  public static boolean bedrNrIsValid (String bedrNr)
  {
    for (int i=orgBedrNr.length-1; i>=0; i--)
    {
      for (int j=orgBedrNr[i][1].length-1; j>=0; j--)
      {
        if (orgBedrNr[i][1][j].equalsIgnoreCase(bedrNr))
        {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean orgNrHFIsValid (String orgNr)
  {
    for (int i=orgNrHF.length-1; i>=0; i--)
    {
      if (orgNrHF[i][0][0].equalsIgnoreCase(orgNr))
      {
        return true;
      }
    }
    return false;
  }

  public static boolean orgNrRHFIsValid (String orgNr)
  {
    for (int i=orgNrRHF.length-1; i>=0; i--)
    {
      if (orgNrRHF[i][0][0].equalsIgnoreCase(orgNr))
      {
        return true;
      }
    }
    return false;
  }

}