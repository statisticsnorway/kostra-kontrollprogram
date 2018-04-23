package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_38 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K38: Fullførte/avsluttede program - til hvilken livssituasjon gikk deltakeren? Gyldige verdier.";
  private Vector<Integer> linesWithError = new Vector<Integer>();
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String field = RecordFields.getFieldValue(line, 24);
    boolean lineHasError = false;
    boolean isFilled = true;
    
    if (field.equalsIgnoreCase("3")) {            
      
      isFilled = false;
      
      field = RecordFields.getFieldValue(line, 261);
      if (field.equalsIgnoreCase("01"))
        isFilled = true;        
      else if (!(field.equalsIgnoreCase("  ") || field.equalsIgnoreCase("00")))
        lineHasError = true;
      
      field = RecordFields.getFieldValue(line, 262);
      if (field.equalsIgnoreCase("11"))
        isFilled = true;        
      else if (!(field.equalsIgnoreCase("  ") || field.equalsIgnoreCase("00")))
        lineHasError = true;      

      for (int i=3; i<=7; i++) {
      field = RecordFields.getFieldValue(line, 260+i);
      field = field.replace(' ','0');
      if (field.equalsIgnoreCase("0"+Integer.toString(i-1)))
        isFilled = true;        
      else if (!(field.equalsIgnoreCase("  ") || field.equalsIgnoreCase("00")))
        lineHasError = true;
      }
      
      for (int i=8; i<=9; i++) {
      field = RecordFields.getFieldValue(line, 260+i);
      field = field.replace(' ','0');
      if (field.equalsIgnoreCase(Integer.toString(i+4)))
        isFilled = true;        
      else if (!(field.equalsIgnoreCase("  ") || field.equalsIgnoreCase("00")))
        lineHasError = true;
      }

      
      field = RecordFields.getFieldValue(line, 2611);
      if (field.equalsIgnoreCase("10"))
        isFilled = true;        
      else if (!(field.equalsIgnoreCase("  ") || field.equalsIgnoreCase("00")))
        lineHasError = true;
      
/*      
      for (int i=3; i<=11; i++) {
        if (i == 10){
        	continue;        	
        }
        
        field = RecordFields.getFieldValue(line,(i>9?2600+i:260+i));
       
        if (field.equalsIgnoreCase((i!=11?"0":"") + Integer.toString(i-1)))
          isFilled = true;        
        else if (!(field.equalsIgnoreCase("  ") || field.equalsIgnoreCase("00")))
          lineHasError = true;
      }
*/      
    }      
    
    
    if (lineHasError || !isFilled)
      linesWithError.add(new Integer(lineNumber));
    
    return lineHasError || !isFilled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil (i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + "): " +
          "Feltet \"Ved fullført program eller program avsluttet " +
          "etter avtale (gjelder ikke flytting) -" + lf + "\thva var deltakerens viktigste livssituasjon " +
          "umiddelbart etter avslutningen?\" må fylles ut dersom det er " + lf +
          "\tkrysset av for svaralternativ 3 = \"Deltakeren har fullført program eller avsluttet program etter " + lf +
          "\tavtale\" (gjelder ikke flytting) under feltet for \"Hva er status for deltakelsen i kvalifiseringsprogrammet " + lf +
          "\tper 31.12." + Constants.kostraYear + "?\""; 
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
    return Constants.CRITICAL_ERROR;
  }
}