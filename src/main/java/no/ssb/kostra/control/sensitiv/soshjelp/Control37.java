package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */
 
import java.util.Vector;

import no.ssb.kostra.control.Constants;

public final class Control37 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K37: lån fordelt på måneder";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
	int field_152_int, sum_fields_16;
    String field_152, field_162, field_164, field_166, field_168, field_1610, field_1612, field_1614, field_1616, field_1618, field_1620, field_1622, field_1624;
	boolean lineHasError = false;

//  Import fields:
    field_152 = RecordFields.getFieldValue(line, 152);
    field_162 = RecordFields.getFieldValue(line, 162);
    field_164 = RecordFields.getFieldValue(line, 164);
    field_166 = RecordFields.getFieldValue(line, 166);
    field_168 = RecordFields.getFieldValue(line, 168);
    field_1610 = RecordFields.getFieldValue(line, 1610);
    field_1612 = RecordFields.getFieldValue(line, 1612);
    field_1614 = RecordFields.getFieldValue(line, 1614);
    field_1616 = RecordFields.getFieldValue(line, 1616);
    field_1618 = RecordFields.getFieldValue(line, 1618);
    field_1620 = RecordFields.getFieldValue(line, 1620);
    field_1622 = RecordFields.getFieldValue(line, 1622);
    field_1624 = RecordFields.getFieldValue(line, 1624);
  
//  Erstatter spece med 0:
    field_152 = field_152.replace(' ', '0');
    field_162 = field_162.replace(' ', '0');
    field_164 = field_164.replace(' ', '0');
    field_166 = field_166.replace(' ', '0');
    field_168 = field_168.replace(' ', '0');
    field_1610 = field_1610.replace(' ', '0');
    field_1612 = field_1612.replace(' ', '0');
    field_1614 = field_1614.replace(' ', '0');
    field_1616 = field_1616.replace(' ', '0');
    field_1618 = field_1618.replace(' ', '0');
    field_1620 = field_1620.replace(' ', '0');
    field_1622 = field_1622.replace(' ', '0');
    field_1624 = field_1624.replace(' ', '0');


//  Int fields:
    field_152_int = Integer.parseInt(field_152);
    sum_fields_16 = Integer.parseInt(field_162) +
    	            Integer.parseInt(field_164) +
    	            Integer.parseInt(field_166) +
    	            Integer.parseInt(field_168) +
    	            Integer.parseInt(field_1610) +
    	            Integer.parseInt(field_1612) +
    	            Integer.parseInt(field_1614) +
    	            Integer.parseInt(field_1616) +
    	            Integer.parseInt(field_1618) +
    	            Integer.parseInt(field_1620) +
    	            Integer.parseInt(field_1622) +
    	            Integer.parseInt(field_1624);

//	int field_152_int = Integer.parseInt(RecordFields.getFieldValue(line, 152));
//    int sum_fields_16 =  Integer.parseInt(RecordFields.getFieldValue(line, 162)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 164)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 166)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 168)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1610)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1612)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1614)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1616)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1618)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1620)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1622)) +
//    					 Integer.parseInt(RecordFields.getFieldValue(line, 1624));

    try
    {
    	if(field_152_int > 0)
    	lineHasError = field_152_int !=  sum_fields_16;
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
          "\tDet er ikke fylt ut lån fordelt på måneder eller " + lf +
          "\tsum stemmer ikke med sum lån utbetalt i løpet av året.";           
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