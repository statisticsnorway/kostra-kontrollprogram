package no.ssb.kostra.utils;

/**
    @hack TAYdersbond 2000. 
    @author TAYdersbond In part adapted from Hetland/Thirud ssb
                         
*/



public final class DatoFnr
{
  static final int[] ANT_DAGER = {0,31,28,31,30,31,30,31,31,30,31,30,31} ;
  static final int DATOFEIL = -10000000 ;
  static int day,month,year ;


  static void parseDate(int yfmt, String dt)
  { 
    day = Format.parseInt(dt.substring(0,2)) ;  
    
    /* Hvis man bruker D-nummer legger man til 4 på første siffer. */ 
    /* Når dag er større enn 31 benyttes D-nummer, trekk fra 40 og få gyldig dag  */
    if (day > 31){
    	day = day - 40;    	
    }
    
    month = Format.parseInt(dt.substring(2,4)) ;
    switch (yfmt) {  
      case 6 : 
	  year = Format.parseInt(dt.substring(4,6)) ;
	  break ;
      case 8:
	  year = Format.parseInt(dt.substring(4,8)) ;
	  break ;
      default: 
	  year = 0 ;
    }
  }


// diffdager


//int diffdager(String d1, String d2)
//{
//  return(daysBetweenDMY(d1,d2)) ;
//}



// This method attempts to work whether dates are given DDMMYY (in which case YY<50 => 21.century or DDMMYYYY
// With DDMMYY, it works only for years 1910-2010
// 

//  public static int daysBetweenDMY(String dt1, String dt2)
//  {   int t1, t2, td ;
//
//      if ((dt1.length()==6)&&(dt2.length()==6)) {
//	if (!(validDateDDMMYY(dt1)==1)&&(validDateDDMMYY(dt2)==1)) return(DATOFEIL) ;
//	  parseDate(6,dt1) ;
//          if (year>10) year += 1900 ; else year += 2000 ;
//          t1 = toJulian() ;
//          parseDate(6,dt2) ;
//          if (year>10) year += 1900 ; else year += 2000 ;
//          t2 = toJulian() ;
//      }
//      else if ((dt1.length()==8)&&(dt2.length()==8)) {
//	if (!(validDateDDMMYYYY(dt1)==1)&&(validDateDDMMYYYY(dt2)==1)) return(DATOFEIL) ;
//	  parseDate(8,dt1) ;
//	  t1 = toJulian() ;
//          parseDate(8,dt2) ;
//	  t2 = toJulian() ;
//
//      }
//      else return(DATOFEIL) ;
//      td = t2 - t1 ;
//      return ( td) ;
//  }


//   private static int toJulian()
//   /**
//    * @return The Julian day number that begins at noon of
//    * this day
//    * Positive year signifies A.D., negative year B.C.
//    * Remember that the year after 1 B.C. was 1 A.D.
//    *
//    * A convenient reference point is that May 23, 1968 noon
//    * is Julian day 2440000.
//    *
//    * Julian day 0 is a Monday.
//    *
//    * This algorithm is from Press et al., Numerical Recipes
//    * in C, 2nd ed., Cambridge University Press 1992
//    */
//   {  int jy = year;
//      if (year < 0) jy++;
//      int jm = month;
//      if (month > 2) jm++;
//      else
//      {  jy--;
//         jm += 13;
//      }
//      int jul = (int) (java.lang.Math.floor(365.25 * jy)
//      + java.lang.Math.floor(30.6001*jm) + day + 1720995.0);
//
//      int IGREG = 15 + 31*(10+12*1582);
//         // Gregorian Calendar adopted Oct. 15, 1582
//
//      if (day + 31 * (month + 12 * year) >= IGREG)
//         // change over to Gregorian calendar
//      {  int ja = (int)(0.01 * jy);
//         jul += 2 - ja + (int)(0.25 * ja);
//      }
//      return jul;
//   }

