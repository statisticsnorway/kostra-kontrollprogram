/*
 * FamilievernKontorNrChecker.java
 */

package no.ssb.kostra.utils;

/**
 * 
 */
public final class FamilievernKontorNrChecker {
    
  public static boolean hasCorrectKontorNr (String regionNr, String kontorNr) {
      
      boolean correctKontorNr;
      int kontorNrNumVal;
      
      try {
          kontorNrNumVal = Integer.parseInt(kontorNr);
      } catch (NumberFormatException e) {
          return false;
      }
      
  if (regionNr.equalsIgnoreCase("667600")) {
      
      correctKontorNr = kontorNrNumVal>10 && kontorNrNumVal<60;
      
  } else if (regionNr.equalsIgnoreCase("667500")) {
  
      correctKontorNr = kontorNrNumVal>60 && kontorNrNumVal<110;

  } else if (regionNr.equalsIgnoreCase("667400")) {
  
      correctKontorNr = kontorNrNumVal>110 && kontorNrNumVal<150;

  } else if (regionNr.equalsIgnoreCase("667300")) {
  
      correctKontorNr = kontorNrNumVal>150 && kontorNrNumVal<180;

  } else if (regionNr.equalsIgnoreCase("667200")) {
      
      correctKontorNr = kontorNrNumVal>180 && kontorNrNumVal<210;

  } else {
      // Vi har ugyldig regionnr., men det er ikke vårt ansvar å
      // å rapportere dette...
      correctKontorNr = true;
  }

      return correctKontorNr;
  }    
}
