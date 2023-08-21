package no.ssb.kostra.validation.report

import jakarta.inject.Inject
import no.ssb.kostra.felles.git.GitProperties
import java.util.*

class ValidationReport(private val validationReportArguments: ValidationReportArguments) {
    @Inject
    lateinit var gitProperties: GitProperties // inneholder versjonsinformasjonﬁ

    override fun toString(): String {
        val kotlinArguments = validationReportArguments.kotlinArguments
        val lf = if (kotlinArguments.isRunAsExternalProcess) "" else "\n"
        val endDiv = "</div>"
        val report = StringBuilder()

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
                .append("<div>Rapport generert: ").append(Calendar.getInstance().time).append(endDiv).append(lf)
                .append("<div>Type filuttrekk: ").append(kotlinArguments.skjema).append(".")
                .append(kotlinArguments.aargang)
                .append(endDiv).append(lf)
                .append("<div>Antall sjekker utført: ").append(count)
                .append(endDiv).append(lf).append(lf)
                .append("<div>Feilkode: ").append(severity.info.returnCode).append(endDiv)
                .append(lf)

            // summary
            if (numberOfControls == 0) {
                report.append("Finner ingen data!  :-(")

            } else if (reportEntries.isEmpty()) {
                report.append("Ingen feil funnet!").append(lf)

            } else {
                report.append(lf).append("<h3>Oppsummering pr. kontroll:</h3>").append(lf)

                reportEntries
                    .sortedBy {
                        with(it) {
                            listOf(caseworker, journalId, ruleName, messageText).joinToString("|")
                        }
                    }
                    .groupBy { it.ruleName }
                    .forEach { (title, group) ->
                        when (group.first().severity) {
                            Severity.FATAL ->
                                report.append(
                                    "<div style='font-size:12pt; vertical-align: top; color: ${Severity.FATAL.info.color}'>" +
                                            "$title har funnet ${group.size} feil som hindrer innsending " +
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

                report.append("TODO").append(lf)
            }

            // information per validation

            // statistics
            if (severity < Severity.ERROR && statsReportEntries.isNotEmpty()) {
                report.append("<h3>Statistikkrapport</h3>")
                statsReportEntries.forEach { statsReportEntry ->
                    report.append(statsReportEntry.toString())
                }
            }
        }

        return report.toString()
    }

    fun replaceSpaceWithNoBreakingSpace(s: String): String {
        return s.replace(" ", "&nbsp;")
    }
}