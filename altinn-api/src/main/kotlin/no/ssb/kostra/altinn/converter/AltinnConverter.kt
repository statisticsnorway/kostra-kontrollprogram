package no.ssb.kostra.altinn.converter

import no.ssb.kostra.altinn.model.AltinnRapport
import no.ssb.kostra.altinn.model.AltinnRapportMelding
import no.ssb.kostra.altinn.model.AltinnRespondent
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportArguments

fun ValidationReportArguments.toAltinnReport(version: String): AltinnRapport =
    this.validationResult.reportEntries
        .groupBy { it.copy(lineNumbers = emptyList()) }
        .map { (key, group) ->
            key.copy(
                lineNumbers = group.flatMap { it.lineNumbers }.distinct()
                    .sorted()
            )
        }
        .map {
            AltinnRapportMelding(
                alvorlighetsgrad = it.severity.info.description,
                kontrollNavn = it.ruleName,
                meldingTekst = it.messageText.replace("<br/>", ""),
                linjenumre = it.lineNumbers,
            )
        }.let { reportEntries ->
            AltinnRapport(
                respondent =
                    with(this.kotlinArguments) {
                        AltinnRespondent(
                            aar = aargang.toInt(),
                            kvartal = kvartal,
                            skjema = skjema,
                            region = region,
                            orgnr = orgnr,
                        )
                    },
                antallKontroller = this.validationResult.numberOfControls,
                meldinger = reportEntries,
                alvorlighetsgrad = this.validationResult.reportEntries
                    .map { it.severity }
                    .let { severities ->
                        severities.maxByOrNull { it.ordinal } ?: Severity.OK
                    }
                    .info.description,
                versjon = version,
            )
        }
