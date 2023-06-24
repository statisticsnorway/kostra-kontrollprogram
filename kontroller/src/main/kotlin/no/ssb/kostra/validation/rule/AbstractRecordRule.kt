package no.ssb.kostra.validation.rule

import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

abstract class AbstractRecordRule(
    ruleName: String,
    severity: Severity
) : AbstractRule<KostraRecord>(ruleName, severity)