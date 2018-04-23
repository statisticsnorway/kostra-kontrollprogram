package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: ControlBedriftsNummer.java,v $
 * Revision 1.3  2010/10/22 08:03:59  pll
 * Klar for 2010-rapp.
 *
 * Revision 1.2  2007/10/25 11:33:57  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:55  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:06  pll
 * Import av 2005-filer
 *
 * Revision 1.3  2005/12/22 09:14:28  lwe
 * etter kravspec: endret feilmeldingstekst
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlBedriftsNummer extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 5, Bedriftsnummer:";
  private Vector<Integer> recordNumbers = new Vector<Integer>();
  //private final int AAR = Constants.kostraYear;  //her har er det brukt hardkoding på årgang. Referer til Constants.KosraYear

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String bedriftsNum = RecordFields.getForetaksNummer(line);

    boolean invalidOrgNummer = 
        ! no.ssb.helseforetak.utils.OrgNummer.orgNrIsValid(bedriftsNum);

    boolean invalidBedrNummer = 
        ! no.ssb.helseforetak.utils.OrgNummer.bedrNrIsValid(bedriftsNum);

    boolean lineHasError = (invalidOrgNummer && invalidBedrNummer);

    if (lineHasError)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
     errorReport += "\tFeil: Det er benyttet feil bedriftsnummer i " + 
       numOfRecords + " av " + totalLineNumber + " records." + lf +
       "\tDersom helseforetaket ikke leverer regnskapet fordelt på institusjoner skal " + lf + 
       "\torganisasjonsnummeret gjentas i posisjonene 23-31. Regionale helseforetak (RHF) " + lf +
       "\tsom har under-bedrifter som skal rapporteres med, benytter bedriftsnummeret " + lf +
       "\ti posisjon 23-31. " + lf +
       "\tDersom RHF-et ikke har under-bedrifter, gjentar foretakets organisasjonsnummer " + lf +
       "\ti posisjonene 23-31." + lf +
       "\tFor nærmere informasjon om institusjonsnumre og kontrollen for øvrig " + lf + 
       "\tvises det til Håndbok for rapportering av regnskapsdata for helseforetak og " + lf + 
       "\tregionale helseforetak " + Constants.kostraYear + "." + lf;
          
      if (numOfRecords <= 10)
      {
        errorReport += "\t\t(Gjelder record nr.:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")" + lf;
      }
      errorReport += lf + lf;
    }
    return errorReport;
  }

  public boolean foundError()
  {
    return recordNumbers.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
