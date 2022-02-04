package no.ssb.kostra.felles;

import no.ssb.kostra.control.felles.Utils;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.*;

public class ErrorReport {
    private final List<ErrorReportEntry> entries = new ArrayList<>();
    private final Map<String, Long> mapEntries = new TreeMap<>();
    private final Map<String, Map<String, Map<String, List<String>>>> rapportMap = new TreeMap<>();
    private final Date startTime = Calendar.getInstance().getTime();
    private final Arguments args;
    private long count = 0;
    private int errorType = Constants.NO_ERROR;
    private List<String> reportHeaders = List.of("", "", "", "");
    private final List<StatsReportEntry> stats = new ArrayList<>();


    public ErrorReport() {
        this.args = new Arguments(new String[]{"-s", "X", "-y", "9999", "-r", "999900"});
    }

    public ErrorReport(Arguments args) {
        this.args = args;
    }

    public void incrementCount() {
        count++;
    }

    public boolean addEntry(ErrorReportEntry entry) {
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
        return entries.add(entry);
    }

    public String generateReport() {
        final String VERSION = "2022.02.5";

        StringBuilder report = new StringBuilder();

        final String lf = System.getProperty("line.separator");

        if (count == 0) {
            this.errorType = Constants.CRITICAL_ERROR;
        }


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
                .append("<hr/>").append(lf).append("<span>Kontrollprogramversjon: ").append(VERSION).append("</span>").append(lf)
                .append("<span>Kontroller startet: ").append(startTime.toString()).append("</span>").append(lf)
                .append("<span>Rapport generert: ").append(Calendar.getInstance().getTime()).append("</span>").append(lf)
                .append("<span>Type filuttrekk: ").append(this.args.getSkjema()).append(".").append(this.args.getAargang()).append("</span>").append(lf)
                .append("<span>Antall sjekker utført: ").append(this.count).append("</span>").append(lf).append(lf)
                .append("<span>errorType:").append(errorType).append("</span>").append(lf);

        if (!mapEntries.isEmpty()) {
            report.append(lf).append("<h3>Oppsummering pr. kontroll:</h3>").append(lf);

            for (Map.Entry<String, Long> entry : mapEntries.entrySet()) {
                CharSequence s = "***";
                String val = entry.getKey();

                if (val.contains(s)) {
                    report.append(String.format("<span style='color: red  '>%s har funnet %d feil som hindrer innsending</span>", entry.getKey(), entry.getValue())).append(lf);
                } else {
                    report.append(String.format("<span style='color: black'>%s har funnet %d advarsler</span>", entry.getKey(), entry.getValue())).append(lf);
                }
            }


            report.append("<h3>Opplisting av feil, advarsler og meldinger</h3>");
            report.append("<table>");

            if (reportHeaders.stream().allMatch(s -> 0 < s.trim().length())) {
                report.append("<tr>");
                reportHeaders.forEach(s -> report.append(String.format("<td>%s</td>", s)));
                report.append("</tr>").append(lf);
            }

            for (String saksbehandler : rapportMap.keySet()) {
                Map<String, Map<String, List<String>>> saksbehandlerMap = rapportMap.get(saksbehandler);

                for (String journalnummer : saksbehandlerMap.keySet()) {
                    Map<String, List<String>> journalnummerMap = saksbehandlerMap.get(journalnummer);

                    for (String refNr : journalnummerMap.keySet()) {
                        List<String> entrieStringsList = journalnummerMap.get(refNr);
                        String kontrollnummer = entrieStringsList.get(0);
                        String kontrolltekst = entrieStringsList.get(1);
                        int errorType = Integer.parseInt(entrieStringsList.get(2));
                        String htmlcolor = (errorType == Constants.CRITICAL_ERROR) ? "red  " : (errorType == Constants.NORMAL_ERROR) ? "black" : "green";

                        if (args.isRunAsExternalProcess()) {
                            report.append(lf);
                        }

                        report
                                .append("<tr style='font-size:12pt; vertical-align: top; color: ").append(htmlcolor).append("'>")
                                .append("<td>").append(Utils.replaceSpaceWithNoBreakingSpace(saksbehandler)).append("</td>")
                                .append("<td>").append(journalnummer).append("</td>")
                                .append("<td>").append(kontrollnummer).append("</td>")
                                .append("<td>").append(kontrolltekst).append("</td>")
                                .append("</tr>");
                    }
                }
            }
            report.append("</table>").append("<br />");

        } else {
            if (count == 0) {
                report.append("Finner ingen data!  :-(");
                this.errorType = Constants.CRITICAL_ERROR;
            } else {
                report.append("Ingen feil funnet!").append(lf);
            }
        }

        if (errorType != Constants.CRITICAL_ERROR && 0 < stats.size()) {
            report.append("<h3>Statistikkrapport</h3>");
            stats.forEach(s -> report.append(s.toString()));
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

    public void setReportHeaders(List<String> stringList) {
        this.reportHeaders = stringList;
    }

    public void addStats(StatsReportEntry entry) {
        this.stats.add(entry);
    }

    public Arguments getArgs() {
        return args;
    }
}
