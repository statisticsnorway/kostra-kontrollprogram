package no.ssb.kostra.control.sensitiv.famvern_53;

/*
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlFylkeKontornummer extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K5: Manglende samsvar mellom fylkes- og kontornummer";
  private Vector<Integer> lineNumbers = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String fylkesNumber = RecordFields.getRegionNr(line);
    String kontorNumber = RecordFields.getKontornummer(line);

    {
      String field1;
      boolean isFilled = false;
      boolean lineHasError = false;

      String field = fylkesNumber;
      if (field.equalsIgnoreCase("01"))
          {
       	   field1 = kontorNumber;
           field1 = field1.replace(' ','0');
           if (
            	field1.equalsIgnoreCase("016") ||
            	field1.equalsIgnoreCase("017")
              )
            isFilled = true;      
            else if (!field.equalsIgnoreCase("00"))
            lineHasError = true;
          }

      else if (field.equalsIgnoreCase("02"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("021") ||
           	field1.equalsIgnoreCase("023") ||
           	field1.equalsIgnoreCase("024") ||
           	field1.equalsIgnoreCase("026")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("03"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("037") ||
           	field1.equalsIgnoreCase("038") ||
           	field1.equalsIgnoreCase("039")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }
      
      else if (field.equalsIgnoreCase("04"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("041") ||
           	field1.equalsIgnoreCase("043") ||
           	field1.equalsIgnoreCase("044")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("05"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("051") ||
           	field1.equalsIgnoreCase("052") ||
           	field1.equalsIgnoreCase("054")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("06"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("061") ||
           	field1.equalsIgnoreCase("062") ||
           	field1.equalsIgnoreCase("064")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("07"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("071") ||
           	field1.equalsIgnoreCase("073")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("08"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("081") ||
           	field1.equalsIgnoreCase("082")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("09"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("091")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("10"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("101")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }
      
      else if (field.equalsIgnoreCase("11"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("111") ||
           	field1.equalsIgnoreCase("112")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }
      
      else if (field.equalsIgnoreCase("12"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("121") ||
           	field1.equalsIgnoreCase("122") ||
           	field1.equalsIgnoreCase("123") ||
           	field1.equalsIgnoreCase("124") ||
           	field1.equalsIgnoreCase("125") ||
           	field1.equalsIgnoreCase("126")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("14"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("141") ||
           	field1.equalsIgnoreCase("142")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("15"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("151") ||
           	field1.equalsIgnoreCase("152") ||
           	field1.equalsIgnoreCase("153")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }
      
      else if (field.equalsIgnoreCase("16"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("162")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("17"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("171") ||
           	field1.equalsIgnoreCase("172")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("18"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("181") ||
           	field1.equalsIgnoreCase("182") ||
           	field1.equalsIgnoreCase("183") ||
           	field1.equalsIgnoreCase("184") ||
           	field1.equalsIgnoreCase("185")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("19"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("191") ||
           	field1.equalsIgnoreCase("192") ||
           	field1.equalsIgnoreCase("193")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }

      else if (field.equalsIgnoreCase("20"))
      {
      	  field1 = kontorNumber;
          field1 = field1.replace(' ','0');
          if (
           	field1.equalsIgnoreCase("201") ||
           	field1.equalsIgnoreCase("202") ||
           	field1.equalsIgnoreCase("203") ||
           	field1.equalsIgnoreCase("204")
             )
           isFilled = true;      
           else if (!field.equalsIgnoreCase("00"))
           lineHasError = true;
      }
      
      
          if (lineHasError || !isFilled)
          {
        	lineNumbers.add (new Integer (lineNumber));
          }
          return lineHasError || !isFilled;
    }
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf + lf;
    if (foundError())
    {
      int numOfRecords = lineNumbers.size();
      errorReport += "\tFeil: i " + numOfRecords + 
      " record" + (numOfRecords == 1 ? "" : "s") + 
      " fylkesnummer og kontornummer stemmer ikke overens."; 
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
