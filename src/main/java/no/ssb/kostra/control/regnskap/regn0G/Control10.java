package no.ssb.kostra.control.regnskap.regn0G;

import no.ssb.kostra.control.Constants;

final class Control10 extends no.ssb.kostra.control.Control
{
//  private int sumForControl = 0;

    boolean hasBelopKap510 = false;
    boolean hasBelopKap513 = false;
    boolean hasBelopKap556 = false;
//    boolean hasBelopKap557 = false;
//    boolean hasBelopKap558 = false;
    boolean hasBelopKap55990 = false;
    
public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  { 
    String kapittel = RecordFields.getFunksjon(line); 
    @SuppressWarnings("unused")
     boolean lineControlled = false;
    
    /*
    System.out.print("kap: " +kapittel);
    System.out.println("   "+ RecordFields.getBelop(line));
    */
    
    if (kapittel.equalsIgnoreCase("10")) { //Tar bort ledende 5
      lineControlled = true;
      try {
        hasBelopKap510 = RecordFields.getBelopIntValue(line) != 0;
        hasBelopKap510 = true;
      } catch (Exception e) {
        hasBelopKap510 = false;
      }
    }
      
    if (kapittel.equalsIgnoreCase("13")) { //Tar bort ledende 5
      lineControlled = true;
      try {
        hasBelopKap513 = RecordFields.getBelopIntValue(line) != 0;
        hasBelopKap513 = true;
      } catch (Exception e) {
        hasBelopKap513 = false;
      }
    }
    
    if (kapittel.equalsIgnoreCase("56")) { //Tar bort ledende 5
      lineControlled = true;
      try {
        hasBelopKap556 = RecordFields.getBelopIntValue(line) != 0;
        hasBelopKap556 = true;
      } catch (Exception e) {
        hasBelopKap556 = false;
      }
    }
/*    
    if (kapittel.equalsIgnoreCase("57")) { //Tar bort ledende 5
      lineControlled = true;
      try {
        hasBelopKap557 = RecordFields.getBelopIntValue(line) != 0;
        hasBelopKap557 = true;
      } catch (Exception e) {
        hasBelopKap557 = false;
      }
    }
    
    if (kapittel.equalsIgnoreCase("58")) { //Tar bort ledende 5
      lineControlled = true;
      try {
        hasBelopKap558 = RecordFields.getBelopIntValue(line) != 0;
        hasBelopKap558 = true;
      } catch (Exception e) {
        hasBelopKap558 = false;
      }
    }
*/    
    if (kapittel.equalsIgnoreCase("5990")) { //Tar bort ledende 5
      lineControlled = true;
      try {
        hasBelopKap55990 = RecordFields.getBelopIntValue(line) != 0;
        hasBelopKap55990 = true;
      } catch (Exception e) {
        hasBelopKap55990 = false;
      }
    }
    
    return !(hasBelopKap510 & hasBelopKap513 & hasBelopKap556 /*& hasBelopKap557 & hasBelopKap558*/ & hasBelopKap55990);
  }

  public String getErrorReport (int totalLineNumber)
  {
    StringBuilder errorReport = new StringBuilder ("Kontroll 10, beløp mangler for:" + lf);
    if (!hasBelopKap510) errorReport.append("\tkapittel 510 Kasse, postgiro, bankinnskudd" + lf);
    if (!hasBelopKap513) errorReport.append("\tkapittel 513 Kortsiktig fordringer" + lf);
    if (!hasBelopKap556) errorReport.append("\tkapittel 556 Disposisjonsfond" + lf);/*
    if (!hasBelopKap557) errorReport.append("\tkapittel 557" + lf);
    if (!hasBelopKap558) errorReport.append("\tkapittel 558" + lf);*/
    if (!hasBelopKap55990) errorReport.append("\tkapittel 55990 Kapitalkonto" + lf);
      
    return errorReport.toString();
  }

  public boolean foundError()
  {
    return !(hasBelopKap510 & hasBelopKap513 & hasBelopKap556 /*& hasBelopKap557 & hasBelopKap558*/ & hasBelopKap55990);
  }


  public int getErrorType() {
	  return Constants.NORMAL_ERROR;
  }
}
