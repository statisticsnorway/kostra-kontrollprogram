package no.ssb.kostra.validation.rule.regnskap.kostra.extensions

import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BALANSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_REGIONALE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_RESULTAT
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_DRIFT
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_INVESTERING
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
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
    getFieldAsIntegerDefaultEquals0(FIELD_ART) in 10..599

fun KostraRecord.isInntekt() =
    getFieldAsIntegerDefaultEquals0(FIELD_ART) in 600..999

fun KostraRecord.isOslo() =
    getFieldAsString(FIELD_REGION) == "030100"

fun KostraRecord.isOsloBydel() =
    getFieldAsString(FIELD_REGION).substring(0, 4) == "0301"
            && getFieldAsString(FIELD_REGION).substring(4, 6) != "00"
