package no.ssb.kostra.control.regnskap.regn0K;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class ControlSkatteinntekter extends no.ssb.kostra.control.Control {

  private Vector<String[]> invalidArter = new Vector<String[]>();
  private boolean hasFoundFunction800 = false;
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    String art = RecordFields.getArt(line);
    boolean lineHasError = false; 
    if(isInvalidArt(art)) {
      lineHasError = true;
      String[] container = new String[2];
      container[0] = Integer.toString (lineNumber);
      container[1] = art;
      invalidArter.add(container);
    }
    String function = RecordFields.getFunksjon(line); 
    if(isInvalidFunction(function)) {
      lineHasError = true;
      hasFoundFunction800 = true;
    }
    return lineHasError; 
  }
  
  private boolean isInvalidArt(String art) {
    return (art.equalsIgnoreCase("800") || art.equalsIgnoreCase("870") ||
            art.equalsIgnoreCase("874") || art.equalsIgnoreCase("875") || 
            art.equalsIgnoreCase("877")); 
  }
  
  private boolean isInvalidFunction(String function) {
    return function.equalsIgnoreCase("800"); 
  }
  
  public String getErrorReport(int totalLineNumber) {
    String errorReport = "Kontroll 13, skatteinntekter:" + lf + lf;
    if (hasFoundFunction800) {
      errorReport += "\tFeil: Ugyldig funksjon, skatteinntekter skal ikke forekomme i skjema 0K (funksjon 800)" +lf;
    }
    int numOfRecords = invalidArter.size();
    if (numOfRecords > 0) {
      errorReport += "\tFeil: Ugyldig" + (numOfRecords ==1 ? "" : "e") + 
          " art" + (numOfRecords ==1 ? "" : "er") + ", statlig rammetilskudd " +
          "eller skatteinntekter skal ikke forekomme i skjema 0K " + lf;
      for (int i=0; i<numOfRecords; i++) {
        String[] container = (String[]) invalidArter.elementAt(i);
        errorReport += "\t\tart " + container[1] + 
            " (Record nr. " + container[0] + ")" + lf;
      }
    }
    errorReport += lf;
    return errorReport; 
  }
  
  public boolean foundError() {
    return (invalidArter.size() > 0 || hasFoundFunction800);
  }

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }

}
