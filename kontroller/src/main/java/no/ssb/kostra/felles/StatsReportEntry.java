package no.ssb.kostra.felles;

import java.util.List;

public class StatsReportEntry {
    private final String content;
    private final List<Code> classification;
    private final List<StatsEntry> data;

    public StatsReportEntry(String content, List<Code> classification, List<StatsEntry> data){
        this.content = content;
        this.classification = classification;
        this.data = data;
    }

    @Override
    public String toString(){
        final var lf = System.getProperty("line.separator");
        final var sb = new StringBuilder();

        sb.append("<table style='border: 1px solid black'>").append(lf);
        // add headers
        sb.append("<tr>").append("<td>&nbsp;</td>").append("<td>").append(content).append("</td>").append("</tr>").append(lf);


        // add content
        if (this.classification == null || this.classification.isEmpty()){
            data.forEach(d -> sb.append("<tr>").append("<td>&nbsp;</td>").append("<td style='text-align:right;'>").append(d.value()).append("</td>").append("</tr>").append(lf));
        } else {
            classification.forEach(c -> {
                StatsEntry se = data.stream().filter(e -> c.code().equalsIgnoreCase(e.id())).findFirst().orElse(new StatsEntry(c.code(), ".."));
                sb.append("<tr>").append("<td>").append(c.value()).append("</td>").append("<td style='text-align:right;'>").append(se.value()).append("</td>").append("</tr>").append(lf);
            });
        }

        // add ending
        sb.append("</table>").append(lf);
        return sb.toString();
    }
}
