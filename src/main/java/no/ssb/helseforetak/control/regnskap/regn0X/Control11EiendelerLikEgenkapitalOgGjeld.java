package no.ssb.helseforetak.control.regnskap.regn0X;

/**
 * $Log: Control11EiendelerLikEgenkapitalOgGjeld.java,v $
 * Revision 1.3  2007/12/07 12:49:20  pll
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
 * Revision 1.2  2005/12/14 13:04:20  lwe
 * updated all 2004 to 2005 (and added logmessage in javadoc)
 *
 */


import no.ssb.kostra.control.Constants;

final class Control11EiendelerLikEgenkapitalOgGjeld extends no.ssb.kostra.control.Control
{
  private final String ERROR_TEXT = "Kontroll 11, Eiendeler = egenkapital + gjeld:";
  private final int MAX_DIFF = 10;

  private int sumA = 0;
  private int sumB = 0;
  
  private String[] sumAKontokoder = 
  {
    "100", "102", "103", "104", "105", "106", "110", "112", "113", "114", "115",
    "116", "119", "120", "121", "123", "124", "125", "126", "127", "128", "129",
    "131", "132", "135", "136", "139", "140", "141", "142", "143", "150", "153",
    "155", "156", "157", "158", "167", "170", "171", "175", "176", "181", "182",
    "183", "184", "185", "186", "187", "188", "190", "192", "195"
  };

  private String[] sumBKontokoder = 
  {
    "200", "205", "208", "210", "216", "218", "220", "221", "222", "224", "226",
    "227", "228", "229", "230", "232", "234", "236", "238", "240", "246", "260",
    "261", "262", "263", "264", "265", "269", "270", "271", "272", "273", "274",
    "277", "278", "279", "290", "291", "292", "293", "294", "295", "296", "297",
    "298", "299"
  };

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {
    boolean lineContributedToSum = false;

    String kontoKode = RecordFields.getKontokode(line);
    int belop; 

    if (doSumA(kontoKode))
    {
      try
      {
        belop = RecordFields.getBelopIntValue(line);
        sumA = sumA + belop;
        lineContributedToSum = true;
      }
      catch (Exception e)
      {
      }
    }

    if (doSumB(kontoKode))
    {
      try
      {
        belop = RecordFields.getBelopIntValue(line);
        sumB = sumB + belop;
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
      errorReport += "\tBalansen skal balansere ved at sum eiendeler = " +
      "sum egenkapital + sum gjeld." + lf + "\tDifferanser på +/- 10 000 kroner godtas." + lf;
    }
    errorReport += lf + lf;
    return errorReport;
  }

  public boolean foundError()
  {
    return Math.abs (sumA + sumB) > MAX_DIFF;
  }

  private boolean doSumA (String kontokode)
  {
    for (int i=sumAKontokoder.length-1; i>=0; i--)
    {
      if (kontokode.equalsIgnoreCase(sumAKontokoder[i]))
      {
        return true;
      }
    }
    return false;
  }

  private boolean doSumB (String kontokode)
  {
    for (int i=sumBKontokoder.length-1; i>=0; i--)
    {
      if (kontokode.equalsIgnoreCase(sumBKontokoder[i]))
      {
        return true;
      }
    }
    return false;
  }

  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}