package no.ssb.kostra.control.sensitiv.famvern;

import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control16 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K16: Utdypende om primærklientens formelle sivilstand";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
     boolean lineHasError = false;     
     String field_9 = RecordFields.getFieldValue(line, 9);
     
     if (field_9.equalsIgnoreCase("3") || field_9.equalsIgnoreCase("4")) {
         
         String field_9_1 = RecordFields.getFieldValue(line, 91);
         try {
             int kode = Integer.parseInt(field_9_1);
             lineHasError = kode<1 || kode>6;
         } catch (NumberFormatException e) {
             lineHasError = true;
         }
     }

     if (lineHasError)
         lineNumbers.add(new Integer(lineNumber));
     
     return lineHasError;
      
      
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + 
      " er det oppgitt at primærklientens samlivsstatus er Samboer eller " + lf +
      "\tat primærklient ikke lever i samliv, men det er ikke fylt ut hva " + lf +
      "\tsom er primærklientens korrekt sivilstand."; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + lineNumbers.elementAt(i);
        }
        errorReport += ").";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return lineNumbers.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}
