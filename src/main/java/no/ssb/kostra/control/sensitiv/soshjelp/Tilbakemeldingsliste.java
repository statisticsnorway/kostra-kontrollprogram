package no.ssb.kostra.control.sensitiv.soshjelp;

import no.ssb.kostra.control.Constants;
import no.ssb.kostra.utils.*;

import java.util.*;

import no.ssb.kostra.control.sensitiv.InvalidFnrException;

/**
 * 
 */

public final class Tilbakemeldingsliste extends no.ssb.kostra.control.Control
    implements no.ssb.kostra.control.SingleRecordErrorReport {

  
  
  
  private final String ERROR_TEXT = "Tilbakemeldingsliste";
  
  private int bidrag = 0;
  private int laan = 0;

  private int tilfeller_i_alt = 0;
  private int tilfeller_under_18 = 0;
  private int tilfeller_18_24 = 0;
  private int tilfeller_25_44 = 0;
  private int tilfeller_45_66 = 0;
  private int tilfeller_67_og_over = 0;
  private int tilfeller_ugyldig_fnr = 0;
  
  private int stonadstid_1 = 0;
  private int stonadstid_2_3 = 0;
  private int stonadstid_4_6 = 0;
  private int stonadstid_7_9 = 0;
  private int stonadstid_10_11 = 0;
  private int stonadstid_12 = 0;
  private int stonadstid_uoppgitt = 0;
  
  private int stonad_1_10k = 0;
  private int stonad_10k_50k = 0;
  private int stonad_50k_100k = 0;
  private int stonad_100k_150k = 0;
  private int stonad_over_150k = 0;
  private int stonad_uoppgitt = 0;
    
  private HashMap<String, Vector<String>> duplicate_list = new HashMap<String, Vector<String>>();
  private String region = "";
//  private DuplicateChecker full_duplicate_list = new DuplicateChecker();   --tatt vekk av raz 28.01.2013 pga warning "The value of the field Tilbakemeldingsliste.full_duplicate_list is not used"
  
  
  public boolean doControl(String line, int lineNumber, String region, String statistiskEnhet) {

    this.region = region;
    String fnr = RecordFields.getFodselsnummer(line);   
    Vector<String> vec;
    int bidrag_this_rec, laan_this_rec;//, kvalstonad_this_rec;
    
//    if (! full_duplicate_list.isDuplicate(line, lineNumber)) { // tatt bort av RAZ 22.01.2013
        { //satt inn av RAZ 22.01.2013
        tilfeller_i_alt++;
        
        try {
          bidrag_this_rec = Integer.parseInt(RecordFields.getBidrag(line));
        } catch (NumberFormatException e) { bidrag_this_rec = 0; }
        bidrag += bidrag_this_rec;
        
        try {
          laan_this_rec = Integer.parseInt(RecordFields.getLaan(line));
        } catch (NumberFormatException e) { laan_this_rec = 0; }
        laan += laan_this_rec;
                                  
        int fnr_is_ok = DatoFnr.validNorwId(fnr);
          
        int alder;
        try {
          alder = Toolkit.getAlderFromFnr(fnr);
        } catch (InvalidFnrException e) {
          // Noe er likevel galt med fnr!
          alder = -1;
        }

        if (fnr_is_ok == 1 && alder != -1) {
            if (alder < 18) tilfeller_under_18++;
            else if (alder > 17 && alder < 25) tilfeller_18_24++;
            else if (alder > 24 && alder < 45) tilfeller_25_44++;
            else if (alder > 44 && alder < 67) tilfeller_45_66++;
            else tilfeller_67_og_over++;          
        } else {
            tilfeller_ugyldig_fnr++;  
        }

        int antall_mnd = 0;
        String kode;
        int int_kode;
        for (int i=1; i<=12; i++) {
            kode = RecordFields.getFieldValue(line, (i<10?140+i:1400+i));
            try {
              int_kode = Integer.parseInt(kode);
            } catch (NumberFormatException e) { int_kode = -99; }
            if (int_kode == i) antall_mnd++;
        }
        if (antall_mnd == 1) stonadstid_1++;
        else if (antall_mnd > 1 && antall_mnd < 4) stonadstid_2_3++;
        else if (antall_mnd > 3 && antall_mnd < 7) stonadstid_4_6++;
        else if (antall_mnd > 6 && antall_mnd < 10) stonadstid_7_9++;
        else if (antall_mnd > 9 && antall_mnd < 12) stonadstid_10_11++;
        else if (antall_mnd == 12) stonadstid_12++;
        else {stonadstid_uoppgitt++;}
        
        int stonad = bidrag_this_rec + laan_this_rec;
        if (stonad > 0 && stonad < 10000) stonad_1_10k++;
        else if (stonad > 9999 && stonad < 50000) stonad_10k_50k++;
        else if (stonad > 49999 && stonad < 100000) stonad_50k_100k++;
        else if (stonad > 99999 && stonad < 150000) stonad_100k_150k++;
        else if (stonad > 149000) stonad_over_150k++;
        else stonad_uoppgitt++;
    
        if (! duplicate_list.containsKey(fnr)) {

            vec = new Vector<String>();
            vec.add(line);
            duplicate_list.put(fnr, vec);

        } else {

            vec = (Vector<String>) duplicate_list.get(fnr);
            if (! vec.contains(line)) vec.add(line);      

        }
      
    }    
        
    return false;
  }

  
  
  
  public String getErrorReport (int totalLineNumber) {
    
    StringBuilder report = new StringBuilder();
    report.append(lf+lf+lf+lf+lf);
    report.append("---------------------------------------------------------------------------------------------------------------------------------------------------" + lf);
    report.append("---------------------------------------------------------------------------------------------------------------------------------------------------" + lf + lf);
    report.append("SKJEMA NR. 11\t\tFORELØPIGE TALL FOR ØKONOMISK SOSIALHJELP\t\t"+region+" "+Regioner.getRegionName(region)+lf+lf);
    report.append("---------------------------------------------------------------------------------------------------------------------------------------------------" + lf);
    report.append("---------------------------------------------------------------------------------------------------------------------------------------------------" + lf + lf);
    report.append("Samlet stønad i alt\t"+ (bidrag + laan) + lf);
    report.append("Bidrag\t\t\t"+ bidrag + lf);
    report.append("Lån\t\t\t"+ laan + lf + lf);
    
    report.append("Sosialhjelpstilfeller" + lf + "i alt\t\t\t" + tilfeller_i_alt + lf + lf);
    
    report.append("Sosialhjelpstilfeller etter alder" + lf+lf);
    report.append("Under 18 år\t\t" + tilfeller_under_18 + lf);
    report.append("18-24 år\t\t" + tilfeller_18_24 + lf);
    report.append("25-44 år\t\t" + tilfeller_25_44 + lf);
    report.append("45-66 år\t\t" + tilfeller_45_66 + lf);
    report.append("67 år og over\t\t" + tilfeller_67_og_over + lf);
    report.append("Ugyldig f.nr.\t\t" + tilfeller_ugyldig_fnr + lf + lf);
    
    report.append("Sosialhjelpstilfeller etter stønadstid" + lf+lf);
    report.append("1 mnd\t\t\t" + stonadstid_1 + lf);
    report.append("2 - 3 mndr\t\t" + stonadstid_2_3 + lf);
    report.append("4 - 6 mndr\t\t" + stonadstid_4_6 + lf);
    report.append("7 - 9 mndr\t\t" + stonadstid_7_9 + lf);
    report.append("10 - 11 mndr\t\t" + stonadstid_10_11 + lf);
    report.append("12 mndr\t\t\t" + stonadstid_12 + lf);
    report.append("Uoppgitt varighet\t" + stonadstid_uoppgitt + lf + lf);

    report.append("Sosialhjelpstilfeller etter stønad" + lf+ lf);
    report.append("1-9999 kr\t\t" + stonad_1_10k + lf);
    report.append("10000-49999 kr\t\t" + stonad_10k_50k + lf);
    report.append("50000-99999 kr\t\t" + stonad_50k_100k + lf);
    report.append("100000-149999 kr\t" + stonad_100k_150k + lf);
    report.append("150000 kr og mer\t" + stonad_over_150k + lf);
    report.append("Uoppgitt stønad\t\t" + stonad_uoppgitt + lf + lf);
    
    
    report.append("---------------------------------------------------------------------------------------------------------------------------------------------------" + lf);
    report.append("Dubletter" + lf);
    report.append("---------------------------------------------------------------------------------------------------------------------------------------------------" + lf + lf);
    report.append("Bydel\tDistrikt\tFødselsnr\tBidrag\tLån\tJan\tFeb\tMar\tApr\tMai\tJun\tJul\tAug\tSep\tOkt\tNov\tDes" + lf+lf);
    String rec, fnr;
    Vector<String> vec;
    Set<String> fnr_set = duplicate_list.keySet();
    Iterator<String> fnr_iterator = fnr_set.iterator();
    Iterator<String> rec_iterator;
    while (fnr_iterator.hasNext()) {
      fnr = (String) fnr_iterator.next();
      vec = (Vector<String>) duplicate_list.get(fnr);
      if (vec.size()>1) {
        rec_iterator = vec.iterator();
        while (rec_iterator.hasNext()) {
          rec = (String) rec_iterator.next();
          report.append(RecordFields.getBydelsnummer(rec) + "\t");
          report.append(RecordFields.getDistriktsnummer(rec) + "\t");
          report.append(RecordFields.getFodselsnummer(rec) + "\t");
          report.append(RecordFields.getBidrag(rec) + "\t");
          report.append(RecordFields.getLaan(rec) + "\t");
          for (int j=1; j<=12; j++) {
            report.append(RecordFields.getFieldValue(rec,(j<10?140+j:1400+j)) + "\t");
          }
          report.append(lf);
        }
      }
    }
    /*
      report.append(RecordFields.getBydelsnummer(rec) + "\t");
      report.append(RecordFields.getDistriktsnummer(rec) + "\t");
      report.append(RecordFields.getFodselsnummer(rec) + "\t");
      report.append(RecordFields.getBidrag(rec) + "\t");
      report.append(RecordFields.getLaan(rec) + "\t");
      for (int j=1; j<=12; j++) {
        report.append(RecordFields.getFieldValue(rec,(j<10?130+j:1300+j)) + "\t");
      }
      report.append(lf);
    */
    
    
    return report.toString();
  }

  
  
  
  public boolean foundError() {
    return false;
  }  

  
  
  
  public String getErrorText() {
    return  ERROR_TEXT;
  }

  
  
  
  public int getErrorType() {
    return Constants.NORMAL_ERROR;
  }
}