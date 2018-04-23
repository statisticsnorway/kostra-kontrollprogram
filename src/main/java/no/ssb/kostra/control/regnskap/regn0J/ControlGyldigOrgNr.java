package no.ssb.kostra.control.regnskap.regn0J;

import java.util.Vector;
import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.OrgNrForIKS;

final class ControlGyldigOrgNr extends no.ssb.kostra.control.Control {
  
  private Vector<Integer> recordNumbers = new Vector<Integer>();
  private OrgNrForIKS org_nr_checker = new OrgNrForIKS();
  
  
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    String orgNum = RecordFields.getOrgNummer (line);

    boolean lineHasError 
        = ! (org_nr_checker.isValidIksOrgNr(orgNum) || org_nr_checker.isValidKfOrgNr(orgNum));

    if (lineHasError)
    {
      recordNumbers.add (new Integer (lineNumber));
    }
    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = lf + "Kontroll 13, gyldig organisasjonsnummer for sÃ¦rbedriftene:" + lf + lf;
    int numOfRecords = recordNumbers.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: Ugyldig organisasjonsnummer. " + lf + 
          "\tI " + numOfRecords + " av " + totalLineNumber + " records " +
          "er oppgitt organisasjonsnummer ikke korrekt " + lf +
          "\tihht. Brønnøysundregistrets opplysninger pr 15. september " + Constants.kostraYear + ". " + 
          lf + "\tDersom foretaket ikke er registrert for regnskapsåret " + Constants.kostraYear +
          lf + "\tbes sÃ¦rskilt melding gitt til SSB";      
      if (numOfRecords <= 10)
      {
        errorReport += "(Gjelder følgende records:";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + recordNumbers.elementAt(i);
        }
        errorReport += ")";
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
    return Constants.CRITICAL_ERROR;
  }
}
