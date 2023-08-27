package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry


class Rule000HasAttachment : AbstractRule<List<String>>(
    "Kontroll 000 : Skal levere filuttrekk",
    Severity.FATAL
) {
    override fun validate(
        context: List<String>,
        arguments: KotlinArguments
    ): List<ValidationReportEntry>? =
        if (arguments.harVedlegg)
            if (arguments.inputFileContent.length != 1)
                null
            else
                createSingleReportEntryList(
                    messageText = "Det er krysset av i skjemaet at det finnes deltakere, men fil som kun inneholder et mellomrom er levert."
                )
        else
            if (arguments.inputFileContent.length != 1)
                createSingleReportEntryList(
                    messageText = ("Det er krysset av i skjemaet at det ikke finnes deltakere, men filen som er levert har annet innhold enn ett mellomrom."
                            + " Kryptert fil uten innhold kan lastes ned fra https://www.ssb.no/innrapportering/kostra-innrapportering<br/>"
                            + " -> Kontrollprogram og programmer til fagsystem for kommuner og leverandører<br/>"
                            + " -> Kvalifiseringsstønad<br/>"
                            + " -> Tom, kryptert fil (for dem som ikke har noen mottakere av kvalifiseringsstønad i ${arguments.aargang})<br/>"
                            )
                )
            else
                createSingleReportEntryList(
                    messageText = "Det er krysset av i skjemaet at det ikke finnes deltakere og fil som kun inneholder et mellomrom er levert.",
                    severity = Severity.OK
                )
}