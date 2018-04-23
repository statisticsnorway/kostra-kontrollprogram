package no.ssb.kostra.utils;

import java.util.StringTokenizer;

public class CompatJdk13 
{
  public static boolean isNumerical (String str)
  {
    int strLength = str.length();
    char[] ch = new char[strLength];
    str.getChars(0,strLength,ch,0);
    for (int i=strLength-1; i>=0; i--)
    {
      if (! Character.isDigit(ch[i]))
      {
        return false;
      }      
    }
    return true;    
  }

  public static boolean isNumericalWithSpace (String str)
  {
    int strLength = str.length();
    char[] ch = new char[strLength];
    str.getChars(0,strLength,ch,0);
    for (int i=strLength-1; i>=0; i--)
    {
      if (! (Character.isDigit(ch[i]) || ch[i]==' '))
      {
        return false;
      }      
    }
    return true;    
  }

  public static String removeSpace (String str)
  {
    String returnString = "";
    StringTokenizer st = 
      new StringTokenizer (str);
    while (st.hasMoreTokens())
    {
      returnString += st.nextToken();
    }
    return returnString;
  }
  
	public static boolean isFloat(String number, int decimals)
	{
		String pattern = String.format("^((\\+|\\-)?(0|([1-9][0-9]+))(\\.[0-9]{%1$d})?$)|(^(0{0,1}|([1-9][0-9]*))(\\.[0-9]{%1$d})?)$", decimals);
		String trimmedNumber = number.trim();
		String replacedTrimmedNumber = trimmedNumber.replace(",", ".");
		
		return (replacedTrimmedNumber.length() > 0) ? replacedTrimmedNumber.matches(pattern) : false;
	}

	public static boolean isInt(String number)
	{
		String pattern = String.format("^(\\+|\\-)?(0|([1-9][0-9]*))$");
		String trimmedNumber = number.trim();
		String replacedTrimmedNumber = trimmedNumber.replace(",", ".");
		
		return (replacedTrimmedNumber.length() > 0) ? replacedTrimmedNumber.matches(pattern) : false;
	}

}