package no.ssb.kostra.control.regnskap.regn0K;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class Control22b 
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String kontoklasse = RecordFields.getKontoklasse (line);
    String funksjon = RecordFields.getFunksjon (line);
    String art = RecordFields.getArt(line);
    int belop  = RecordFields.getBelopIntValue(line);
   
   if (belop != 0)
   {
	  if (art.equalsIgnoreCase ("729"))
		{
			if (!funksjon.equalsIgnoreCase ("841") && kontoklasse.equalsIgnoreCase("4"))
			{
			        lineHasError = true;
			        String[] container = {kontoklasse, funksjon, art, Integer.toString (lineNumber)};  
			        invalidCombinations.add (container);
			}
		}
   }
    
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 22b, kombinasjon kontoklasse, funksjon og art:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = invalidCombinations.size();
      errorReport += "\tFeil: I investeringsregnskapet, kontoklasse 4, er art 729 kun tillat brukt i kombinasjon med funksjon 841." + lf; 
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidCombinations.elementAt(i);
        errorReport += "\t\tkontoklasse: " + container[0] + 
            ", funksjon: " + container[1] +
            ", art: " + container[2] + " (Record nr. " + container[3] + ")" + lf;
      }
    }
    errorReport += lf + "\tKorreksjon: Rett opp til riktig kombinasjon av kontoklasse, funksjon og art." + lf + lf;
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