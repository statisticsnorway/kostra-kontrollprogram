package no.ssb.kostra.control.regnskap.regn0P;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class Control22a 
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String kontoklasse = RecordFields.getKontoklasse (line);
    String funksjon = RecordFields.getFunksjon (line);
    int belop = RecordFields.getBelopIntValue(line);

    if (belop != 0)
    {
	  if (funksjon.equalsIgnoreCase ("841"))
		{
			if (!kontoklasse.equalsIgnoreCase("0"))
			{
			        lineHasError = true;
			        String[] container = {kontoklasse, funksjon, Integer.toString (lineNumber)};  
			        invalidCombinations.add (container);
			}
		}
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 22a, kombinasjon kontoklasse, funksjon og art:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = invalidCombinations.size();
      errorReport += "\tFeil: Funksjon 841 er kun tillatt brukt i investeringsregnskapet, kontoklasse 0." + lf; 
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidCombinations.elementAt(i);
        errorReport += "\t\tkontoklasse: " + container[0] + 
            " funksjon: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
      }
    }
    errorReport += lf + "\tKorreksjon: Rett opp feil kontoklasse eller funksjon. I driftsregnskapet skal"
    			 + lf + "\tmva-kompensasjon (art 729) føres på funksjonen hvor utgiften oppstod." + lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidCombinations.size() > 0;
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}