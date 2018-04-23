package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlStonadsmaaneder.java,v $
 * Revision 1.3  2008/10/09 13:50:47  pll
 * Tekstendring.
 *
 * Revision 1.2  2007/10/25 11:37:12  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:51  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:32  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.4  2006/01/05 08:31:55  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.3  2006/01/05 08:16:38  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlStonadsmaaneder extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K14: Stønadsmåneder";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean oneFieldFilled = false;

    String field;
    //Felt 15.1 - 15.9:
    for (int i=1; i<10; i++)
    {
      field = RecordFields.getFieldValue(line, (1500+i));
      if (field.equalsIgnoreCase("0" + Integer.toString(i)))
      {
        oneFieldFilled = true;
      }
      else if (! (field.equalsIgnoreCase("00") ||
                        field.equalsIgnoreCase("  ")))
      {
        linesWithError.add(new Integer(lineNumber));
        return true;
      }
    }
    //Felt 15.10 - 15.12:
    for (int i=10; i<13; i++)
    {
      field = RecordFields.getFieldValue(line, (1500+i));
      if (field.equalsIgnoreCase(Integer.toString(i)))
      {
        oneFieldFilled = true;
      }
      else if (! (field.equalsIgnoreCase("00") ||
                        field.equalsIgnoreCase("  ")))
      {
        linesWithError.add(new Integer(lineNumber));
        return true;
      }
    }
    if (! oneFieldFilled)
    {
      linesWithError.add(new Integer(lineNumber));
    }
    return ! oneFieldFilled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: Det er ikke krysset av for hvilke måneder mottakeren har fått" + lf +
                              "\tutbetalt introduksjonsstønad. Feltet er obligatorisk å fylle ut.";
      
      if (numOfRecords <= 200)
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
