package no.ssb.kostra.control.regnskap.regn0A;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class ControlKombinasjonFunksjonOgArt 
    extends no.ssb.kostra.control.Control
{
  private Vector<String[]> invalidCombinations = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
  
    String art = RecordFields.getArt(line);
	String funksjon = RecordFields.getFunksjon(line);
    
    if (
    	art.equalsIgnoreCase("070") ||
    	art.equalsIgnoreCase("075") ||
    	art.equalsIgnoreCase("230") ||
    	art.equalsIgnoreCase("250") ||
    	art.equalsIgnoreCase("260")
    	)
    { 

      if (!funksjon.equalsIgnoreCase ("130") &&
    	  !funksjon.equalsIgnoreCase ("221") &&
    	  !funksjon.equalsIgnoreCase ("222") &&
    	  !funksjon.equalsIgnoreCase ("261") &&
    	  !funksjon.equalsIgnoreCase ("381") &&
    	  !funksjon.equalsIgnoreCase ("386"))
      { 
        lineHasError = true;
      }
    }
    if (lineHasError) {
        lineHasError = true;
        String[] container = new String[3];
        container[0] = funksjon;
        container[1] = art;
        container[2] = Integer.toString (lineNumber);
        invalidCombinations.add (container);
      }
    return lineHasError;

  }


  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 22, byggrelarte arter skal føres på byggfunksjonene for de " + lf +
    "tjenesteområdene som hører til disse funksjonene." + lf +
    "Der tjenesteområdet ikke har egen byggfunksjon benyttes den aktuelle tjenestefunksjonen. " + lf +
    "Opplistingen nedenfor er kun veiledende og ment som en påminnelse. " + lf +
    "Dataene kan sendes inn uavhengig av denne kontrollen.";

    if (foundError())
    {
      int numOfRecords = invalidCombinations.size();
      errorReport += "\tFeil: Arten er kun logisk i kombinasjon med byggfunksjonene." + lf; 
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidCombinations.elementAt(i);
        errorReport += "\t\tfunksjon: " + container[0] + 
            " art: " + container[1] + " (Record nr. " + container[2] + ")" + lf;
      }
    }
    errorReport += "\tKorreksjon: Sjekk om bruken av byggrelaterte arter "  + lf +
        "\ter ført på formålsbyggfunksjonene." + lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidCombinations.size() > 0;
  }  

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}