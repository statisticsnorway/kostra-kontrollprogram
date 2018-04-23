package no.ssb.kostra.control;

import java.io.*;
import java.util.Calendar;

public class ProgramExitMarker 
{
  private static final String EXIT_FILE_NAME = "controlprogram.log";

  public static void markProgramExit (String reportFileUri, String message)
  {
    try
    {
      String path = getPath (reportFileUri);
      File exitFile = new File (path, EXIT_FILE_NAME);
      BufferedWriter fileWriter = 
          new BufferedWriter (new FileWriter (exitFile));
      fileWriter.write (Calendar.getInstance().getTime() + ": " + message);
      fileWriter.close();
    }
    catch (Exception e)
    {
      System.out.println(e.toString());
      System.exit (Constants.IO_ERROR);
    }    
  }

  private static String getPath (String reportFileUri)
  {
    String separator = System.getProperty("file.separator");
    if (separator.equalsIgnoreCase("\\"))
    {
      reportFileUri = reportFileUri.replace('/', '\\'); // Just in case...
    }    
    int index = reportFileUri.lastIndexOf(separator);
    return reportFileUri.substring(0, index);
  }
}