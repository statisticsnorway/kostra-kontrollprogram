package no.ssb.kostra.validation.rule.regnskap

import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BALANSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNTING_TYPE_BEVILGNING
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_DRIFT
import no.ssb.kostra.area.regnskap.RegnskapConstants.ACCOUNT_TYPE_INVESTERING
import no.ssb.kostra.area.regnskap.RegnskapConstants.DERIVED_ACCOUNTING_TYPE
import no.ssb.kostra.area.regnskap.RegnskapConstants.DERIVED_KONTOKLASSE
import no.ssb.kostra.program.KostraRecord

fun KostraRecord.isBevilgningRegnskap() =
    getFieldAsString(DERIVED_ACCOUNTING_TYPE) == ACCOUNTING_TYPE_BEVILGNING

fun KostraRecord.isBevilgningDriftRegnskap() =
    getFieldAsString(DERIVED_ACCOUNTING_TYPE) == ACCOUNTING_TYPE_BEVILGNING
            && getFieldAsString(DERIVED_KONTOKLASSE) == ACCOUNT_TYPE_DRIFT

fun KostraRecord.isBevilgningInvesteringRegnskap() =
    getFieldAsString(DERIVED_ACCOUNTING_TYPE) == ACCOUNTING_TYPE_BEVILGNING
            && getFieldAsString(DERIVED_KONTOKLASSE) == ACCOUNT_TYPE_INVESTERING

fun KostraRecord.isBalanseRegnskap() =
    getFieldAsString(DERIVED_ACCOUNTING_TYPE) == ACCOUNTING_TYPE_BALANSE
