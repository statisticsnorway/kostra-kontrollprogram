package no.ssb.kostra.control.regnskap.regn0F;

import no.ssb.kostra.control.Constants;

import java.util.Vector;

final class ControlArt830 extends no.ssb.kostra.control.Control
{
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  boolean lineControlled = false;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    try {
	String kontoklasse = RecordFields.getKontoklasse (line);
    int funksjon = RecordFields.getFunksjonIntValue(line);
    int art = RecordFields.getArtIntValue (line);

	boolean rettKontoklasse = (kontoklasse.equalsIgnoreCase("3") || kontoklasse.equalsIgnoreCase("4"));
	boolean rettFunksjon = (funksjon != 89);
    boolean rettArt = (art == 830);
//    if (rettFunksjon && !rettArt && rettKontoklasse){
      if (rettArt && rettKontoklasse && rettFunksjon){
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
	    String errorReport = "Kontroll 19, Kombinasjon kontoklasse, funksjon og art" + lf + lf;
	    if (foundError())
	    {
	      int numOfRecords = lineNumbers.size();	
	      errorReport += "\tFeil: Art 830 er kun tillat brukt mot funksjon 3.089 og 4.089." + lf +
	          "\tKorreksjon: Rett opp slik at art 830 ikke benyttes på andre funksjoner enn 089." + lf + lf;
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
	    return Constants.CRITICAL_ERROR;
	  }
}