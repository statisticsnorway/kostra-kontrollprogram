package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlKontokoderBalanse.java,v $
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:22  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlKontokoderBalanse extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 10, Kontokoder, gyldige i balansen:";
  
  private String[] validKontokoder = 
  {
    "100", "102", "103", "104", "105", "106", "110", "112", "113", "114", "115",
    "116", "119", "120", "121", "123", "124", "125", "126", "127", "128", "129",
    "131", "132", "135", "136", "139", "140", "141", "142", "143", "150", "153",
    "155", "156", "157", "158", "167", "170", "171", "175", "176", "181", "182",
    "183", "184", "185", "186", "187", "188", "190", "192", "195", "200", "205",
    "208", "210", "216", "218", "220", "221", "222", "224", "226", "227", "228",
    "229", "230", "232", "234", "236", "238", "240", "246", "260", "261", "262",
    "263", "264", "265", "269", "270", "271", "272", "273", "274", "277", "278",
    "279", "290", "291", "292", "293", "294", "295", "296", "297", "298", "299"
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
    String errorReport = ERROR_TEXT + lf + lf;
    int numOfRecords = invalidKontokoder.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: Ukjent" + (numOfRecords ==1 ? "" : "e") + 
          " kontokode" + (numOfRecords ==1 ? "" : "r") + 
          "(Kun gyldige kontokoder godtas):" + lf;
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidKontokoder.elementAt(i);
        errorReport += "\t\tkontokode " + container[1] + 
            " (Record nr. " + container[0] + ")" + lf;
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidKontokoder.size() > 0;
  }

  private boolean validKontokode (String kontokode)
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
    return Constants.NORMAL_ERROR;
  }
}