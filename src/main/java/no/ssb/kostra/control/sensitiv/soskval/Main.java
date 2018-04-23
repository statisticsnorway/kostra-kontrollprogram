package no.ssb.kostra.control.sensitiv.soskval;

/*
 *
 */
 
import java.io.*;
import java.util.*;
import no.ssb.kostra.control.*;
import no.ssb.kostra.utils.Regioner;

public final class Main 
{
  private String regionNumber;
  private File sourceFile;
  private File reportFile;
  private Control[] controls;
  private final String lf = Constants.lineSeparator;
  private final String VERSION = "11C." + Constants.kostraYear + ".01";
  
  private Vector<String[]> errorLines = new Vector<String[]>();

  public Main (String regionNumber, File sourceFile, File reportFile)
  {    
    this.regionNumber = regionNumber;
    this.sourceFile = sourceFile;
    this.reportFile = reportFile; 
    initControls();
  }

  
  public int start() throws Exception
  {
    int lineNumber = 0;
    int error_type = Constants.NO_ERROR;
    
    try
    {
      BufferedReader reader;
      if (sourceFile == null)
        reader = new BufferedReader(new InputStreamReader(System.in));
      else
        reader = new BufferedReader (new FileReader(sourceFile));
      
      String line;
      String[] container;
      boolean printNewline;      
      while ((line = reader.readLine()) != null)
      {
        if (! line.equalsIgnoreCase(""))
        {
          lineNumber += 1;
          //Sjekker recordlengde forst, fordi feil reccordlengde 
          //vil kunne odelegge mange andre kontroller 
          //(StringIndexOutOfBoundsException etc.)
          if (! controls[0].doControl(line, lineNumber, regionNumber, ""))
          {
            printNewline = false;
            for (int i=1; i<controls.length; i++)
            {
              if (controls[i].doControl(line, lineNumber, regionNumber, ""))
              {
                container = new String[3];
                container[0] = " " + lineNumber + "  ";
                container[1] = line.substring(18, 29) + "  ";
                container[2] = ((SingleRecordErrorReport) controls[i]).getErrorText() +
                    (controls[i].getErrorType()==Constants.CRITICAL_ERROR?Constants.CRITICAL_ERROR_SHORT_TEXT_MSG:"");
                errorLines.add(container);
                printNewline = true;
              }
            }

            if (printNewline)
            {
              container = new String[3];
              container[0] = "";
              container[1] = "";
              container[2] = "";
              errorLines.add(container);
            }
          }
          else
          {
            container = new String[3];
            container[0] = " " + lineNumber + " ";
            container[1] = " xxxxxxxxx ";
            container[2] = ((SingleRecordErrorReport) controls[0]).getErrorText() + (controls[0].getErrorType()==Constants.CRITICAL_ERROR?Constants.CRITICAL_ERROR_SHORT_TEXT_MSG:"") + lf;
            errorLines.add(container);
          }
        }
      }
      reader.close();
    }
    catch (IOException e)
    {
      throw e;
    }
    catch (Exception e) 
    {
      throw new UnreadableDataException();
    }

    try
    {
      BufferedWriter writer = null;
      if (reportFile != null)
        writer = new BufferedWriter (new FileWriter (reportFile));

      StringBuffer report = new StringBuffer();
             report.append("-------------------------------------------------" + lf);    
             report.append(lf);
             report.append(" Feilmeldingsrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + lf);
             report.append(lf);
             report.append("-------------------------------------------------" + lf + lf);

      report.append("Kontrollprogramversjon: " + VERSION + lf);
      report.append("Rapport generert: " + Calendar.getInstance().getTime() + lf);
      report.append("Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
      report.append("Type filuttrekk: Kvalifiseringsstønad " + Constants.kostraYear + lf + lf);

      int numOfErrors = 0;
      int control_error_type;
      StringBuffer buffer = new StringBuffer();
      
      for (int i=0; i<controls.length; i++)
      {
        if (controls[i].foundError())
        {
          control_error_type = controls[i].getErrorType();
          if (control_error_type > error_type)
            error_type = control_error_type;
          
          if (control_error_type == Constants.CRITICAL_ERROR)
            buffer.append(Constants.CRITICAL_ERROR_TEXT_MSG + lf);
          
          buffer.append(controls[i].getErrorReport (lineNumber));
          numOfErrors += 1;
        }
      }

      if (numOfErrors > 0)
      {
        int errorLinesSize = errorLines.size();
        if (errorLinesSize > 0)
        {
          String[] container;

          report.append(lf + "Opplisting av feil pr. record (recordnr., fødselsnr., kontrolltekst):" + lf +
                         "---------------------------------------------------------------------" + lf + lf);
          
          for (int i=0; i<errorLinesSize; i++)
          {
            container = (String[]) errorLines.elementAt(i);
            report.append(container[0] + container[1] + container[2] + lf); 
          }
          
        }
        report.append(lf + "Oppsummering pr. kontroll:" + lf +
        "-------------------------------------------------" + lf + lf);
        report.append("Følgende " + (numOfErrors == 1 ? "kontroll" : (numOfErrors + " kontroller")) + " har funnet feil eller advarsler:" +lf + lf);
        report.append(buffer);
      }
      else
      {
        if (lineNumber == 0) {
          report.append("Finner ingen data!");
          error_type = Constants.CRITICAL_ERROR;
        } else {          
          report.append("Ingen feil funnet!");
          report.append(controls[controls.length-1].getErrorReport(lineNumber));
        }
      }
      
      if (reportFile != null)
      {
        writer.write (report.toString());
        writer.close();
      } else {
        System.out.println(report);
      }
    }
    catch (Exception e)
    {
      System.out.println("Can't write report file: ");
      System.out.println(e.toString());;
      System.exit (Constants.IO_ERROR);
    } 
    
    return error_type;
  }

  private void initControls()
  {
    controls = new Control[32];
    controls[0] = new ControlRecordlengde();
    controls[1] = new ControlNumericalFields();
    controls[2] = new ControlKommunenummer();
    controls[3] = new ControlAargang();
    controls[4] = new ControlFodselsnummer();    
    controls[5] = new ControlAlderUnder18();    
    controls[6] = new ControlAlderOver99();
    controls[7] = new ControlKjonn();
    controls[8] = new ControlSivilstatus();
    controls[9] = new ControlBarnUnder18GyldigeVerdier();
    controls[10] = new ControlBarnUnder18Antall();    
    controls[11] = new ControlBarnUnder18();    
    controls[12] = new ControlBarnUnder18Mange();
    controls[13] = new ControlDatoRegNAV();
    controls[14] = new ControlDatoVedtakProgram();
    controls[15] = new ControlDatoStartProgram();       
//    controls[16] = new ControlDeltattKvalProgram();
//    controls[17] = new ControlDeltattKvalProgramAntall();   
    controls[16] = new ControlKvalProgramAnnenKommune();
    controls[17] = new ControlKvalProgramAnnenKommuneKnr();
    controls[18] = new Control_20a();
    controls[19] = new Control_21();
//    controls[20] = new Control_22();
    controls[20] = new Control_23();
//    controls[23] = new Control_24();
//    controls[23] = new Control_25();
    controls[21] = new Control_26();
    controls[22] = new Control_27();
    controls[23] = new Control_28();
    controls[24] = new Control_29();
    controls[25] = new Control_30();
    controls[26] = new Control_31();
    controls[27] = new Control_32();
    controls[28] = new Control_33();
//    controls[32] = new Control_34();
//    controls[33] = new Control_35();
    controls[29] = new Control_36();
    controls[30] = new Control_37();
    controls[31] = new Control_38();
//    controls[32] = new Control_39();    
  }
}
