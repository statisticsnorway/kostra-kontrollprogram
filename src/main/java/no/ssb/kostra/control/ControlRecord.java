/*
 * ControlRecord.java
 *
 * Created on 9. oktober 2008, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.ssb.kostra.control;

import java.util.Vector;

/**
 *
 * @author pll
 */
public class ControlRecord extends Control {
  
  
  private Vector<Integer> lineNumbers = new Vector<Integer>();
  private String[] illegalCharacters = new String[] {"\t"};
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    boolean hasError = false;
    
    for (int i=illegalCharacters.length-1; i>=0; i--) {
      if (line.indexOf(illegalCharacters[i]) != -1) {
        hasError = true;
        lineNumbers.add(new Integer(lineNumber));
      }
    }
        
    return hasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    StringBuilder errorReport = 
        new StringBuilder("Kontroll av ulovlige tegn (f.eks. TAB/tabulator-tegn) i filuttrekket:" + lf + lf);

    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport.append("\tFeil: ulovlige tegn i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + "." + lf);

      if (numOfRecords <= 10)
      {
        errorReport.append("\t(Gjelder record nr.");
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport.append(" " + lineNumbers.elementAt(i));
        }
        errorReport.append(").");
      }
    }
    errorReport.append(lf + "\tKorreksjon: Fjern ulovlige tegn." + lf + lf);
    
    return errorReport.toString();
  }

  
  
  
  public boolean foundError() {
    
    return lineNumbers.size() > 0;
  }

  
  
  
  public int getErrorType() {
    
    return Constants.CRITICAL_ERROR;
  }
  
}
