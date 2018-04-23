package no.ssb.kostra.control.sensitiv.sosintro;

/*
 * $Log: Main.java,v $
 * Revision 1.13  2009/10/13 12:33:40  pll
 * Versjon: 2009-rapporteringen.
 *
 * Revision 1.12  2008/12/17 13:34:25  pll
 * Nytt versjonsnummer.
 *
 * Revision 1.11  2008/10/04 10:51:32  pll
 * Versjon: 2008-rapportering.
 *
 * Revision 1.10  2008/01/11 13:52:10  pll
 * Kritiske feil markeres i kontrollrapporten.
 *
 * Revision 1.9  2007/12/02 12:57:12  pll
 * Metoden start() fanger opp IOException og Exception ifbm. innlesing av
 * filuttrekket. Ved fanging av Exception kastes et nytt
 * UnreadableDataException-objekt. IOException kastes på nytt.
 *
 * Revision 1.8  2007/11/27 19:54:20  pll
 * Byttet String med StringBuffer av hensyn til ytelse.
 *
 * Revision 1.7  2007/11/14 09:28:20  pll
 * Skriver feilmeldinger til System.out isteden for System.err.
 * Bruker exit-kode definert i no.ssb.kostra.control.Constants.
 *
 * Revision 1.6  2007/11/07 11:09:07  pll
 * Metoden start() returnerer int-verdi.
 * Fanger opp returverdi fra kontrollene.
 * Forbedret håndtering av manglende data (tomme filer).
 *
 * Revision 1.5  2007/11/07 10:52:24  pll
 * Nytt versjonsnummer.
 *
 * Revision 1.4  2007/11/06 07:37:33  pll
 * Nytt versjonsnummer etter bugfix.
 *
 * Revision 1.3  2007/10/08 09:42:43  pll
 * Modifisert for alternativ bruk av System.in/System.out i steden for filer.
 *
 * Revision 1.2  2007/10/05 04:40:35  pll
 * Benytter no.ssb.kostra.utils.Regioner isteden for
 * no.ssb.kostra.utils.Regioner2006.
 *
 * Revision 1.1.1.1  2007/09/18 12:24:07  pll
 * Versjon: 2006-rapporteringen
 *
 * Revision 1.3  2006/10/03 13:22:47  lwe
 * Lagt til ny kontroll (avslutningsdato før startdato)
 *
 * Revision 1.2  2006/09/22 09:13:51  lwe
 * Oppdatert årgang
 *
 * Revision 1.1  2006/09/22 08:18:32  lwe
 * Flyttet 2005-filene over i 2006-katalog - men ikke oppdatert årstallene
 *
 * Revision 1.2  2006/01/05 08:16:38  lwe
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
  private File sourceFile;
  private File reportFile;
  private Control[] controls;
  private final String lf = Constants.lineSeparator;
  private final String VERSION = "11B." + Constants.kostraYear + ".01";
  
  private Vector<String[]> errorLines = new Vector<String[]>();

  public Main 
      (String regionNumber, File sourceFile, File reportFile)
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
                container[2] = ((SingleRecordErrorReport) controls[i]).getErrorText() + (controls[i].getErrorType()==Constants.CRITICAL_ERROR?Constants.CRITICAL_ERROR_SHORT_TEXT_MSG:"");
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
            container[2] = ((SingleRecordErrorReport) controls[0]).getErrorText()+ (controls[0].getErrorType()==Constants.CRITICAL_ERROR?Constants.CRITICAL_ERROR_SHORT_TEXT_MSG:"")  + lf;
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
             report.append ("-------------------------------------------------" + lf);    
             report.append (lf);
             report.append (" Feilmeldingsrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + lf);
             report.append (lf);
             report.append ("-------------------------------------------------" + lf + lf);
      report.append ("Kontrollprogramversjon: " + VERSION + lf);
      report.append ("Rapport generert: " + Calendar.getInstance().getTime() + lf);
      report.append ("Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
      report.append ("Type filuttrekk: Introduksjonsstønad " + Constants.kostraYear + lf + lf);

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

          report.append ( lf + "Opplisting av feil pr. record (recordnr., fødselsnr., kontrolltekst):" + lf +
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
        if (lineNumber == 0) {
          report.append ( "Finner ingen data!");
          error_type = Constants.CRITICAL_ERROR;
        } else {          
          report.append ( "Ingen feil funnet!");
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
    controls = new Control[23];
    controls[0] = new ControlRecordlengde();
    controls[1] = new ControlNumericalFields();
    controls[2] = new ControlKommunenummer();
    controls[3] = new ControlAargang();
    controls[4] = new ControlFodselsnummer();
    controls[5] = new ControlDUFnummer();
    controls[6] = new ControlKjonn();
    controls[7] = new ControlSivilstatus();
    controls[8] = new ControlStartdato();
    controls[9] = new ControlInntektsgivendeArbeid();
    controls[10] = new ControlKursYrkesforbTiltak();
    controls[11] = new ControlStatusDeltakelseAaretsSlutt();
    controls[12] = new ControlAvslutningsdato();
    controls[13] = new ControlStonadsmaaneder();
    controls[14] = new ControlStonadssum();
    controls[15] = new ControlAvslutningsdatoForStartdato(); 
    controls[16] = new Control_17(); 
    controls[17] = new Control_18(); 
    controls[18] = new Control_19(); 
    controls[19] = new Control_20(); 
    controls[20] = new Control_21(); 
    controls[21] = new Control_22();
    controls[22] = new Control_23();
  }
}
