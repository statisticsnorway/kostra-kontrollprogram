package no.ssb.kostra.control.sensitiv.soskval;

/*
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlBarnUnder18Antall extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = 
          "K11: Det bor barn under 18 år i husholdningen. Mangler antall barn";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    String barnUnder18 = RecordFields.getFieldValue(line, 91);
    
    int antallBarnUnder18;
    try {
        antallBarnUnder18 = Integer.parseInt(RecordFields.getFieldValue(line, 92));
    } catch (NumberFormatException e) {
        antallBarnUnder18 = 0;
    }
    
    boolean lineHasError = barnUnder18.equalsIgnoreCase("1") && antallBarnUnder18 == 0;

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
          " record" + (numOfRecords == 1 ? "" : "s") + " er krysset av " +
              "for at Deltakeren har barn under 18 år, som deltakeren " + lf +
              "\t(eventuelt ektefelle/samboer) har forsørgerplikt for, " +
              "og som bor i husholdningen" + lf + "\tved siste kontakt, men det er " +
              "ikke oppgitt hvor mange barn som bor i husholdningen. " + lf +
              "\tFeltet er obligatorisk å fylle ut."; 
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