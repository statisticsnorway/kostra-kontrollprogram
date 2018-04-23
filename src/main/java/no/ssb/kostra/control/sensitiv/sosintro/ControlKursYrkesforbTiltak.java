package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: ControlKursYrkesforbTiltak.java,v $
 * Revision 1.5  2009/10/13 10:08:55  pll
 * Versjon: 2009-rapporteringen.
 *
 * Revision 1.4  2008/12/17 13:29:44  pll
 * Tilbakerulling til 2007-versjon.
 *
 * Revision 1.3  2008/10/04 10:46:07  pll
 * Versjon: 2008-rapportering.
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
 * Revision 1.1  2006/09/22 08:18:31  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.4  2006/01/05 08:31:54  lwe
 * replaced _final class_ with _public final class_
 *
 * Revision 1.3  2006/01/05 08:16:37  lwe
 * added logmessage
 * 
 */
 
import java.util.Vector;
import no.ssb.kostra.control.Constants;

public final class ControlKursYrkesforbTiltak extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport
{
  private final String ERROR_TEXT = "K11: Kurs og yrkesforberedende tiltak";
  private Vector<Integer> linesWithError = new Vector<Integer>();

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean oneFieldFilled = false;
    String field;

  for (int i=1; i<=4; i++)
  {
    field = RecordFields.getFieldValue(line, (120+i));
    field = field.replace(' ','0');

    if (field.equalsIgnoreCase("0" + Integer.toString(i)))
    {
      oneFieldFilled = true;
    }
    else if (! field.equalsIgnoreCase("00"))
    {
        linesWithError.add(new Integer(lineNumber));
        return true;
    }
   }

    field = RecordFields.getFieldValue(line, 125);
    field = field.replace(' ','0');

    if (field.equalsIgnoreCase("00"))
    {
      oneFieldFilled = true;
    }
    else if (! field.equalsIgnoreCase("00"))
    {
      linesWithError.add(new Integer(lineNumber));
      return true;
    }

    for (int i=6; i<=8; i++)
    {
      field = RecordFields.getFieldValue(line, (120+i));
      field = field.replace(' ','0');

      if (field.equalsIgnoreCase("0" + Integer.toString(i)))
      {
        oneFieldFilled = true;
      }
      else if (! field.equalsIgnoreCase("00"))
      {
          linesWithError.add(new Integer(lineNumber));
          return true;
      }
    }      

    field = RecordFields.getFieldValue(line, 129);
    field = field.replace(' ','0');

    if (field.equalsIgnoreCase("10"))
    {
      oneFieldFilled = true;
    }
    else if (! field.equalsIgnoreCase("00"))
    {
      linesWithError.add(new Integer(lineNumber));
      return true;
    }

    field = RecordFields.getFieldValue(line, 1210);
    field = field.replace(' ','0');

    if (field.equalsIgnoreCase("11"))
    {
      oneFieldFilled = true;
    }
    else if (! field.equalsIgnoreCase("00"))
    {
      linesWithError.add(new Integer(lineNumber));
      return true;
    }
    
    field = RecordFields.getFieldValue(line, 1211);
    field = field.replace(' ','0');

    if (field.equalsIgnoreCase("99"))
    {
      oneFieldFilled = true;
    }
    else if (! field.equalsIgnoreCase("00"))
    {
      linesWithError.add(new Integer(lineNumber));
      return true;
    }

    //Sjekk om feltene 121-1211 inneholder bare 0000000000000000000000 
    field = line.substring(50, 72);
    field = field.replace(' ','0');
	if (field.equalsIgnoreCase("0000000000000000000000"))
	{
	  linesWithError.add(new Integer(lineNumber));
	  return true;
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
      errorReport += lf + "\tFeil: i " + numOfRecords + 
          " record" + (numOfRecords == 1 ? "" : "s") + 
          " er det ikke krysset av for hvilke kurs og" + lf + "\tyrkesforberedende " +
          "tiltak mottakeren har deltatt på i løpet" + lf + "\tav rapporteringsåret, " +
          "eller feil kode er benyttet." + lf + "\tFeltet er obligatorisk å fylle ut."; 
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
