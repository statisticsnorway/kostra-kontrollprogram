package no.ssb.helseforetak.control.regnskap.regn0Y;

/**
 * $Log: ControlKontokoderBalanse.java,v $
 * Revision 1.7  2010/10/21 07:56:24  pll
 * Klar for 2010-rapp.
 *
 * Revision 1.6  2009/10/03 12:44:08  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.5  2009/04/06 11:49:40  pll
 * String -> StringBuilder
 *
 * Revision 1.4  2007/11/19 08:05:28  pll
 * Endret implementasjon av getErrorType().
 *
 * Revision 1.3  2007/11/12 13:41:02  pll
 * Endret for 2007-rapporteringen.
 *
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:23  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.3  2006/12/06 11:46:44  pll
 * Konti:
 * lagt til: 107, 138,165, 223
 * tatt vekk: 236, 272, 273
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:08  pll
 * Import av 2005-filer
 *
 * Revision 1.3  2005/12/22 13:18:51  lwe
 * etter kravspec: endret/sjekket gyldige kontokoder (ut: ingen, inn: 172, 177, 201,202, 280,281)
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
	"100","102","103","104","105","106","107","108","110","112","113","114","115","116","119","120","121","123","124","125",
	"126","127","128","129","131","132","135","136","138","139","140","141","142","143","150","153","155","156","157","158",
	"165","167","170","171","172","175","176","177","181","182","183","184","185","186","187","188","190","192","194","195",
	"200","202","205","209","210","216","218","220","221","222","223","224","226","227","228","229","230","232","234","238",
	"240","246","260","261","262","263","264","265","269","270","271","274","275","276","277","278","279","280","281","290",
	"291","292","293","294","295","296","297","298","299"
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
      else
       errorReport.append("\t\tGjelder flere enn 10 records.");   
    }
    errorReport.append(lf + lf);
    return errorReport.toString();
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
    return Constants.CRITICAL_ERROR;
  }
}