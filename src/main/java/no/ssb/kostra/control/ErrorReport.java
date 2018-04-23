package no.ssb.kostra.control;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import no.ssb.kostra.utils.Regioner;

public class ErrorReport {
	private final String VERSION = "15." + Constants.kostraYear + ".01";
	private long antall = 0;
	private List<ErrorReportEntry> entries = new ArrayList<ErrorReportEntry>();
	private Map<String, Long> mapEntries = new TreeMap<String, Long>();
	private Map<String, Map<String, Map<String, List<String>>>> rapportMap = new TreeMap<String, Map<String, Map<String, List<String>>>>();
	private int errorType = Constants.NO_ERROR;
	private Date startTime = Calendar.getInstance().getTime();

	public long incrementAntall() {
		antall++;
		return antall;
	}

	public void addEntry2(ErrorReportEntry entry) {
		try {
			if (this.errorType < entry.getErrorType()) {
				this.errorType = entry.getErrorType();
			}

			if (entry.getErrorType() == Constants.CRITICAL_ERROR) {
				String errorText = new String("**Hindrer innsending** " + entry.getErrorText());
				// errorText.concat(Constants.CRITICAL_ERROR_SHORT_TEXT_MSG);
				entry.setErrorText(errorText);
			}

			Long l = 0L;
			if (mapEntries.containsKey(entry.getKontrollNr())) {
				l = (mapEntries.get(entry.getKontrollNr()) != null) ? mapEntries.get(entry.getKontrollNr()) : 0L;
			}

			l++;
			mapEntries.put(entry.getKontrollNr(), l);
			entries.add(entry);

		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	public void addEntry(ErrorReportEntry entry) {
		try {
			// Avklare status
			if (this.errorType < entry.getErrorType()) {
				this.errorType = entry.getErrorType();
			}

			// Hvis kontrollen hindrer innsending, legg til standardtekst slik dette vises også i oppsummeringen.
			if (entry.getErrorType() == Constants.CRITICAL_ERROR) {
				entry.setKontrollNr(entry.getKontrollNr().concat(Constants.CRITICAL_ERROR_SHORT_TEXT_MSG));
			}

			// Legg til entry'en i en liste for oppsummering
			Long l = 0L;
			if (mapEntries.containsKey(entry.getKontrollNr())) {
				l = (mapEntries.get(entry.getKontrollNr()) != null) ? mapEntries.get(entry.getKontrollNr()) : 0L;
			}

			l++;
			mapEntries.put(entry.getKontrollNr(), l);

			// Legg til en entry i hierarkiet
			Map<String, Map<String, List<String>>> saksbehandlerMap = (rapportMap.containsKey(entry.getSaksbehandler())) ? rapportMap.get(entry
					.getSaksbehandler()) : new TreeMap<String, Map<String, List<String>>>();

			Map<String, List<String>> journalnummerMap = saksbehandlerMap.containsKey(entry.getJournalnummer()) ? saksbehandlerMap.get(entry
					.getJournalnummer()) : new TreeMap<String, List<String>>();

			List<String> entriesList = (journalnummerMap.containsKey(entry.getKontrollNr())) ? journalnummerMap.get(entry.getKontrollNr())
					: new ArrayList<String>();

			entriesList.add(entry.getKontrollNr());
			entriesList.add(entry.getErrorText());
			entriesList.add(new Integer(entry.getErrorType()).toString());

			journalnummerMap.put(entry.getKontrollNr(), entriesList);
			saksbehandlerMap.put(entry.getJournalnummer(), journalnummerMap);
			rapportMap.put(entry.getSaksbehandler(), saksbehandlerMap);
			entries.add(entry);

		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	public String generateReport(String regionNumber, File sourceFile, File reportFile) {
		StringBuffer report = new StringBuffer();
		String lf = Constants.lineSeparator;

		report.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + lf);
		report.append("<html><body>" + lf);
		report.append("<hr/><br/>" + lf);
		report.append("<br/>" + lf);
		report.append("<h3>Kontrollrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + "</h3>"+ lf);
		report.append("<br/>" + lf);
		report.append("<hr/><br/><br/>" + lf + lf);

		report.append("<h4>Kontrollprogramversjon: " + VERSION + "</h4>" + lf);
		report.append("<h4>Kontroller startet: " + startTime.toString() + "</h4>" + lf);
		report.append("<h4>Rapport generert: " + Calendar.getInstance().getTime() + "</h4>" + lf);
		report.append("<h4>Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + "</h4>" + lf);
		report.append("<h4>Type filuttrekk: Barnevern " + Constants.kostraYear + "</h4>" + lf + lf);

		if (!mapEntries.isEmpty()) {
			report.append(lf + "<h3>Oppsummering pr. kontroll:</h3>" + lf + "<hr/><br/>" + lf + lf);

			for (Map.Entry<String, Long> entry : mapEntries.entrySet()) {
				CharSequence s = "***";
				String val = entry.getKey();

				if (val.contains(s)){
					report.append(String.format("<div style='color: red  '>Kontroll %s har funnet %d feil som hindrer innsending</div>", entry.getKey(), entry.getValue()) + lf);
				} else {
					report.append(String.format("<div style='color: black'>Kontroll %s har funnet %d advarsler</div>", entry.getKey(), entry.getValue()) + lf);
										
				}
			}

			report.append("<br/>" + lf + "<h4>Opplisting av feil og advarsler pr. saksbehandler, journalnr., kontrollnr. og kontrolltekst):</h4>" + lf
					+ "<hr/><br/><br/>" + lf + lf);

			for (String saksbehandler : rapportMap.keySet()) {
				int kritiskeFeil = 0;
				int normaleFeil = 0;
				Map<String, Map<String, List<String>>> saksbehandlerMap = rapportMap.get(saksbehandler);
				String htmlcolor = "black"; 
				report.append("<h3>Saksbehandler " + saksbehandler + "</h3>" + lf);

				report.append("   <ul>" + lf);
				for (String journalnummer : saksbehandlerMap.keySet()) {
					Map<String, List<String>> journalnummerMap = saksbehandlerMap.get(journalnummer);
					report.append("      <li>Journalnummer " + journalnummer + "</li>" + lf);
					report.append("      <ul>" + lf);

					for (String refNr : journalnummerMap.keySet()) {
						List<String> entrieStringsList = journalnummerMap.get(refNr);
						String kontrollnummer = entrieStringsList.get(0);
						String kontrolltekst = entrieStringsList.get(1);
						int errorType = Integer.parseInt(entrieStringsList.get(2));
						if (errorType == Constants.CRITICAL_ERROR) {
							kritiskeFeil++;
							htmlcolor = "red  ";
						} else if (errorType == Constants.NORMAL_ERROR) {
							htmlcolor = "black"; 
							normaleFeil++;
						}

						report.append("         <li style='color: " + htmlcolor + "'>" + kontrollnummer + lf);
						report.append("         <ul><li style='color: " + htmlcolor + "'>"+ kontrolltekst +"</li></ul></li>" + lf);

					}
					report.append("      </ul>" + lf);

				}

				htmlcolor = (kritiskeFeil > 0) ? "red  " : "black";
				
				report.append(
						"   <h3 style='color: " + htmlcolor + "'>Oppsummering " + saksbehandler + "</h3>" + lf + 
						"   <ul><li style='color: " + htmlcolor + "'>Antall feil som hindrer innsending " + kritiskeFeil	+ "</li>" + lf + 
						"   <li>Antall advarsler som kan sendes inn " + normaleFeil + "</li></ul><br/>"
						+ lf + lf + lf);
				report.append("   </ul>" + lf);
				report.append("</ul><br/>"  + lf);

			}

			report.append(lf + "<div>errorType:" + errorType + lf + "<hr/></div>" + lf + lf);
			report.append("</body></html>" + lf);

			
		} else {
			if (antall == 0) {
				report.append("Finner ingen data!  :-(");
				this.errorType = Constants.CRITICAL_ERROR;
			} else {
				report.append("Ingen feil funnet!");
			}
		}

		return report.toString();
	}

	public String generateReport2(String regionNumber, File sourceFile, File reportFile) {
		StringBuffer report = new StringBuffer();
		String lf = Constants.lineSeparator;

		report.append("-------------------------------------------------" + lf);
		report.append(lf);
		report.append(" Kontrollrapport for " + regionNumber + " " + Regioner.getRegionName(regionNumber) + lf);
		report.append(lf);
		report.append("-------------------------------------------------" + lf + lf);

		report.append("Kontrollprogramversjon: " + VERSION + lf);
		report.append("Rapport generert: " + Calendar.getInstance().getTime() + lf);
		report.append("Kontrollert fil: " + (sourceFile != null ? sourceFile.getAbsolutePath() : "") + lf);
		report.append("Type filuttrekk: Barnevern " + Constants.kostraYear + lf + lf);

		if (!entries.isEmpty()) {
			report.append(lf + "Opplisting av feil pr. saksbehandler -> journalnummer -> referansenr -> kontrollnummer og -tekst):" + lf
					+ "---------------------------------------------------------------------" + lf + lf);

			// report.append("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
			// + lf);
			// report.append("00000000011111111112222222222333333333344444444445555555555666666666677777777778888888888999999999900000000011111111112222222222333333333344444444445555555555666666666677777777778888888888999999999990"
			// + lf);
			// report.append("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112"
			// + lf);

			for (ErrorReportEntry ere : entries) {
				report.append(String.format("%-15s %-80s %s", ere.getJournalnummer(), ere.getKontrollNr(), ere.getErrorText()) + lf);
			}
			report.append(lf + "Oppsummering pr. kontroll:" + lf + "-------------------------------------------------" + lf + lf);

			for (Map.Entry<String, Long> entry : mapEntries.entrySet()) {
				report.append(String.format("Kontroll %s har funnet %d feil eller advarsler", entry.getKey(), entry.getValue()) + lf);
			}

			report.append(lf + "errorType:" + errorType + lf + "-------------------------------------------------" + lf + lf);

		} else {
			if (antall == 0) {
				report.append("Finner ingen data! :-(");
				this.errorType = Constants.CRITICAL_ERROR;
			} else {
				report.append("Ingen feil funnet!");
			}
		}

		return report.toString();
	}

	public int getErrorType() {
		return errorType;
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public int size() {
		return entries.size();
	}
}
