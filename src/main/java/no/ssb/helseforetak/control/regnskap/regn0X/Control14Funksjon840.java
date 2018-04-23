package no.ssb.helseforetak.control.regnskap.regn0X;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class Control14Funksjon840 extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 14, funksjon 840 skal kun omfatte inntektsposter:";  
  private Vector<String[]> invalidKontokoder = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    
    String funksjon = RecordFields.getFunksjon(line);
    
    if (funksjon.equalsIgnoreCase("840"))
    {
      int kontokode = 0;
      
        try {
          
          kontokode = RecordFields.getKontokodeIntValue(line);
          
        } catch (Exception e) {
          return lineHasError;
        }  
          
        if (kontokode < 300 || kontokode > 399)
        {
            lineHasError = true;
            String[] container = new String[2];
            container[0] = Integer.toString (lineNumber);
            container[1] = RecordFields.getKontokode(line);
            invalidKontokoder.add (container);
        }
    }
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    int numOfRecords = invalidKontokoder.size();
    if (numOfRecords > 0)
    {
      errorReport += "\tFeil: Ugyldig" + (numOfRecords ==1 ? "" : "e") + 
          " kontokode" + (numOfRecords ==1 ? "" : "r") + " i kombinasjon med funksjon 840:" + lf;
      for (int i=0; i<numOfRecords; i++)
      {
        String[] container = (String[]) invalidKontokoder.elementAt(i);
        errorReport += "\t\tkontokode " + container[1] + 
            " (Record nr. " + container[0] + ")" + lf;
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return invalidKontokoder.size() > 0;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}