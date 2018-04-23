package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlKvalStonadKode.java,v $
 * Revision 1.4  2008/09/09 09:03:51  pll
 * Bugfix.
 *
 * Revision 1.3  2008/09/05 06:46:56  pll
 * Bugfix.
 *
 * Revision 1.2  2008/09/05 05:52:19  pll
 * Tekstendringer.
 *
 * Revision 1.1  2008/09/04 13:11:07  pll
 * Import
 *
 * Revision 1.2  2007/10/25 09:10:37  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/09/25 08:24:33  pll
 * Versjon: 2007-rapporteringen.
 *
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlKvalStonadKode 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport {
  
  private final String ERROR_TEXT = "K26: Ugyldig kode for måneder med kvalifiseringsstønad.";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {
    
    String field_10 = RecordFields.getFieldValue(line, 10);

    if (field_10.equalsIgnoreCase("8")) {
      
      String field;
      int int_value;
      
      //Felt 17.1 - 17.12:
      for (int i=12; i>0; i--) {
        
        field = RecordFields.getFieldValue(line, (i>9?Integer.parseInt("17"+i):170+i));
        field = field.replace(' ', '0');
        
        try {
          
          int_value = Integer.parseInt(field);
          if (int_value != i && int_value != 0) {
            linesWithError.add(new Integer(lineNumber));
            return true;
          }
          
        } catch (NumberFormatException e) {
          linesWithError.add(new Integer(lineNumber));
          return true;
        }
      }
    }
            
    return false;
  }

  
  
  
  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
          "er det krysset av for kvalifiseringsstønad under viktigste kilde " + lf +
          "\ttil livsopphold, men det er ikke oppgitt hvilke måneder mottakeren " +
          "har fått kvalifiseringsstønad." + lf + 
          "\tFeltet er obligatorisk hvis kvalifiseringsstønad er oppgitt som " +
          "viktigste kilde til livsopphold. ";           
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + linesWithError.elementAt(i);
        }
        errorReport += ").";
      }
      else
      {
        errorReport += lf + "\t\t(Gjelder " + numOfRecords + " records).";
      }
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return linesWithError.size() > 0;
  }  

  public String getErrorText()
  {
    return  ERROR_TEXT;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}