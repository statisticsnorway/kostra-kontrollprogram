package no.ssb.kostra.control.regnskap.regn0Akvartal;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

/**
 * $Log: ControlAargang.java,v $
 * Revision 1.4  2008/10/23 09:13:52  pll
 * Benytter egen konstant, no.ssb.kostra.control.Constants.kvartalKostraYear.
 *
 * Revision 1.3  2008/10/21 10:53:23  pll
 * Setter konstanten YEAR lokalt.
 *
 * Revision 1.2  2008/03/13 14:06:29  pll
 * Tilpasning til kvartalsregnskap.
 * StringBuffer isteden for String.
 *
 * Revision 1.1  2008/03/11 11:56:04  pll
 * Import.
 *
 * Revision 1.4  2007/11/29 11:25:06  pll
 * Fanger opp Exception isteden for NumberFormatException.
 *
 * Revision 1.3  2007/10/25 11:07:46  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/10/03 08:12:14  pll
 * Benytter no.ssb.kostra.control.Constants.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:06  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:37  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:08  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.2  2005/10/26 15:34:45  lwe
 * lister nå de records som slår ut med feil årstall (+tester javadoc/cvs-log)
 * 
 * 
 * Test av javadoc 
 */

final class ControlAargang extends no.ssb.kostra.control.Control {
  
  
  private Vector<Integer> previousYear = new Vector<Integer>();
  private Vector<Integer> nextYear = new Vector<Integer>();
  private Vector<Integer> otherYears = new Vector<Integer>();

  private final int YEAR = Integer.parseInt(Constants.kvartalKostraYear);
    
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    boolean lineHasError = false;
    
    try {
      
      int year = RecordFields.getAargangIntValue (line);

      if (year != YEAR) {
        
        lineHasError = true;
        
        if (year == (YEAR - 1))
          previousYear.add (new Integer (lineNumber));
        else if (year == (YEAR + 1))
          nextYear.add (new Integer (lineNumber));
        else
          otherYears.add (new Integer (lineNumber));
      }
      
    } catch (Exception e) {
      lineHasError = true;
      otherYears.add (new Integer (lineNumber));
    }
    
    return lineHasError;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    StringBuffer errorReport = 
        new StringBuffer ("Kontroll 2, årgang:" + lf + lf);
    
    int numOfRecords = previousYear.size();    
    if (numOfRecords > 0) {
      
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører fjorårets bevilgningsregnskap: " + lf); 
      
      for(int i=0; i<previousYear.size(); i++) 
          errorReport.append("\t\t Record nr. " + previousYear.get(i) + lf); 
      
    }
    
    numOfRecords = nextYear.size();    
    if (numOfRecords > 0) {
      
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører neste års bevilgningsbudsjett." + lf); 
      
      for(int i=0; i<nextYear.size(); i++) 
          errorReport.append("\t\t Record nr. " + nextYear.get(i) + lf);     
      
    }
    
    numOfRecords = otherYears.size();
    if (numOfRecords > 0) {
      
      errorReport.append("\tFeil: " + numOfRecords + " av totalt " + totalLineNumber + 
          " records tilhører andre års bevilgningsregnskap/-budsjett." + lf); 
      
      for(int i=0; i<otherYears.size(); i++) 
          errorReport.append("\t\t Record nr. " + otherYears.get(i) + lf); 
          
    }
    
    errorReport.append(lf);
    
    return errorReport.toString();
  }

  
  
  
  public boolean foundError()
  {
    return (nextYear.size() > 0 ||
            previousYear.size() > 0 ||
            otherYears.size() > 0);
  }  

  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}