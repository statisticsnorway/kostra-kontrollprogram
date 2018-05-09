/*
 * Constants.java
 *
 * Created on 24. september 2007, 09:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.ssb.kostra.control;

/**
 *
 * @author pll
 */
public final class Constants {
    
    // Rapporteringsaar.
    public static final String kostraYear = "2018";
    public static final String kvartalKostraYear = "2018";

    // Linjeskift til bruk ved skriving av kontrollrapporter.
    public static final String lineSeparator 
            = System.getProperty("line.separator");
    
    // Brukes i forbindelse med datohåndtering
    public static final String datoFormatKort = "ddMMyy";
    public static final String datoFormatLangt = "yyyy-MM-dd";

    
    ////////////////////////////////////////////////////////////////////////////
    //
    // Konstanter som brukes ved System.exit.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    // Resultat av kontroller.
    
    // Ingen feil er funnet.
    public static final int NO_ERROR = 0;
    
    // "Ikke-kritiske" feil er funnet.
    public static final int NORMAL_ERROR = 1;
    
    // "Kritiske" feil er funnet. Skal hindre innsending til SSB.
    public static final int CRITICAL_ERROR = 2;

    
    // Hvis programmet ikke lar seg kjoere normalt.
    
    // Uspesifikk feil.
    public static final int SYSTEM_ERROR = 253;   

    // Feil antall eller type argumenter ved start av programmet.
    public static final int PARAMETER_ERROR = 254;

    // F.eks. hvis kildefil ikke lar seg lese, eller rapportfil ikke 
    // er skrivbar.
    public static final int IO_ERROR = 255;   
    
    ////////////////////////////////////////////////////////////////////////////
    //
    // Tekstmeldinger.
    //
    ////////////////////////////////////////////////////////////////////////////
    
    public static final String CRITICAL_ERROR_TEXT_MSG = 
        "********************************************************************************************************" + lineSeparator +
        "*** NB! Kontrollen under er \"kritisk\", dvs. at forekomst av feil vil forhindre innsending til SSB. ***" + lineSeparator +
        "********************************************************************************************************";

    public static final String CRITICAL_ERROR_SHORT_TEXT_MSG = "\t*** NB! Denne feilen hindrer innsending! ***";
    
    public static int getRapporteringsAar() {
      
      int aar;
      
      try {
        aar = Integer.parseInt(kostraYear);
      } catch (NumberFormatException e) {
        aar = -1;
        System.exit (SYSTEM_ERROR);
      }
      
      return aar;
    }
    
    public static int getRapporteringsAarTwoDigits() {
      
      int aar;
      
      try {
        aar = Integer.parseInt(kostraYear.substring(2));
      } catch (NumberFormatException e) {
        aar = -1;
        System.exit (SYSTEM_ERROR);
      }
      
      return aar;
    }

} // End class Constants.
