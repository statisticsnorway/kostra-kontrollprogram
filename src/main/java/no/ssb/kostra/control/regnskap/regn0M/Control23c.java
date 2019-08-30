package no.ssb.kostra.control.regnskap.regn0M;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class Control23c
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String kontoklasse = RecordFields.getKontoklasse (line);
    String funksjon = RecordFields.getFunksjon (line);
    String art = RecordFields.getArt(line);
    int belop = RecordFields.getBelopIntValue(line);

   if(belop != 0)
    {
	  if (art.equalsIgnoreCase ("874") || art.equalsIgnoreCase ("875"))
		{
			if (!funksjon.equalsIgnoreCase ("800") && kontoklasse.equalsIgnoreCase("3"))
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
    String errorReport = "Kontroll 23c, kombinasjon kontoklasse, funksjon og art:" + lf + lf;
    if (foundError())
    {
      int numOfRecords = invalidCombinations.size();
      errorReport += "\tFeil: I driftsregnskapet, kontoklasse 3, er artene 874 og 875 kun tillat brukt i kombinasjon med funksjon 800." + lf;
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidCombinations.elementAt(i);
        errorReport += "\t\tkontoklasse: " + container[0] + 
            ", funksjon: " + container[1] +
            ", og art: " + container[2] + " (Record nr. " + container[3] + ")" + lf;
      }
    }
    errorReport += "\tKorreksjon: Rett opp til riktig kombinasjon av kontoklasse, funksjon og art." + lf + lf;
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