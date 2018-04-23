package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlMangeBarnUnder18Husholdningen 
    extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K9: Det bor barn under 18 år i husholdningen";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineHasError = false;

    String barnUnder18 = RecordFields.getFieldValue(line, 91);

    if (barnUnder18.equalsIgnoreCase("1"))
    {
      String field_9_2 = RecordFields.getFieldValue(line, 92);
      try
      {
        int antallBarn = Integer.parseInt(field_9_2);
        if (antallBarn <= 0)
        {
          lineHasError = true;
        }
      }
      catch (NumberFormatException e)
      {
        lineHasError = true;
      }
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
          " record" + (numOfRecords == 1 ? "" : "s") + " " +
      "er det krysset av for at det bor barn under 18 år" + lf + 
      "\ti husholdningen som mottaker eller ektefelle/samboer har forsørgerplikt for, " + 
      lf + "\tmen det er ikke oppgitt hvor mange barn som bor i husholdningen." + lf +
      "\tFeltet er obligatorisk når det er oppgitt at det bor " +
      "barn under 18 år i husholdningen."; 
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