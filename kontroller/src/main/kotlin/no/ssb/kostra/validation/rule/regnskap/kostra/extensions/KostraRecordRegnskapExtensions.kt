package no.ssb.kostra.validation.rule.regnskap.kostra.extensions

import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BALANSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_RESULTAT
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_DRIFT
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_INVESTERING
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.KostraRecord

fun KostraRecord.isBevilgningRegnskap() =
    ACCOUNTING_TYPE_BEVILGNING in RegnskapConstants.getRegnskapTypeBySkjema(getFieldAsString(FIELD_SKJEMA))

fun KostraRecord.isBevilgningDriftRegnskap() =
    this.isBevilgningRegnskap()
            && ACCOUNT_TYPE_DRIFT in RegnskapConstants.getKontoTypeBySkjemaAndKontoklasse(
        getFieldAsString(FIELD_SKJEMA),
        getFieldAsString(FIELD_KONTOKLASSE)
    )

fun KostraRecord.isBevilgningInvesteringRegnskap() =
    this.isBevilgningRegnskap()
            && ACCOUNT_TYPE_INVESTERING in RegnskapConstants.getKontoTypeBySkjemaAndKontoklasse(
        getFieldAsString(FIELD_SKJEMA),
        getFieldAsString(FIELD_KONTOKLASSE)
    )

fun KostraRecord.isBalanseRegnskap() =
    ACCOUNTING_TYPE_BALANSE in RegnskapConstants.getRegnskapTypeBySkjema(getFieldAsString(FIELD_SKJEMA))

fun KostraRecord.isResultatRegnskap() =
    ACCOUNTING_TYPE_RESULTAT in RegnskapConstants.getRegnskapTypeBySkjema(getFieldAsString(FIELD_SKJEMA))

fun KostraRecord.isRegional() =
    ACCOUNTING_TYPE_REGIONALE in RegnskapConstants.getRegnskapTypeBySkjema(getFieldAsString(FIELD_SKJEMA))

fun KostraRecord.isUtgift() =
    getFieldAsIntegerOrDefault(FIELD_ART) in 10..599

fun KostraRecord.isInntekt() =
    getFieldAsIntegerOrDefault(FIELD_ART) in 600..999

fun KostraRecord.isOslo() =
    getFieldAsString(FIELD_REGION) == "030100"

fun KostraRecord.isOsloBydel() =
    getFieldAsString(FIELD_REGION).substring(0, 4) == OSLO_MUNICIPALITY_ID
            && getFieldAsString(FIELD_REGION).substring(4, 6) != "00"

fun KostraRecord.isLongyearbyen() =
    getFieldAsString(FIELD_REGION) == "211100"

fun KostraRecord.isAktiva() =
    getFieldAsIntegerOrDefault(FIELD_KAPITTEL) in 10..29

fun KostraRecord.isPassiva() =
    getFieldAsIntegerOrDefault(FIELD_KAPITTEL) in 30..5999

fun KostraRecord.isOsloInternRegnskap() =
    this.isOslo()
            && getFieldAsString(FIELD_SKJEMA) in listOf("0A", "0M")

fun KostraRecord.isKommuneRegnskap() =
    getFieldAsString(FIELD_SKJEMA) in listOf("0A", "0B", "0M", "0N")

fun KostraRecord.isFylkeRegnskap() =
    getFieldAsString(FIELD_SKJEMA) in listOf("0C", "0D", "0P", "0Q")