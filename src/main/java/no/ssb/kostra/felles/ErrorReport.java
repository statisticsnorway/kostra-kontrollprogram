package no.ssb.kostra.felles;

import no.ssb.kostra.control.felles.Utils;
import no.ssb.kostra.controlprogram.Arguments;

import java.util.*;

@SuppressWarnings("SpellCheckingInspection")
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

    public ErrorReport(final Arguments args) {
        this.args = args;
    }

    public void incrementCount() {
        count++;
    }

    public boolean addEntry(final ErrorReportEntry errorReportEntry) {
        // Avklare status
        if (this.errorType < errorReportEntry.getErrorType()) {
            this.errorType = errorReportEntry.getErrorType();
        }

        // Hvis kontrollen hindrer innsending, legg til standardtekst slik dette vises også i oppsummeringen.
        if (errorReportEntry.getErrorType() == Constants.CRITICAL_ERROR) {
            String CRITICAL_ERROR_SHORT_TEXT_MSG = " *** NB! Denne feilen hindrer innsending! ***";
            errorReportEntry.setKontrollNr(errorReportEntry.getKontrollNr().concat(CRITICAL_ERROR_SHORT_TEXT_MSG));
        }

        // Legg til entry'en i en liste for oppsummering
        var l = 0L;
        if (mapEntries.containsKey(errorReportEntry.getKontrollNr())) {
            l = (mapEntries.get(errorReportEntry.getKontrollNr()) != null) ? mapEntries.get(errorReportEntry.getKontrollNr()) : 0L;
        }

        l++;
        mapEntries.put(errorReportEntry.getKontrollNr(), l);

        // Legg til en entry i hierarkiet
        final var saksbehandlerMap =
                rapportMap.containsKey(errorReportEntry.getSaksbehandler())
                        ? rapportMap.get(errorReportEntry.getSaksbehandler())
                        : new TreeMap<String, Map<String, List<String>>>();

        final var journalnummerMap = saksbehandlerMap.containsKey(errorReportEntry.getJournalnummer())
                ? saksbehandlerMap.get(errorReportEntry.getJournalnummer())
                : new TreeMap<String, List<String>>();

        final var entriesList = (journalnummerMap.containsKey(errorReportEntry.getKontrollNr()))
                ? journalnummerMap.get(errorReportEntry.getKontrollNr())
                : new ArrayList<String>();

        entriesList.add(errorReportEntry.getKontrollNr());
        entriesList.add(errorReportEntry.getErrorText());
        entriesList.add(Integer.toString(errorReportEntry.getErrorType()));

        journalnummerMap.put(errorReportEntry.getKontrollNr(), entriesList);
        saksbehandlerMap.put(errorReportEntry.getJournalnummer(), journalnummerMap);

        rapportMap.put(errorReportEntry.getSaksbehandler(), saksbehandlerMap);
        return entries.add(errorReportEntry);
    }

    public String generateReport() {
        final var VERSION = "2022.07.07";
        final var report = new StringBuilder();
        final var lf = (args.isRunAsExternalProcess()) ? "<br />" : "<br />" + System.getProperty("line.separator");

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
                .append("<span>Kontroller startet: ").append(startTime).append("</span>").append(lf)
                .append("<span>Rapport generert: ").append(Calendar.getInstance().getTime()).append("</span>").append(lf)
                .append("<span>Type filuttrekk: ").append(this.args.getSkjema()).append(".").append(this.args.getAargang()).append("</span>").append(lf)
                .append("<span>Antall sjekker utført: ").append(this.count).append("</span>").append(lf).append(lf)
                .append("<span>errorType:").append(errorType).append("</span>").append(lf);

        if (!mapEntries.isEmpty()) {
            report.append(lf).append("<h3>Oppsummering pr. kontroll:</h3>").append(lf);

            for (Map.Entry<String, Long> entry : mapEntries.entrySet()) {
                final var s = "***";
                final var val = entry.getKey();

                if (val.contains(s)) {
                    report.append(String.format("<span style='color: red  '>%s har funnet %d feil som hindrer innsending</span>", entry.getKey(), entry.getValue())).append(lf);
                } else {
                    report.append(String.format("<span style='color: black'>%s har funnet %d advarsler</span>", entry.getKey(), entry.getValue())).append(lf);
                }
            }


            report.append("<h3>Opplisting av feil, advarsler og meldinger</h3>").append(lf);
            report.append("<table>").append(lf);

            if (reportHeaders.stream().allMatch(s -> 0 < s.trim().length())) {
                report.append("<tr>");
                reportHeaders.forEach(s -> report.append(String.format("<td>%s</td>", s)));
                report.append("</tr>").append(lf);
            }

            for (String saksbehandler : rapportMap.keySet()) {
                final var saksbehandlerMap = rapportMap.get(saksbehandler);

                for (var journalnummer : saksbehandlerMap.keySet()) {
                    final var journalnummerMap = saksbehandlerMap.get(journalnummer);

                    for (var refNr : journalnummerMap.keySet()) {
                        final var entrieStringsList = journalnummerMap.get(refNr);
                        final var kontrollnummer = entrieStringsList.get(0);
                        final var kontrolltekst = entrieStringsList.get(1);
                        final var errorType = Integer.parseInt(entrieStringsList.get(2));
                        final var htmlcolor = errorType == Constants.CRITICAL_ERROR
                                ? "red  "
                                : errorType == Constants.NORMAL_ERROR
                                ? "black"
                                : "green";

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
            report.append("</table>");

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

    public void setReportHeaders(final List<String> stringList) {
        this.reportHeaders = stringList;
    }

    public void addStats(final StatsReportEntry entry) {
        this.stats.add(entry);
    }

    public Arguments getArgs() {
        return args;
    }
}
