package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class Control_27 extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
      "K27: Mottatt økonomisk sosialhjelp, kommunal bostøtte eller " +
      "Husbankens bostøtte i tillegg til kvalifiseringsstønad i løpet av " +
       Constants.kostraYear + ". Svaralternativer";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	String field1;
    boolean isFilled = false;
    boolean lineHasError = false;

    String field = RecordFields.getFieldValue(line, 201);
    if (field.equalsIgnoreCase("1"))
        {
    	for (int i=2; i<=3; i++) {
    	field1 = RecordFields.getFieldValue(line, 200+i);
        field1 = field1.replace(' ','0');
          if (
        	  field1.equalsIgnoreCase(Integer.toString(i+2)) ||
        	  field1.equalsIgnoreCase(Integer.toString(0))
        	  )
          isFilled = true;      
          else if (!field1.equalsIgnoreCase("0"))
          lineHasError = true;
    	}

    	field1 = RecordFields.getFieldValue(line, 204);
        field1 = field1.replace(' ','0');
        if (
          	  field1.equalsIgnoreCase(Integer.toString(9)) ||
          	  field1.equalsIgnoreCase(Integer.toString(0))
          	)
          isFilled = true;      
          else if (!field.equalsIgnoreCase("00"))
          lineHasError = true;

        field1 = RecordFields.getFieldValue(line, 205);
        field1 = field1.replace(' ','0');
        if (
        	  field1.equalsIgnoreCase(Integer.toString(8)) ||
        	  field1.equalsIgnoreCase(Integer.toString(0))
        	)
        isFilled = true;      
        else if (!field.equalsIgnoreCase("00"))
        lineHasError = true;

        field1 = RecordFields.getFieldValue(line, 206);
        field1 = field1.replace(' ','0');
        if (
        	  field1.equalsIgnoreCase(Integer.toString(7)) ||
        	  field1.equalsIgnoreCase(Integer.toString(0))
        	)
        isFilled = true;      
        else if (!field.equalsIgnoreCase("00"))
        lineHasError = true;
        }

    else if (field.equalsIgnoreCase("2"))
    {
	for (int i=2; i<=6; i++) {
	field1 = RecordFields.getFieldValue(line, 200+i);
    field1 = field1.replace(' ','0');
      if (
    	  field1.equalsIgnoreCase(Integer.toString(0))
    	  )
      isFilled = true;      
      else if (!field1.equalsIgnoreCase("0"))
      lineHasError = true;
	}
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
          " record" + (numOfRecords == 1 ? "" : "s") + "): Feltet \"Har " +
          "deltakeren i " + Constants.kostraYear + " i løpet av perioden med kvalifiseringsstønad " + lf +
          "\togså mottatt  økonomisk sosialhjelp, kommunal bostøtte eller " +
          "Husbankens bostøtte?\"" + lf + "\ter ikke utfylt eller feil kode er benyttet. " +
          "Feltet er obligatorisk å fylle ut"; 
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