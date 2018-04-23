package no.ssb.kostra.control.sensitiv;

//import java.io.*;
//import no.ssb.kostra.control.ProgramExitMarker;

public class ControlProgram 
{
  /*
  private String regionNumber;
  private String kontorNumber;
  private String sourceFileName;
  private String reportFileName;
  private int sourceFileType;  //Type filuttrekk.

  public static final int SOSIALHJELP = 0;
  public static final int INTRODUKSJONSSTONAD = 1;
  public static final int BARNEVERN = 2;
  public static final int FAMILIEVERN = 3;
  
  public ControlProgram (String[] args)
  {
    try
    {
      regionNumber = args[0];
      sourceFileName = args[1];
      reportFileName = args[2];
      sourceFileType = (new Integer (args[3])).intValue();
      if (args.length == 5)
      {
        kontorNumber = args[4];
      }
      else
      {
        kontorNumber = "???";
      }
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println
      ("Usage: ControlProgram <region no> <source file> <report file> <filuttrekk type> [kontor no]");
      System.exit (Constants.PARAMETER_ERROR);
    }
    catch (NumberFormatException e)
    {
      System.out.println
      ("Usage: ControlProgram <region no> <source file> <report file> <filuttrekk type> [kontor no]");
      System.out.println
      ("<filuttrekk type> in (0, 1, 2, 3)");
      System.exit (Constants.PARAMETER_ERROR);
    }
  }

  public void start()
  {
    checkReportFileIsWritable();
    checkSourceFileIsReadable();

    switch (sourceFileType)
    {
      case SOSIALHJELP:
        no.ssb.kostra.control.sensitiv.soshjelp.Main cp_sos = 
            new no.ssb.kostra.control.sensitiv.soshjelp.Main
                (regionNumber, new File (sourceFileName), new File(reportFileName));
        cp_sos.start();
        break;
      case INTRODUKSJONSSTONAD:
        no.ssb.kostra.control.sensitiv.sosintro.Main cp_intro = 
            new no.ssb.kostra.control.sensitiv.sosintro.Main
                (regionNumber, sourceFileName, reportFileName);
        cp_intro.start();
        break;
      case BARNEVERN:
        no.ssb.kostra.control.sensitiv.barnevern.Main cp_bv = 
            new no.ssb.kostra.control.sensitiv.barnevern.Main
                (regionNumber, sourceFileName, reportFileName);
        cp_bv.start();
        break;
      case FAMILIEVERN:
        no.ssb.kostra.control.sensitiv.famvern.Main cp_fv = 
            new no.ssb.kostra.control.sensitiv.famvern.Main
                (regionNumber, kontorNumber, sourceFileName, reportFileName);
        cp_fv.start();
        break;
      default:
        String message = "Unknown type filuttrekk!";
        System.out.println (message);
        ProgramExitMarker.markProgramExit(reportFileName, message);      
        System.exit (Constants.PARAMETER_ERROR);
        break;
    }
    ProgramExitMarker.markProgramExit(reportFileName, "Normal exit.");
  }

  private void checkSourceFileIsReadable()
  {
    try
    {
      BufferedReader fileReader 
          = new BufferedReader (new FileReader (sourceFileName));
      String line = fileReader.readLine();
      fileReader.close();
    }
    catch (Exception e)
    {
      System.out.println (e.toString());
      ProgramExitMarker.markProgramExit(reportFileName, "Can't read source file");      
      System.exit(Constants.IO_ERROR);
    }
  
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
      System.exit(Constants.IO_ERROR);
    }    
  }
  
  public static void main(String[] args)
  {
    ControlProgram controlProgram = new ControlProgram (args);
    controlProgram.start();    
  }
   */
}
