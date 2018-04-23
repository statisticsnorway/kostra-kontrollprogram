package no.ssb.helseforetak.control.regnskap.regn0X;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

final class Control17Konto327og457 extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 17, konto 327 og konto 457:";  
  private Vector<String[]> invalidKontokoder = new Vector<String[]>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    int belop = 0;
    
    try {
      
      belop = RecordFields.getBelopIntValue(line);    
      
    } catch (Exception e) {
      return lineHasError;
    }
    
    
    if (belop != 0)
    {
      int kontokode = 0;
      
        try {
          
          kontokode = RecordFields.getKontokodeIntValue(line);
          
        } catch (Exception e) {
          return lineHasError;
        }  

      if (kontokode == 327 || kontokode == 457)
      {
          String regon_in_record = RecordFields.getRegion(line);
          if (! regon_in_record.equalsIgnoreCase("120000"))
          {
              lineHasError = true;
              String[] container = new String[2];
              container[0] = Integer.toString (lineNumber);
              container[1] = RecordFields.getKontokode(line);
              invalidKontokoder.add (container);
          }
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
        errorReport += "\tUgyldige kontokoder. Dette er kontoer som kun Helse Sør-Øst kan benytte." + lf;
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