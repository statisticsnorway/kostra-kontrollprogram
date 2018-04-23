package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: Control12SumInntekterOgKostnader.java,v $
 * Revision 1.3  2007/12/07 12:49:34  pll
 * Fanger opp Exception isteden for NumberFormatException.
 *
 * Revision 1.2  2007/10/25 11:33:57  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:10  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.2  2006/11/27 10:16:55  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:07  pll
 * Import av 2005-filer
 *
 * Revision 1.3  2006/01/02 09:58:17  lwe
 * endret etter mail-forespørsel fra Siri Bogen: feilmeldinger i K0, K11, K12
 *
 * Revision 1.2  2005/12/14 13:04:21  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import no.ssb.kostra.control.Constants;

final class Control12SumInntekterOgKostnader extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 12, Sum inntekter og kostnader = 0:";
  private final int MAX_DIFF = 30;

  private int controlSum = 0;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineContributedToSum = false;

    String kontoKode = RecordFields.getKontokode(line);
    int belop; 

    if (ControlKontokoder.validKontokode(kontoKode))
    {
      try
      {
        belop = RecordFields.getBelopIntValue(line);
        controlSum = controlSum + belop;
        lineContributedToSum = true;
      }
      catch (Exception e)
      {
      }
    }
    
    return lineContributedToSum;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = ERROR_TEXT + lf + lf;
    if (foundError())
    {
      errorReport += "\tSum inntekter og kostnader er forskjellig fra 0. "+
      "Differanser på opptil +/- 30 000 kroner godtas." + lf;
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (controlSum) >= MAX_DIFF;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}