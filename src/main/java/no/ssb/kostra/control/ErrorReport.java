package no.ssb.kostra.control;

import no.ssb.kostra.controlprogram.Arguments;

import java.util.*;

public class ErrorReport {
    private final String VERSION = "2020.11";
    private final List<ErrorReportEntry> entries = new ArrayList<>();
    private final Map<String, Long> mapEntries = new TreeMap<>();
    private final Map<String, Map<String, Map<String, List<String>>>> rapportMap = new TreeMap<>();
    private final Date startTime = Calendar.getInstance().getTime();
    private long count = 0;
    private int errorType = Constants.NO_ERROR;
    private Arguments args;
    private String executiveOfficerHeader = "";
    private String journalNumberHeader = "";


    public ErrorReport() {
        this.args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900"});
    }

    public ErrorReport(Arguments args) {
        this.args = args;
        this.executiveOfficerHeader = this.args.getSkjema().equalsIgnoreCase("15F") ? "Saksbehandler " : "";
        this.journalNumberHeader = this.args.getSkjema().equalsIgnoreCase("15F") ? "Journalnummer " : "";
    }


    public void incrementCount() {
        count++;
    }

    public void addEntry(ErrorReportEntry entry) {
        // Avklare status
        if (this.errorType < entry.getErrorType()) {
            this.errorType = entry.getErrorType();
        }

        // Hvis kontrollen hindrer innsending, legg til standardtekst slik dette vises også i oppsummeringen.
        if (entry.getErrorType() == Constants.CRITICAL_ERROR) {
            String CRITICAL_ERROR_SHORT_TEXT_MSG = " *** NB! Denne feilen hindrer innsending! ***";
            entry.setKontrollNr(entry.getKontrollNr().concat(CRITICAL_ERROR_SHORT_TEXT_MSG));
        }

        // Legg til entry'en i en liste for oppsummering
        long l = 0L;
        if (mapEntries.containsKey(entry.getKontrollNr())) {
            l = (mapEntries.get(entry.getKontrollNr()) != null) ? mapEntries.get(entry.getKontrollNr()) : 0L;
        }

        l++;
        mapEntries.put(entry.getKontrollNr(), l);

        // Legg til en entry i hierarkiet
        Map<String, Map<String, List<String>>> saksbehandlerMap = (rapportMap.containsKey(entry.getSaksbehandler())) ? rapportMap.get(entry
                .getSaksbehandler()) : new TreeMap<>();

        Map<String, List<String>> journalnummerMap = saksbehandlerMap.containsKey(entry.getJournalnummer()) ? saksbehandlerMap.get(entry
                .getJournalnummer()) : new TreeMap<>();

        List<String> entriesList = (journalnummerMap.containsKey(entry.getKontrollNr())) ? journalnummerMap.get(entry.getKontrollNr())
                : new ArrayList<>();

        entriesList.add(entry.getKontrollNr());
        entriesList.add(entry.getErrorText());
        entriesList.add(Integer.toString(entry.getErrorType()));

        journalnummerMap.put(entry.getKontrollNr(), entriesList);
        saksbehandlerMap.put(entry.getJournalnummer(), journalnummerMap);
        rapportMap.put(entry.getSaksbehandler(), saksbehandlerMap);
        entries.add(entry);
    }

