package no.ssb.kostra.validation.report

import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.felles.git.GitPropertiesLoader
import java.time.Duration
import java.time.LocalDateTime

class ValidationReport(
    private val validationReportArguments: ValidationReportArguments,
    private val gitProperties: GitProperties = GitPropertiesLoader.loadGitProperties(
        GitPropertiesLoader.DEFAULT_GIT_PROPERTIES_FILENAME
    )
) {
    override fun toString(): String {
        val kotlinArguments = validationReportArguments.kotlinArguments
        val lf = "\n"
        val endDiv = "</div>"
        val report = StringBuilder()
        val now = LocalDateTime.now()
        val reportGenerationTime = Duration.between(kotlinArguments.startTime, now)

        with(validationReportArguments.validationResult) {
            // heading
            report
                .append("<html>")
                .append("<head>").append(lf)
                .append("<title>Kontrollrapport</title>").append(lf)
                .append("</head>").append(lf)
                .append("<body>").append(lf)
                .append("<hr/>").append(lf)
                .append("<h2>Kontrollrapport for ").append(kotlinArguments.region).append(" ")
                .append(kotlinArguments.navn)
                .append("</h2>").append(lf)
                .append("<hr/>").append(lf)
                .append("<div>Kontrollprogramversjon: ")
                .append(gitProperties.tags)
                .append(endDiv).append(lf)
                .append("<div>Kontroller startet: ").append(kotlinArguments.startTime).append(endDiv).append(lf)
                .append("<div>Rapport generert: ").append(now).append(endDiv).append(lf)
                .append("<div>Rapport generert på : ").append(reportGenerationTime.toMillis()).append(" ms")
                .append(endDiv).append(lf)
                .append("<div>Type filuttrekk: ").append(kotlinArguments.skjema).append(".")
                .append(kotlinArguments.aargang)
                .append(endDiv).append(lf)
                .append("<div>Antall sjekker utført: ").append(numberOfControls)
                .append(endDiv).append(lf)
                .append("<div>Returkode: ").append(severity.info.returnCode).append(endDiv)
                .append(lf)

            // summary
            when {
                numberOfControls == 0 -> report.append("<div>Finner ingen data!  :-(</div>").append(lf)
                reportEntries.isEmpty() -> report.append("<div>Ingen feil funnet!</div>").append(lf)
                else -> {
                    report.append(lf).append("<h3>Oppsummering pr. kontroll:</h3>").append(lf)

                    reportEntries
                        .sortedBy { it.ruleName }
                        .groupBy { it.ruleName }
                        .forEach { (title, group) ->
                            when (group.first().severity) {
                                Severity.FATAL ->
                                    report.append(
                                        "<div style='font-size:12pt; vertical-align: top; color: ${Severity.FATAL.info.color}'>" +
                                                "$title har funnet ${group.size} kritiske feil som hindrer innsending " +
                                                "og de andre kontrollene i bli kjørt</div>"
                                    ).append(lf)

                                Severity.ERROR ->
                                    report.append(
                                        "<div style='font-size:12pt; vertical-align: top; color: ${Severity.ERROR.info.color}'>" +
                                                "$title har funnet ${group.size} feil som hindrer innsending</div>"
                                    ).append(lf)

                                Severity.WARNING ->
                                    report.append(
                                        "<div style='font-size:12pt; vertical-align: top; color: ${Severity.WARNING.info.color}'>" +
                                                "$title har funnet ${group.size} advarsler</div>"
                                    ).append(lf)

                                Severity.INFO ->
                                    report.append(
                                        "<div style='font-size:12pt; vertical-align: top; color: ${Severity.INFO.info.color}'>" +
                                                "$title har funnet ${group.size} til informasjonsmeldinger</div>"
                                    ).append(lf)

                                else ->
                                    report.append(
                                        "<div style='font-size:12pt; vertical-align: top; color: ${Severity.OK.info.color}'>" +
                                                "$title har funnet ${group.size} meldinger</div>"
                                    ).append(lf)
                            }
                        }

                    report.append("<h3>Opplisting av feil, advarsler og meldinger</h3>").append(lf)
                    report.append("<table>").append(lf)

                    uniqueReportEntries
                        .sorted()
                        .map {
                            report
                                .append("<tr style='font-size:12pt; vertical-align: top; color: ")
                                .append(it.severity.info.color).append("'>")
                                .append("<td>").append(it.caseworker.replace(" ", "&nbsp;")).append("</td>")
                                .append("<td>").append(it.journalId).append("</td>")
                                .append("<td>").append(it.ruleName).append("</td>")
                                .append("<td>").append(it.messageText).append("</td>")
                                .append("<td>").append(it.lineNumbers).append("</td>")
                                .append("</tr>").append(lf)
                        }

                    report.append("</table>").append(lf)
                }
            }

            // information per validation
            // statistics
            if (severity < Severity.ERROR && statsReportEntries.isNotEmpty()) {
                report.append("<h3>Statistikkrapport</h3>").append(lf)
                statsReportEntries.forEach { statsReportEntry ->
                    report.append(statsReportEntry.toString())
                }
            }
        }

        return report.toString()
    }
}