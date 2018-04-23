package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_39 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K39: Fullførte/avsluttede program - til hvilken stønadssituasjon gikk deltakeren? Gyldige verdier";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;
    String field = RecordFields.getFieldValue(line, 24);
    
    if (field.equalsIgnoreCase("3")) {            
      
      field = RecordFields.getFieldValue(line, 27);
      
      lineHasError = !(field.equalsIgnoreCase("1") ||
                       field.equalsIgnoreCase("2") ||
                       field.equalsIgnoreCase("3") ||
                       field.equalsIgnoreCase("4") ||
                       field.equalsIgnoreCase("5") ||
                       field.equalsIgnoreCase("6") ||
                       field.equalsIgnoreCase("7"));
      
    }      

    if (lineHasError)
      linesWithError.add(new Integer(lineNumber));
    
    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
	  String errorReport = "";
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {     
      errorReport = ERROR_TEXT + ":" + lf;
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): " +
          "Feltet \"Ved fullført program eller program avsluttet etter " +
          "avtale -" + lf + "\ttil hvilken stønadssituasjon gikk deltakeren " +
          "umiddelbart etter avslutningen?\" må " + lf + "\tfylles ut dersom " +
          "det er krysset av for svaralternativ 3 = \"Deltakeren har fullført" +
          lf + "\tprogram eller avsluttet program etter avtale\" (gjelder ikke flytting)" +
          " under feltet " + lf + "\tfor \"Hva er status for deltakelsen i " +
          "kvalifiseringsprogrammet per 31.12." + Constants.kostraYear + "?\""; 
      if (numOfRecords <= 10)
      {
        errorReport += lf + "\t\t(Gjelder record nr.";
        for (int i=0; i<numOfRecords; i++)
        {
          errorReport += " " + linesWithError.elementAt(i);
        }
        errorReport += ").";
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