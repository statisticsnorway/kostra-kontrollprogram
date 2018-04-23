package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Control19A.java,v $
 * Revision 1.1  2008/12/13 07:39:45  pll
 * no message
 *
 * Revision 1.2  2007/10/25 11:37:03  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:47  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:25  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.6  2006/01/05 08:16:30  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control19A extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K19A: Varighet på relasjon mellom primærklient og viktigste samtalepartner, partnere";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false; 
    String field_12 = RecordFields.getFieldValue(line, 12);
    
    if (field_12.equalsIgnoreCase("1")) {
        
        String field_13 = RecordFields.getFieldValue(line, 13);

        try {
            int kode = Integer.parseInt(field_13);
            lineHasError = kode<1 || kode>6;
        } catch (NumberFormatException e) {
            lineHasError = true;
        }        
    }
      
    if (lineHasError)
      lineNumbers.add (new Integer (lineNumber));
    
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + 
      " er det oppgitt at primærklientens relasjon til viktigste deltager " + lf + 
      "\ter partner, men det er ikke oppgitt hvor lenge partene har vært " + lf +
      "\tgift, samboere eller registrerte partnere."; 
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
