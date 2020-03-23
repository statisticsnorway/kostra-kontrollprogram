package no.ssb.kostra.control.regnskap.regn0F;

import java.util.Vector;

import no.ssb.kostra.control.Constants;

final class ControlFunksjon089 extends no.ssb.kostra.control.Control
{
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    try {
	String kontoklasse = RecordFields.getKontoklasse (line);
    int funksjon = RecordFields.getFunksjonIntValue(line);
    int art = RecordFields.getArtIntValue (line);

	boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || kontoklasse.equalsIgnoreCase("4"));
	boolean rettFunksjon = (funksjon == 89);
    boolean rettArt = ((art >= 500 && art <= 580) || (art == 830) || (art >= 900 && art <= 980));
    if (rettFunksjon && !rettArt && rettKontoklasse){
    	lineNumbers.add(lineNumber);
    	return true;
    }
  } catch (Exception e) 
  {
	e.printStackTrace();
    //Maa logges!
  }
    return false;
}

  public String getErrorReport(int totalLineNumber) {
	    String errorReport = "Kontroll 18, Funksjon 089 - Gjelder for artene (500-580), (830) og (900-980)" + lf + lf;
	    if (foundError())
	    {
	      int numOfRecords = lineNumbers.size();	
	      errorReport += "\tFeil: Artene 500-580, 830 og artene 900-980 er tillat brukt mot funksjon 3.089 og 4.089. Kontrollen hindrer ikke innsending." + lf +
	          "\tKorreksjon: Rett opp art i drifts- / investeringsregnskapet som er gyldig mot funksjon 3.089 og 4.089." + lf + lf;
	      if (numOfRecords <= 10)
	      {
	        errorReport += " (Gjelder record nr.";
	        for (int i=0; i<numOfRecords; i++)
	        {
	          errorReport += " " + lineNumbers.elementAt(i);
	        }
	        errorReport += ").";
	      }    }    
	    return errorReport;
	  }
	  
	  public boolean foundError()
	  {
	    return lineNumbers.size() > 0;
	  }  
	  
	  public int getErrorType() {
	    return Constants.NORMAL_ERROR;
	  }
}