   @SuppressWarnings("unused")
private static void fromJulian(int j)
   /**
    * Converts a Julian day to a calendar date
    * @param j  the Julian date
    * This algorithm is from Press et al., Numerical Recipes
    * in C, 2nd ed., Cambridge University Press 1992
    */
   {  int ja = j;
   
      int JGREG = 2299161;
         /* the Julian date of the adoption of the Gregorian
            calendar    
         */   

      if (j >= JGREG)
      /* cross-over to Gregorian Calendar produces this 
         correction
      */   
      {  int jalpha = (int)(((float)(j - 1867216) - 0.25) 
             / 36524.25);
         ja += 1 + jalpha - (int)(0.25 * jalpha);
      }
      int jb = ja + 1524;
      int jc = (int)(6680.0 + ((float)(jb-2439870) - 122.1)
          /365.25);
      int jd = (int)(365 * jc + (0.25 * jc));
      int je = (int)((jb - jd)/30.6001);
      day = jb - jd - (int)(30.6001 * je);
      month = je - 1;
      if (month > 12) month -= 12;
      year = jc - 4715;
      if (month > 2) --year;
      if (year <= 0) --year;
   }


  public static int findYearYYYY(int year) {
    if(year <= 50) {
      return (2000 + year);
    } else {
      return (1900 + year);
    }
  }

  
  public static int validDateDDMMYY(String dt)
  { 
    try {
      Integer.parseInt(dt);
    } catch (Exception e) {
      return 0;
    }    
    if (dt.length()<6) return(0) ;
    parseDate(6,dt) ;   
    if ((month<1)||(month>12)) return(0) ;
    // Leap year - NB gives wrong result for 29.02.1900 !!! Correct for 2000
    if (((day>0)&&(day<=ANT_DAGER[month]))||((month==2)&&(day==29)&&(year%4==0))) return(1) ;
    else return(0) ;                                                                  
 
 
  }
 
  
  public static int validDateDDMMYYYY(String dt)
  {
    try {
      Integer.parseInt(dt);
    } catch (Exception e) {
      return 0;
    }
    boolean isLeap=false ;
    if (dt.length()<8) return(0) ;
    parseDate(8,dt) ;   
    // Check leap year 
    if (((year%4==0)&&(year%100!=0)) ||
        ((year%4==0)&&(year%100==0)&&(year%400==0))) isLeap = true ;  
   
    if ((month<1)||(month>12)) return(0) ;
   
    if (((day>0)&&(day<=ANT_DAGER[month]))||((month==2)&&(day==29)&&isLeap)) return(1) ;
    else return(0) ;                                                                  
 
 
  }
 
 
  public static int validNorwId(String fnr)
  { int[] s = new int[12] ; int k1, k2, rest ;
  
    if (fnr.length()<11) return(0) ;
    String f_dt = fnr.substring(0,6) ; 
    
    if (validDateDDMMYY(f_dt)==0) return(0) ;
  
    for (int i=1;i<=11;i++) s[i]=Format.parseInt(fnr.substring(i-1,i)) ;
    
    k1 = (s[1]*3) +  (s[2]*7) +  (s[3]*6) +  s[4] + (s[5]*8) +
         (s[6]*9) +  (s[7]*4) +  (s[8]*5) +  (s[9]*2) ;
         
    rest = k1 % 11 ;
    if (rest==1) return(0) ;
    if (rest==0) k1 = 0 ;
    else k1 = 11-rest ;
    
     k2 = (s[1]*5) +  (s[2]*4) +  (s[3]*3) +  (s[4]*2) + (s[5]*7) +
         (s[6]*6) +  (s[7]*5) +  (s[8]*4) +  (s[9]*3) + (k1*2) ;
         
    rest = k2 % 11 ;
    if (rest==1) return(0) ;
    if (rest==0) k2 = 0 ;
    else k2 = 11-rest ;
    
    if ((k1!=s[10])||(k2!=s[11])) return(0) ;
    return(1) ;        
 
    
  }

 

}