    public String generateReport() {

        StringBuilder report = new StringBuilder();
        String lf = Constants.lineSeparator;

        report
                .append("<html>")
                .append("<head>").append(lf)
                .append("<title>Kontrollrapport</title>").append(lf)
                .append("<style>").append(lf)
                .append("ul.itemlist li.item { line-height: 5pt; }").append(lf)
                .append("ul.summarylist li.summary { line-height: normal; }").append(lf)
                .append("</style>").append(lf)
                .append("</head>").append(lf)
                .append("<body>").append(lf)
                .append("<hr/>").append(lf)
                .append("<h2>Kontrollrapport for ").append(this.args.getRegion()).append(" ").append(args.getNavn()).append("</h2>").append(lf)
                .append("<hr/>").append(lf).append(lf).append("<h4>Kontrollprogramversjon: ").append(VERSION).append("</h4>").append(lf)
                .append("<h4>Kontroller startet: ").append(startTime.toString()).append("</h4>").append(lf)
                .append("<h4>Rapport generert: ").append(Calendar.getInstance().getTime()).append("</h4>").append(lf)
                .append("<h4>Type filuttrekk: ").append(this.args.getSkjema()).append(".").append(this.args.getAargang()).append("</h4>").append(lf)
                .append("<h4>Antall sjekker utført: ").append(this.count).append("</h4>").append(lf).append(lf);

        if (!mapEntries.isEmpty()) {
            report.append(lf).append("<h3>Oppsummering pr. kontroll:</h3>").append(lf).append("<hr/>").append(lf).append(lf);

            for (Map.Entry<String, Long> entry : mapEntries.entrySet()) {
                CharSequence s = "***";
                String val = entry.getKey();

                if (val.contains(s)) {
                    report.append(String.format("<div style='color: red  '>%s har funnet %d feil som hindrer innsending</div>", entry.getKey(), entry.getValue())).append(lf);
                } else {
                    report.append(String.format("<div style='color: black'>%s har funnet %d advarsler</div>", entry.getKey(), entry.getValue())).append(lf);
                }
            }

            report
                    .append("<h4>Opplisting av feil og advarsler pr. ").append(executiveOfficerHeader).append(", ").append(journalNumberHeader).append(", kontrollnr. og kontrolltekst):</h4>").append(lf)
                    .append("<hr/>").append(lf);

            for (String saksbehandler : rapportMap.keySet()) {
                int kritiskeFeil = 0;
                int normaleFeil = 0;
                Map<String, Map<String, List<String>>> saksbehandlerMap = rapportMap.get(saksbehandler);
                String htmlcolor = "black";
                report.append("<h3>").append(executiveOfficerHeader).append(" ").append(saksbehandler).append("</h3>").append(lf);

                report.append("   <ul class='itemlist'>").append(lf);
                for (String journalnummer : saksbehandlerMap.keySet()) {
                    Map<String, List<String>> journalnummerMap = saksbehandlerMap.get(journalnummer);
                    report.append("      <li class='item' style='font-size:12pt'>").append(journalNumberHeader).append(" ").append(journalnummer).append(lf)
                            .append("      <ul>").append(lf);

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

                        report.append("         <li style='font-size:12pt; color: ").append(htmlcolor).append("'><pre>").append(kontrollnummer).append("</pre>").append(lf)
                                .append("         <ul><li style='font-size:12pt; color: ").append(htmlcolor).append("'><pre>").append(kontrolltekst).append("</pre></li></ul></li>").append(lf);

                    }

                    report
                            .append("      </ul>").append(lf)
                            .append("</li>");


                }

                report.append("      </ul>").append(lf);

                htmlcolor = (kritiskeFeil > 0) ? "red  " : "black";

                report.append("   <h3 style='color: ").append(htmlcolor).append("'>Oppsummering ").append(saksbehandler).append("</h3>").append(lf)
                        .append("   <ul class='summarylist'>")
                        .append("    <li class='summary' style='font-size:12pt; color: ").append(htmlcolor).append("'>Antall feil som hindrer innsending ").append(kritiskeFeil).append("</li>").append(lf)
                        .append("    <li class='summary' style='font-size:12pt'>Antall advarsler som kan sendes inn ").append(normaleFeil).append("</li>").append(lf);
                report.append("   </ul>").append(lf);
            }

        } else {
            if (count == 0) {
                report.append("Finner ingen data!  :-(");
                this.errorType = Constants.CRITICAL_ERROR;
            } else {
                report.append("Ingen feil funnet!").append(lf);
            }
        }

        report.append(lf).append("<div>errorType:").append(errorType).append(lf).append("<hr/></div>").append(lf).append(lf);
        report.append("</body></html>").append(lf);

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
