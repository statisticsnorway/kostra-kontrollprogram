package no.ssb.kostra.validation.rule

import no.ssb.kostra.validation.report.Severity

abstract class AbstractStringRule(ruleName: String, severity: Severity) :
    AbstractRule<String>(ruleName, severity)