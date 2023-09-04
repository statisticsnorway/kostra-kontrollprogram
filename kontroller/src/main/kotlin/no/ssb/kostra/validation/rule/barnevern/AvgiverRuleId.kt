package no.ssb.kostra.validation.rule.barnevern

enum class AvgiverRuleId(val title: String) {
    AVGIVER_00("Avgiver K0: Feil antall avgivere"),
    AVGIVER_01("Avgiver K1: Validering av avgiver"),
    AVGIVER_02("Avgiver K2: Årgang"),
    AVGIVER_03("Avgiver K3: Organisasjonsnummer"),
    AVGIVER_04("Avgiver K4: Kommunenummer"),
    AVGIVER_06("Avgiver K6: Kommunenavn");
}