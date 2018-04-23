package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */
 
import java.util.Vector;

import no.ssb.kostra.control.Constants;

public final class Control36 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K36: Bidrag fordelt på måneder";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	int field_151_int, sum_fields_16;
    String field_151, field_161, field_163, field_165, field_167, field_169, field_1611, field_1613, field_1615, field_1617, field_1619, field_1621, field_1623;
    boolean lineHasError = false;

//    Import fields:
    field_151 = RecordFields.getFieldValue(line, 151);
    field_161 = RecordFields.getFieldValue(line, 161);
    field_163 = RecordFields.getFieldValue(line, 163);
    field_165 = RecordFields.getFieldValue(line, 165);
    field_167 = RecordFields.getFieldValue(line, 167);
    field_169 = RecordFields.getFieldValue(line, 169);
    field_1611 = RecordFields.getFieldValue(line, 1611);
    field_1613 = RecordFields.getFieldValue(line, 1613);
    field_1615 = RecordFields.getFieldValue(line, 1615);
    field_1617 = RecordFields.getFieldValue(line, 1617);
    field_1619 = RecordFields.getFieldValue(line, 1619);
    field_1621 = RecordFields.getFieldValue(line, 1621);
    field_1623 = RecordFields.getFieldValue(line, 1623);
    
//    Erstatter spece med 0:
    field_151 = field_151.replace(' ', '0');
    field_161 = field_161.replace(' ', '0');
    field_163 = field_163.replace(' ', '0');
    field_165 = field_165.replace(' ', '0');
    field_167 = field_167.replace(' ', '0');
    field_169 = field_169.replace(' ', '0');
    field_1611 = field_1611.replace(' ', '0');
    field_1613 = field_1613.replace(' ', '0');
    field_1615 = field_1615.replace(' ', '0');
    field_1617 = field_1617.replace(' ', '0');
    field_1619 = field_1619.replace(' ', '0');
    field_1621 = field_1621.replace(' ', '0');
    field_1623 = field_1623.replace(' ', '0');

//    Int fields:
    field_151_int = Integer.parseInt(field_151);
    sum_fields_16 = Integer.parseInt(field_161) +
    				Integer.parseInt(field_163) +
    				Integer.parseInt(field_165) +
    				Integer.parseInt(field_167) +
    				Integer.parseInt(field_169) +
    				Integer.parseInt(field_1611) +
    				Integer.parseInt(field_1613) +
    				Integer.parseInt(field_1615) +
    				Integer.parseInt(field_1617) +
    				Integer.parseInt(field_1619) +
    				Integer.parseInt(field_1621) +
    				Integer.parseInt(field_1623);

    try
    {
    	if(field_151_int > 0)
    	lineHasError = field_151_int !=  sum_fields_16;
    }
    catch (NumberFormatException e)
    {
      lineHasError = true;  
    }

    if (lineHasError)
    {
      linesWithError.add(new Integer(lineNumber));
    }

    return lineHasError;
  }
  
  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + " " + lf +
          "\tDet er ikke fylt ut bidrag fordelt på måneder eller " + lf +
          "\tsum stemmer ikke med sum bidrag utbetalt i løpet av året.";           
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