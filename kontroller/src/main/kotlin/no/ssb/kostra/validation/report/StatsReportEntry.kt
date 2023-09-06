package no.ssb.kostra.validation.report

data class StatsReportEntry(
    val heading: StatsEntryHeading,
    val entries: List<StatsEntry>
) {
    override fun toString(): String = StringBuilder().run {
        // add headers
        append("<table style='border: 1px solid black'>")
            .append(lf)
            .append("<tr><td>${heading.id}</td><td>${heading.measure}</td></tr>")
            .append(lf)

        // add content
        entries.forEach { entry ->
            append("<tr><td>${entry.id}</td><td style='text-align:right;'>${entry.value}</td></tr>")
                .append(lf)
        }

        // add ending
        append("</table>").append(lf)

        toString()
    }

    companion object {
        private val lf: String = System.getProperty("line.separator")
    }
}
