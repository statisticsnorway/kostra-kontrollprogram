package no.ssb.kostra.control.regnskap.regn0F;

import no.ssb.kostra.control.Constants;

final class ControlSum600_990 extends no.ssb.kostra.control.Control
{
  private int sumForControl = 0;

  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet)
  {    
    boolean lineControlled = false;
    int belop = 0;
   
    try {
      int art = RecordFields.getArtIntValue(line);
      if (art>=600 && art<=990) {
        belop = RecordFields.getBelopIntValue(line);
        sumForControl += belop;
        lineControlled = true;
      }
    } catch (Exception e) {}
    
    return lineControlled;
  }

  public String getErrorReport (int totalLineNumber)
  {
    String errorReport = "Kontroll 17, summeringskontroll for artene 600-990:" +
    lf + lf + "\tFeil: Inntekter mangler." + lf; 
    return errorReport;
  }

  public boolean foundError()
  {
    return sumForControl>=0;
  }


  public int getErrorType() {
    return Constants.CRITICAL_ERROR;
  }
}
