package no.ssb.kostra.validation.rule.sosial.kvalifisering

import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.*
import no.ssb.kostra.validation.rule.sosial.rule.*

object KvalifiseringRules {

    val kvalifiseringRules = listOf(
        Rule03Kommunenummer(),
        Rule03Bydelsnummer(),
        Rule04OppgaveAar(),
        Rule05Fodselsnummer(),
        Rule06AlderUnder18Aar(),
        Rule07AlderEr68AarEllerOver(),
        Rule08Kjonn(),
        Rule09Sivilstand(),

        Control10Bu18(),
        Control11Bu18AntBu18(),
        Control12AntBu18Bu18(),
        Control13AntBu18(),
        Control14RegDato(),
        Control15VedtakDato(),
        Control16BegyntDato(),
        Control19KvalifiseringsprogramIAnnenKommune(),
        Control20KvalifiseringsprogramIAnnenKommuneKommunenummer(),
        Control20KvalifiseringsprogramIAnnenKommuneKommunenummer(),
        Control21Ytelser(),
        Control26MottattStotte(),
        Control27MottattOkonomiskSosialhjelp(),
        Control28MaanederMedKvalifiseringsstonad(),
        Control29KvalifiseringssumMangler(),
        Control30HarVarighetMenManglerKvalifiseringssum(),
        Control31HarKvalifiseringssumMenManglerVarighet(),
        Control32KvalifiseringssumOverMaksimum(),
        Control36StatusForDeltakelseIKvalifiseringsprogram(),
        Control37DatoForAvsluttetProgram(),
        Control38FullforteAvsluttedeProgramSituasjon(),
        Control39FullforteAvsluttedeProgramInntektkilde()
    )

    val duplicateRules = listOf(Rule05aFoedselsnummerDubletter(), Rule05bJournalnummerDubletter())
}