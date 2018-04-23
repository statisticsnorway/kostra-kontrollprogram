package no.ssb.helseforetak.utils;

/**
 *
 */


public final class BedriftsNummer 
{
  private static final String[][] bedriftsNummer =
  {
	  {"finst_orgnr", "bedriftnavn"},
	  {"983658725", "HELSE VEST RHF"},
	  {"983974724", "Helse Bergen HF"},
	  {"983974732", "Helse Førde HF"},
	  {"983974678", "Helse Stavanger HF"},
	  {"983974694", "Helse Fonna HF"},
	  {"983658776", "HELSE MIDT-NORGE RHF"},
	  {"883974832", "St Olavs Hospital HF"},
	  {"997005562", "Helse Møre OG Romsdal HF"},
	  {"983974791", "Helse Nord Trøndelag HF"},
	  {"883658752", "HELSE NORD RHF"},
	  {"983974910", "Nordlandssykehuset HF"},
	  {"983974929", "Helgelandssykehuset HF"},
	  {"983974880", "Helse Finnmark HF"},
	  {"983974899", "Universitetssykehuset Nord-Norge HF"},
	  {"991324968", "HELSE SØR-ØST RHF"},
	  {"983975267", "Sykehuset Telemark HF"},
	  {"883971752", "Sunnaas sykehus HF"},
	  {"983971636", "Akershus universitetssykehus HF"},
	  {"983975240", "Sørlandet sykehus HF"},
	  {"983975259", "Sykehuset i Vestfold HF"},
	  {"983971709", "Sykehuset Innlandet HF"},
	  {"983971768", "Sykehuset Østfold HF"},
	  {"894166762", "VESTRE VIKEN HF"},
	  {"993467049", "Oslo Universitetssykehus HF"},
	  {"911912759", "Helsetjenestens driftsorganisasjon for nødnett HF"},
	  {"913454405", "Nasjonal IKT HF"},
	  {"914637651", "Sykehuspartner HF"},
	  {"814630722", "Sykehusbygg HF"},
	  {"987601787", "Helse Vest IKT AS "},
	  {"915536255", "Helse Vest Innkjøp HF "},
	  {"916879067", "Sykehusinnkjøp HF "},
	  {"818711832", "Luftambulansetjenesten HF "},
	  {"918177833", "Helse Nord IKT HF "},
	  {"918695079", "Pasientreiser HF "},
//	  {"983658725", "Helse Vest RHF"},
//	  {"983974678", "Helse Stavanger HF"},
//	  {"983974694", "Helse Fonna HF"},
//	  {"983974724", "Helse Bergen HF"},
//	  {"983974732", "Helse Førde HF "},
//	  {"985831580", "HMIT"},
//	  {"996246507", "NPSS-Forvaltningsorganisasjonen "},
//	  {"983759084", "Helsebygg"},
//	  {"883974832", "St Olavs Hospital HF"},
//	  {"997005562", "Helse møre og Romsdal HF"},
//	  {"983974791", "Helse Nord Trøndelag HF"},
//	  {"986523065", "Rusbehandling Midt-Norge HF"},
//	  {"883658752", "Helse Nord RHF"},
//	  {"989000012", "Helse Nord IKT"},
//	  {"983974880", "Helse Finnmark HF"},
//	  {"974726807", "Longyearbyen sykehus"},
//	  {"983974910", "Nordlandssykehuset HF"},
//	  {"983974929", "Helgelandssykehuset HF"},
//	  {"991324968", "Helse Sør-Øst RHF"},
//	  {"986112928", "Sykehuspartner"},
//	  {"883971752", "Sunnaas sykehus HF"},
//	  {"984166762", "Vestre Viken HF"},
//	  {"983971636", "Akershus universitetssykehus HF"},
//	  {"983971709", "Sykehuset Innlandet HF"},
//	  {"983971768", "Sykehuset Østfold HF"},
//	  {"983975240", "Sørlandet sykehus HF"},
//	  {"983975259", "Sykehuset i Vestfold HF"},
//	  {"983975267", "Sykehuset Telemark HF"},
//	  {"983975305", "Psykiatrien i Vestfold HF"},
//	  {"993467049", "Oslo universitetssykehus HF"}
  };
  
  public static boolean bedrNrIsValid (String bedrNr)
  {
    for (int i=bedriftsNummer.length-1; i>=0; i--)
    {
      if (bedrNr.equalsIgnoreCase(bedriftsNummer[i][0]))
      {
        return true;
      }
    }
    return false;
  }

  public static String getNameCorrespondingToBedrNr (String bedrNr)
  {
    for (int i=bedriftsNummer.length-1; i>=0; i--)
    {
      if (bedrNr.equalsIgnoreCase(bedriftsNummer[i][0]))
      {
        return bedriftsNummer[i][1];
      }
    }
    return "";
  }
}