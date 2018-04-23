package no.ssb.helseforetak.control.regnskap.regn0Y;

/**
 * $Log: Control11EiendelerLikEgenkapitalOgGjeld.java,v $
 * Revision 1.7  2010/10/21 07:56:10  pll
 * Klar for 2010-rapp.
 *
 * Revision 1.6  2009/10/03 12:44:25  pll
 * Versjon: 2009-rapporteringen
 *
 * Revision 1.5  2009/04/06 11:49:40  pll
 * String -> StringBuilder
 *
 * Revision 1.4  2007/12/07 12:52:08  pll
 * Fanger opp Exception isteden for NumberFormatException.
 *
 * Revision 1.3  2007/11/12 13:40:49  pll
 * Endret for 2007-rapporteringen.
 *
 * Revision 1.2  2007/10/25 11:33:58  pll
 * Implementerer getErrorType.
 *
 * Revision 1.1  2007/10/23 10:49:23  pll
 * Import av versjon for 2006-rapporteringen.
 *
 * Revision 1.3  2006/12/06 11:50:16  pll
 * Konti:
 * lagt til: 107, 138,165, 223
 * tatt vekk: 236, 272, 273
 *
 * Revision 1.2  2006/11/27 10:16:56  pll
 * Oppdatert aarstall (2005 -> 2006)
 *
 * Revision 1.1  2006/11/27 09:18:08  pll
 * Import av 2005-filer
 *
 * Revision 1.4  2006/01/02 09:58:17  lwe
 * endret etter mail-forespørsel fra Siri Bogen: feilmeldinger i K0, K11, K12
 *
 * Revision 1.3  2005/12/22 13:24:34  lwe
 * etter kravspec: endret/sjekket summe-kontokoder (ut: ingen, inn sumA: 172, 177, inn sumB: 201,202, 280,281)
 *
 * Revision 1.2  2005/12/14 13:04:22  lwe
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
	"100","102","103","104","105","106","107","108","110","112","113","114","115","116","119","120","121","123","124","125",
	"126","127","128","129","131","132","135","136","138","139","140","141","142","143","150","153","155","156","157","158",
	"165","167","170","171","172","175","176","177","181","182","183","184","185","186","187","188","190","192","194","195"
  };

  private String[] sumBKontokoder = 
  {
	"200","202","205","209","210","216","218","220","221","222","223","224","226","227","228","229","230","232","234","238",
	"240","246","260","261","262","263","264","265","269","270","271","274","275","276","277","278","279","280","281","290",
	"291","292","293","294","295","296","297","298","299"
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
    StringBuilder errorReport = new StringBuilder (ERROR_TEXT + lf + lf);
    if (foundError())
    {
      errorReport.append("\tFeil: Balansen skal balansere ved at " + lf +
      "\tsum eiendeler = " +
      "sum egenkapital + sum gjeld." + lf + "\tDifferanser på +/- 10 000 kroner godtas." + lf +
      "\tsum eiendeler: " + sumA + "', sum egenkapital + sum gjeld: " + sumB + "', differanse: " + (sumA + sumB) + "'" + lf);
      
    }
    errorReport.append(lf + lf);
    return errorReport.toString();
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