package no.ssb.kostra.control.sensitiv.soshjelp;

/*
 * $Log: ControlStonadssum.java,v $
 * Revision 1.4  2009/09/21 12:03:51  pll
 * Versjon: 2009-rapporteringen ("refaktorert").
 *
 * Revision 1.3  2007/10/25 09:11:53  pll
 * Implementerer getErrorType.
 *
 * Revision 1.2  2007/09/25 07:45:29  pll
 * Versjon: 2007-rapporteringen.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:50  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:29  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.3  2006/01/05 08:31:53  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.2  2006/01/05 08:16:35  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlStonadssum extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K27: Stønadssum mangler eller har ugyldige tegn";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    int field_15_1, field_15_2;

    //Felt 15.1
    try  {      
      field_15_1 = Integer.parseInt(RecordFields.getFieldValue(line, 151));
    } catch (NumberFormatException e) {
      field_15_1 = 0;
    }      

    //Felt 15.2
    try  {      
      field_15_2 = Integer.parseInt(RecordFields.getFieldValue(line, 152));
    } catch (NumberFormatException e) {
      field_15_2 = 0;
    }      
    
    boolean lineHasError = (field_15_1 + field_15_2)<=0;

    if (lineHasError)
      linesWithError.add(new Integer(lineNumber));

    return lineHasError;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + ":" + lf;
    int numOfRecords = linesWithError.size();
    if (numOfRecords > 0)
    {      
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er det ikke oppgitt hvor mye mottakeren" + lf + "\thar fått i økonomisk " +
          "sosialhjelp (bidrag eller lån) i løpet" + lf + "\tav året, eller feltet " +
          "inneholder andre tegn enn tall." + lf + "\tFeltet er obligatorisk "; 
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