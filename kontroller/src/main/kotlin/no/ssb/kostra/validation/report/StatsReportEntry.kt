package no.ssb.kostra.validation.report

import no.ssb.kostra.program.Code

data class StatsReportEntry(
    val content: String,
    val codeList: List<Code>,
    val statsEntryList: List<StatsEntry>
) {
    override fun toString(): String = StringBuilder().run {
        // add headers
        append("<table style='border: 1px solid black'>")
            .append(lf)
            .append("<tr><td>&nbsp;</td><td>")
            .append(content)
            .append("</td>")
            .append("</tr>")
            .append(lf)

        // add content
        codeList.forEach { code ->
            val statsEntry = statsEntryList.firstOrNull { entry ->
                code.code.equals(entry.id, ignoreCase = true)
            } ?: StatsEntry(code.code, "..")

            append("<tr><td>")
                .append(code.value)
                .append("</td><td style='text-align:right;'>")
                .append(statsEntry.value)
                .append("</td>")
                .append("</tr>")
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
