package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlKontokoder.java,v $
 * Revision 1.10  2010/10/22 08:04:16  pll
 * Klar for 2010-rapp.
 *
 * Revision 1.9  2009/10/15 10:13:43  pll
 * Kontokode 452 tatt ut.
 *
 * Revision 1.8  2009/10/03 12:22:03  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.7  2009/04/06 13:02:50  pll
 * Bugfix.
 *
 * Revision 1.6  2008/10/22 16:34:21  pll
 * Versjon: 2008-rapporteringen.
 *
 * Revision 1.5  2008/04/30 09:07:03  pll
 * String-objekter erstattet med StringBuilder-objekter.
 *
 * Revision 1.4  2007/11/19 07:54:47  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/11/05 11:16:22  pll
 * Endringer før 2007-rapporteringen.
 *
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.3  2006/11/29 07:52:57  pll
 * Inn: 328 Ut: 391
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.4  2006/04/26 11:30:32  lwe
 * det er ikke 868 men 686 (var riktig i listen, men feil i cvs-log-kommentar)
 *
 * Revision 1.3  2005/12/22 12:57:50  lwe
 * etter kravspec: endret/sjekket gyldige kontokoder (ut: 673, inn: 453,454,456,457, 868, 870, 871, 895)
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKontokoder extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 8, Kontokoder, gyldige:";
  
  private static String[] validKontokoder = 
  {
    "300", "301", "308", "310", "311", "318", "320", "321", "322", "323", "324", "325", "326", "327", "328", "329", "330", "331", "332", "334",
    "335", "336", "337", "338", "339", "340", "350", "360", "361", "362", "380", "390", "399", "400", "401", "402", "403", "404", "405", "406",
    "407", "409", "410", "411", "412", "413", "418", "419", "420", "429", "430", "439", "450", "451", "452", "456", "457", "459", "460", "461",
    "462", "463", "464", "468", "469", "490", "499", "500", "501", "502", "503", "504", "505", "506", "509", "510", "511", "512", "513", "514",
    "515", "516", "519", "520", "521", "522", "523", "524", "528", "529", "530", "539", "540", "541", "542", "550", "551", "560", "561", "562",
    "570", "571", "579", "580", "581", "583", "589", "590", "591", "592", "593", "594", "595", "596", "599", "600", "601", "602", "603", "604",
    "605", "606", "609", "610", "619", "630", "632", "634", "635", "636", "639", "640", "641", "642", "643", "644", "645", "649", "650", "651",
    "652", "653", "654", "655", "656", "657", "659", "660", "661", "662", "663", "664", "669", "670", "671", "672", "675", "679", "680", "682",
    "684", "686", "689", "690", "691", "700", "702", "704", "709", "710", "713", "714", "715", "716", "719", "730", "735", "740", "741", "750",
    "751", "760", "769", "770", "771", "772", "779", "780", "781", "782", "783", "790", "800", "801", "802", "803", "804", "810", "811", "812",
    "813", "814", "815", "816", "870", "871", "893", "895", "896"
  };

  private Vector<String[]> invalidKontokoder = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String kontokode = RecordFields.getKontokode(line);
    
    if (! validKontokode (kontokode))
    {
      lineHasError = true;
      String[] container = new String[2];
      container[0] = Integer.toString (lineNumber);
      container[1] = kontokode;
      invalidKontokoder.add (container);
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    StringBuilder errorReport = new StringBuilder (ERROR_TEXT + lf + lf);
    int numOfRecords = invalidKontokoder.size();
    if (numOfRecords > 0)
    {
      errorReport.append("\tFeil: Ukjent" + (numOfRecords ==1 ? "" : "e") + 
          " kontokode" + (numOfRecords ==1 ? "" : "r") + 
          "(Kun gyldige kontokoder godtas):" + lf);
      if (numOfRecords <= 10)
      {
          for (int i=0; i<numOfRecords; i++)
          {
            String[] container = (String[]) invalidKontokoder.elementAt(i);
            errorReport.append("\t\tkontokode " + container[1] + 
                " (Record nr. " + container[0] + ")" + lf);
          }
      }
    }
    errorReport.append(lf);
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return invalidKontokoder.size() > 0;
  }

  static boolean validKontokode (String kontokode)
  {
    for (int i=validKontokoder.length-1; i>=0; i--)
    {
      if (kontokode.equalsIgnoreCase(validKontokoder[i]))
      {
        return true;
      }
    }
    return false;
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}