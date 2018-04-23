package no.ssb.kostra.control.regnskap.regn0Akvartal;

import java.io.*;
import java.util.*;
import no.ssb.kostra.utils.RegionerKvartal;
import no.ssb.kostra.control.*;

public final class Main {
  
  private int kvartal;
  private String regionNumber;
  private String[] regnskap;
  private File sourceFile;
  private File reportFile;
  private Control[] controls;
//  private Control[] linecontrols;
//  private Control[] comparecontrols;
  private final String lf = Constants.lineSeparator;
  private final String VERSION = "0A-kvartal." + Constants.kvartalKostraYear + ".01";

  
  
  
  public Main (String regionNumber, File sourceFile, 
               File reportFile, String[] regnskap, int kvartal) {
    
    this.regionNumber = regionNumber;
    this.sourceFile = sourceFile;
    this.reportFile = reportFile;
    this.regnskap = regnskap;
    this.kvartal = kvartal;
    initControls();
  }

  
  
  
  public int start() {
    
    int lineNumber = 0;
    int error_type = Constants.NO_ERROR;
    CharSequence z = "z";
    CharSequence tilde = "~";
    
    for (int i=0; i<regnskap.length; i++) {
      
      if (! regnskap[i].equalsIgnoreCase("")) {
        
        lineNumber += 1;
        
        //Sjekker recordlengde forst, fordi feil reccordlengde 
        //vil kunne odelegge mange andre kontroller 
        //(StringIndexOutOfBoundsException etc.)
        // Legger på sjekk at linja inneholder z eller ~ så lar vi være å gjøre noe
        if (! controls[0].doControl(regnskap[i], lineNumber, regionNumber, "") &&
        	!(regnskap[i].contains(z) || regnskap[i].contains(tilde))
        ) {
          
          for (int j=1; j<controls.length; j++)
            controls[j].doControl(regnskap[i], lineNumber, regionNumber, "");
          
        }
      }
    }             

    try
    {
      BufferedWriter fileWriter = null;
      if (reportFile != null)
        fileWriter = new BufferedWriter (new FileWriter (reportFile));

      StringBuilder report = new StringBuilder ("-------------------------------------------------" + lf);    
      report.append(lf);
      report.append(" Feilmeldingsrapport for " + regionNumber + " " + RegionerKvartal.getRegionName(regionNumber) + lf);
      report.append(lf);
      report.append("-------------------------------------------------" + lf + lf);
      report.append("Kontrollprogramversjon: " + VERSION + lf);
      report.append("Rapport generert: " + Calendar.getInstance().getTime() + lf);
      report.append("Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
      report.append("Type regnskap: 0A - Bevilgningsregnskap kommune, kvartalsregnskap" + lf + lf);

      int numOfErrors = 0;
      int control_error_type;
      StringBuilder buffer = new StringBuilder();
      
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
        }
      }
      
      if (fileWriter != null)
      {  
        fileWriter.write (report.toString());
        fileWriter.close();
      } else {
        System.out.println(report.toString());
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

  private void initControls() {

    controls = new Control[13];
    controls[0] = new ControlRecordlengde();
    controls[1] = new ControlAargang();
    controls[2] = new ControlKvartal (kvartal);
    controls[3] = new ControlKommunenummer();
    controls[4] = new ControlKontoklasse();
    controls[5] = new ControlFunksjoner();
    controls[6] = new ControlArter();
    controls[7] = new ControlKontoklasseOgFunksjon();
    controls[8] = new ControlKontoklasseOgArtInvestering();
    controls[9] = new ControlKontoklasseOgArtDrift();
    controls[10] = new ControlDubletter();
    controls[11] = new ControlRecord();
    controls[12] = new no.ssb.kostra.control.regnskap.ControlOrgNr();
  }
}