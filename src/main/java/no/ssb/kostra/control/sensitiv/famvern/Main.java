package no.ssb.kostra.control.sensitiv.famvern;

/*
 * $Log: Main.java,v $
 * Revision 1.14  2008/12/17 10:08:18  pll
 * Bugfix: kontornummer i feilrapport.
 *
 * Revision 1.13  2008/12/16 13:15:14  pll
 * TP1.
 *
 * Revision 1.12  2008/12/13 11:57:02  pll
 * Under construction...
 *
 * Revision 1.11  2008/12/13 11:49:17  pll
 * Under construction...
 *
 * Revision 1.10  2008/12/13 09:16:12  pll
 * Under construction...
 *
 * Revision 1.9  2008/12/12 17:00:36  pll
 * Under construction...
 *
 * Revision 1.8  2008/12/11 14:00:42  pll
 * Under construction...
 *
 * Revision 1.7  2008/12/10 12:51:55  pll
 * Filtype 52 er endret til 52A.
 *
 * Revision 1.6  2007/12/02 12:56:29  pll
 * Metoden start() fanger opp IOException og Exception ifbm. innlesing av
 * filuttrekket. Ved fanging av Exception kastes et nytt
 * UnreadableDataException-objekt. IOException kastes på nytt.
 *
 * Revision 1.5  2007/11/27 20:49:58  pll
 * Byttet String med StringBuffer av hensyn til ytelse.
 *
 * Revision 1.4  2007/11/14 09:34:07  pll
 * Skriver feilmeldinger til System.out isteden for System.err.
 * Bruker exit-kode definert i no.ssb.kostra.control.Constants.
 *
 * Revision 1.3  2007/10/08 09:42:12  pll
 * Modifisert for alternativ bruk av System.in/System.out i steden for filer.
 *
 * Revision 1.2  2007/10/05 04:40:35  pll
 * Benytter no.ssb.kostra.utils.Regioner isteden for
 * no.ssb.kostra.utils.Regioner2006.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.2  2006/09/22 09:13:49  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:28  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.6  2006/01/05 08:16:33  lwe
 * added logmessage
 * 
 */
 
import java.io.*;
import java.util.*;
import no.ssb.kostra.control.*;
import no.ssb.kostra.utils.Regioner;

public final class Main 
{
  private String regionNumber;
  private String kontorNumber;
  private File sourceFile;
  private File reportFile;
  private Control[] controls;
  private final String lf = Constants.lineSeparator;
  private final String VERSION = "52A." + Constants.kostraYear + ".01";
  
  private Vector<String[]> errorLines = new Vector<String[]>();

  public Main 
    (String regionNumber, File sourceFile, File reportFile, String kontorNumber)
  {
    this.regionNumber = regionNumber;
    this.kontorNumber = kontorNumber;
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
            //Kontroll 4 er spesiell: krever kontornummer.
            //((ControlKontornummer) controls[3]).setKontorNumber (kontorNumber);
          
            printNewline = false;
            for (int i=1; i<controls.length; i++)
            {
              if (controls[i].doControl(line, lineNumber, regionNumber, ""))
              {
                container = new String[3];
                container[0] = " " + lineNumber + "  ";
                container[1] = line.substring(9, 18) + "  ";
                container[2] = ((SingleRecordErrorReport) controls[i]).getErrorText();
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
            container[2] = ((SingleRecordErrorReport) controls[0]).getErrorText() + lf;
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
//      e.printStackTrace(); //RAZ 20130703
      throw new UnreadableDataException();
    }

    try
    {
      BufferedWriter writer = null;
      if (reportFile != null)
        writer = new BufferedWriter (new FileWriter (reportFile));

      StringBuffer report = new StringBuffer(); 
      report.append ( "-------------------------------------------------" + lf);    
      report.append ( lf);
      report.append ( " Feilmeldingsrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + lf);
      report.append ( lf);
      report.append ( " Kontornummer " + kontorNumber + lf);
      report.append ( lf);
      report.append ( "-------------------------------------------------" + lf + lf);
      report.append ( "Kontrollprogramversjon: " + VERSION + lf);
      report.append ( "Rapport generert: " + Calendar.getInstance().getTime() + lf);
      report.append ( "Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
      report.append ( "Type filuttrekk: Familievern 52A " + Constants.kostraYear + lf + lf);

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

          report.append ( lf + "Opplisting av feil pr. record (recordnr., journalnr., kontrolltekst):" + lf +
                         "---------------------------------------------------------------------" + lf + lf);

          for (int i=0; i<errorLinesSize; i++)
          {
            container = (String[]) errorLines.elementAt(i);
            report.append ( container[0] + container[1] + container[2] + lf); 
          }
        }
        report.append ( lf + "Oppsummering pr. kontroll:" + lf +
        "-------------------------------------------------" + lf + lf);
        report.append ( "Følgende " + (numOfErrors == 1 ? "kontroll" : (numOfErrors + " kontroller")) + " har funnet feil eller advarsler:" +lf + lf);
        report.append ( buffer);  
      }
      else
      {
        report.append ( "Ingen feil funnet!");
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
    controls = new Control[35];
    controls[0] = new ControlRecordlengde();
    controls[1] = new ControlNumericalFields();
    controls[2] = new ControlFylkesnummer();
    controls[3] = new ControlKontornummer();
    controls[4] = new ControlFylkeKontornummer();
    controls[5] = new ControlDubletter();
    controls[6] = new Control7();
    controls[7] = new Control9();
    controls[8] = new Control11();
    controls[9] = new Control13();
    controls[10] = new Control14();
    controls[11] = new Control15();
    controls[12] = new Control16();
    controls[13] = new Control17();
    controls[14] = new Control18();
    controls[15] = new Control19A();
    controls[16] = new Control19B();
    controls[17] = new Control20();
    controls[18] = new Control21();
    controls[19] = new Control22();
    controls[20] = new Control23();
    controls[21] = new Control24();
    controls[22] = new Control25();
    controls[23] = new Control26();
    controls[24] = new Control27();
    controls[25] = new Control28();
    controls[26] = new Control29();
    controls[27] = new Control30();
    controls[28] = new Control31();
    controls[29] = new Control32();
    controls[30] = new Control33();
    controls[31] = new Control34();
    controls[32] = new Control35();
    controls[33] = new Control36();
    controls[34] = new Control37();
  }
}
