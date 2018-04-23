package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: Control14AlleRapporteringsenheter.java,v $
 * Revision 1.2  2007/10/25 11:33:57  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:55  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:07  pll
 * Import av 2005-filer
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.*;

//import no.ssb.helseforetak.control.*;
import no.ssb.helseforetak.utils.*;
import no.ssb.kostra.control.Constants;

final class Control14AlleRapporteringsenheter extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = 
      "Kontroll 14, Kontroll av at alle rapporteringsenhetene" + lf +"inngår i " +
      "rapporten for hvert helseforetak/regionalt helseforetak:";
//  private Vector recordNumbers = new Vector();
  private Vector<String> bedrNr = new Vector<String>();
  private Vector<String> missingBedrNr = new Vector<String>();
  private String orgNr = null;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    if (orgNr == null)
    {
      orgNr = RecordFields.getOrgNummer(line);
    }

    String tmp = RecordFields.getForetaksNummer(line);

    if (! bedrNr.contains(tmp))
    {
      bedrNr.add(tmp);
    }
    
    return false;
  }

  public boolean doControl()
  {
    String[] allBedrNr = OrgNummer.getBedrNrCorrespondingToOrgNr(orgNr);
    if (allBedrNr != null)
    {
      for (int i=allBedrNr.length-1; i>=0; i--)
      {
        if (! bedrNr.contains(allBedrNr[i]))
        {
          if (! missingBedrNr.contains(allBedrNr[i]))
          {
            missingBedrNr.add(allBedrNr[i]);
          }
        }
      }
    }
    return missingBedrNr.size()>0;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    if (foundError())
    {
      errorReport += "\tFeil: Følgende bedriftsnummer mangler for " + 
          "organisasjonsnummer " + orgNr + ": " + lf;
      Enumeration<String> e = missingBedrNr.elements();
      String tmp;
      while (e.hasMoreElements())
      {
        tmp = (String) e.nextElement();
        errorReport += "\t\t" + tmp + lf;
      }
    }
    return errorReport;
  }

  public boolean foundError()
  {
    return missingBedrNr.size()>0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
