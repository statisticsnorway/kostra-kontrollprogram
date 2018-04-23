package no.ssb.helseforetak.control.regnskap;



//import java.io.*;
//import no.ssb.kostra.control.ProgramExitMarker;

public class ControlProgram 
{
/*    
  private String regionNumber;
  private String sourceFileName;
  private String reportFileName;
  private String regnskapType;

  private final String lf = System.getProperty("line.separator");

  public ControlProgram (String[] args)
  {
    try
    {
      regionNumber = args[0];
      sourceFileName = args[1];
      reportFileName = args[2];
      regnskapType = args[3];
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println
      ("Usage: ControlProgram <region no> <source file> <report file> <regnskap type>");
      System.exit (Constants.PARAMETER_ERROR);
    }
  }

  public void start()
  {
    checkReportFileIsWritable();

    if (regnskapType.equalsIgnoreCase("0X"))
    {
      no.ssb.helseforetak.control.regnskap.regn0X.Main cp = 
      new no.ssb.helseforetak.control.regnskap.regn0X.Main 
          (regionNumber, sourceFileName, reportFileName);
      cp.start();
    }
    else if (regnskapType.equalsIgnoreCase("0Y"))
    {
      no.ssb.helseforetak.control.regnskap.regn0Y.Main cp = 
      new no.ssb.helseforetak.control.regnskap.regn0Y.Main 
          (regionNumber, sourceFileName, reportFileName);
      cp.start();
    }
    else
    {
      String errorMessage = 
      "**********************************************************" + lf + lf +
      "Kontrollprogrammet er ikke i stand til å bestemme regnskapstype." + lf +
      "Intern feil. Kontakt SSB." + lf + lf +
      "**********************************************************" + lf;
      writeErrorToReportFile (errorMessage);
    }
    ProgramExitMarker.markProgramExit(reportFileName, "Normal exit");
  }

  private void checkReportFileIsWritable()
  {
    try
    {
      File reportFile = new File (reportFileName);
      BufferedWriter fileWriter = 
          new BufferedWriter (new FileWriter (reportFile));
      fileWriter.write("");
      fileWriter.close();
    }
    catch (Exception e)
    {
      String message = "Can't write report file!";
      System.out.println(message);
      System.out.println (e.toString());
      // Prover aa skrive exit-fil likevel...
      ProgramExitMarker.markProgramExit(reportFileName, message);      
      System.exit (Constants.IO_ERROR);
    }    
  }

  private void writeErrorToReportFile (String errorMessage)
  {
    try
    {
      File reportFile = new File (reportFileName);
      BufferedWriter fileWriter = 
          new BufferedWriter (new FileWriter (reportFile));
      fileWriter.write(errorMessage);
      fileWriter.close();
    }
    catch (Exception e)
    {
      System.out.println (e.toString());
      System.exit (Constants.IO_ERROR);
    }    
  }
  public static void main(String[] args)
  {
    ControlProgram controlProgram = new ControlProgram (args);
    controlProgram.start();    
  }
 */ 
}